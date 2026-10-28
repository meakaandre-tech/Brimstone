#!/usr/bin/env python3
"""Compares two saved worlds (phase 2, P2-0): the server suite's world diff and worldgen diff.

usage: worlddiff.py A_WORLD B_WORLD OUT.json [--mode world|gen] [--max-examples N] [--areas=FILE]

--areas=FILE: only the chunks (and entity chunks) inside the areas listed in FILE ('dimension x1 z1 x2 z2' per line, chunk
  coordinates, the harness's gen file), and only the dimensions it lists.

Every directory named "region" under a world is one dimension's chunk store (key: its path relative to the world, e.g.
"region", "DIM-1/region", "dimensions/the_sift/the_sift/region"), so vanilla's and modded dimensions are found the same
way whatever the save layout. Entities live in the sibling "entities" directory (Minecraft 1.17 and later).

--mode gen (worldgen diff): every chunk present in either world, compared byte for byte on its decompressed NBT. A
  chunk that differs is decoded to name the first differing path. Chunks present on one side only are differences.
--mode world (world diff after a scripted run):
  - block states: every block of every section, decoded from palette and packed data, compared exactly;
  - block entities: compared exactly (whole NBT) by position;
  - entities: per dimension, the multiset of (type, position, health) compared exactly, then summarised per type as
    counts and the largest position and health difference after pairing nearest same-type entities.

Writes OUT.json: {"mode", "dimensions": {key: {"chunks": n, "onlyA": n, "onlyB": n, ...counts..., "examples": [...]}},
"total": {...}}. Exit status 0 always: the gate is in ci/server/compare-server.py, against the vanilla a/b noise.
Standard library only (zlib/gzip region chunks; LZ4 chunks need the lz4 module and are reported if it is missing).
"""
import gzip
import json
import math
import os
import struct
import sys
import zlib

# ---------------------------------------------------------------- NBT

END, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, BYTE_ARRAY, STRING, LIST, COMPOUND, INT_ARRAY, LONG_ARRAY = range(13)


class Reader:
    def __init__(self, data):
        self.d = data
        self.i = 0

    def take(self, n):
        b = self.d[self.i:self.i + n]
        if len(b) != n:
            raise ValueError("truncated NBT")
        self.i += n
        return b

    def u8(self):
        return self.take(1)[0]

    def s(self, fmt):
        return struct.unpack(">" + fmt, self.take(struct.calcsize(">" + fmt)))[0]

    def string(self):
        n = self.s("H")
        return self.take(n).decode("utf-8", "surrogatepass")

    def payload(self, t):
        if t == BYTE:
            return self.s("b")
        if t == SHORT:
            return self.s("h")
        if t == INT:
            return self.s("i")
        if t == LONG:
            return self.s("q")
        if t == FLOAT:
            return self.s("f")
        if t == DOUBLE:
            return self.s("d")
        if t == BYTE_ARRAY:
            n = self.s("i")
            return list(struct.unpack(">%db" % n, self.take(n)))
        if t == STRING:
            return self.string()
        if t == LIST:
            et = self.u8()
            n = self.s("i")
            return [self.payload(et) for _ in range(max(n, 0))]
        if t == COMPOUND:
            out = {}
            while True:
                ct = self.u8()
                if ct == END:
                    return out
                name = self.string()
                out[name] = self.payload(ct)
        if t == INT_ARRAY:
            n = self.s("i")
            return list(struct.unpack(">%di" % n, self.take(4 * n)))
        if t == LONG_ARRAY:
            n = self.s("i")
            return list(struct.unpack(">%dq" % n, self.take(8 * n)))
        raise ValueError("bad NBT tag %d" % t)


def read_nbt(data):
    """The root compound of an NBT blob (named or unnamed root)."""
    r = Reader(data)
    t = r.u8()
    if t != COMPOUND:
        raise ValueError("root is not a compound")
    r.string()
    return r.payload(COMPOUND)


# ---------------------------------------------------------------- region files

def region_chunks(path):
    """{(cx, cz): (compression, raw NBT bytes or None, error)} for one .mca file."""
    out = {}
    base = os.path.basename(path).split(".")
    try:
        rx, rz = int(base[1]), int(base[2])
    except (IndexError, ValueError):
        return out
    with open(path, "rb") as f:
        data = f.read()
    if len(data) < 8192:
        return out
    for idx in range(1024):
        loc = struct.unpack(">I", data[idx * 4:idx * 4 + 4])[0]
        off, count = loc >> 8, loc & 0xFF
        if off == 0 or count == 0:
            continue
        cx, cz = rx * 32 + (idx & 31), rz * 32 + (idx >> 5)
        start = off * 4096
        if start + 5 > len(data):
            out[(cx, cz)] = (None, None, "sector out of file")
            continue
        length = struct.unpack(">I", data[start:start + 4])[0]
        comp = data[start + 4]
        body = data[start + 5:start + 4 + length]
        external = bool(comp & 0x80)
        comp &= 0x7F
        if external:
            ext = os.path.join(os.path.dirname(path), "c.%d.%d.mcc" % (cx, cz))
            try:
                with open(ext, "rb") as f:
                    body = f.read()
            except OSError:
                out[(cx, cz)] = (comp, None, "missing external chunk file")
                continue
        try:
            if comp == 1:
                raw = gzip.decompress(body)
            elif comp == 2:
                raw = zlib.decompress(body)
            elif comp == 3:
                raw = bytes(body)
            elif comp == 4:
                try:
                    import lz4.block  # optional
                except ImportError:
                    out[(cx, cz)] = (comp, None, "LZ4 chunk; install the lz4 module")
                    continue
                raw = lz4_frame(bytes(body), lz4)
            else:
                out[(cx, cz)] = (comp, None, "unknown compression %d" % comp)
                continue
        except Exception as e:  # noqa: BLE001 - a broken chunk is a reported difference, not a crash
            out[(cx, cz)] = (comp, None, "decompress failed: %s" % e)
            continue
        out[(cx, cz)] = (comp, raw, None)
    return out


def lz4_frame(body, lz4):
    """Minecraft's LZ4 chunks are LZ4Java block streams ("LZ4Block" magic, per-block headers)."""
    out = bytearray()
    i = 0
    while i + 21 <= len(body) and body[i:i + 8] == b"LZ4Block":
        token = body[i + 8]
        clen, dlen = struct.unpack("<ii", body[i + 9:i + 17])
        i += 21
        method = token & 0xF0
        chunk = body[i:i + clen]
        i += clen
        if dlen == 0:
            break
        out += chunk if method == 0x10 else lz4.block.decompress(chunk, uncompressed_size=dlen)
    return bytes(out)


def store_chunks(directory):
    chunks = {}
    if not os.path.isdir(directory):
        return chunks
    for name in sorted(os.listdir(directory)):
        if name.endswith(".mca"):
            chunks.update(region_chunks(os.path.join(directory, name)))
    return chunks


def dimensions(world):
    """{key: dimension dir} for every 'region' directory under the world."""
    dims = {}
    for root, subdirs, _ in os.walk(world):
        if "region" in subdirs:
            rel = os.path.relpath(os.path.join(root, "region"), world)
            dims[rel] = root
    return dims


# ---------------------------------------------------------------- decoding

def state_name(entry):
    """A palette entry as one canonical string, in each form 26.x saves: a plain string, {"": "minecraft:stone"} for a
    block with no states, {"id": ..., "properties": {...}}, or the older {"Name": ..., "Properties": {...}}."""
    if isinstance(entry, str):
        return entry
    keys = set(entry)
    if keys == {""} and isinstance(entry[""], str):
        return entry[""]
    for name_key, props_key in (("id", "properties"), ("Name", "Properties")):
        if name_key in keys and keys <= {name_key, props_key} and isinstance(entry[name_key], str):
            name, props = entry[name_key], entry.get(props_key) or {}
            if props:
                name += "[" + ",".join("%s=%s" % (k, props[k]) for k in sorted(props)) + "]"
            return name
    # any other shape: the whole entry, so two different states never read as one (this can only add differences)
    return "?" + canon(entry)


def unpack_states(palette, data, count=4096):
    """Vanilla's packed palette storage: entries never span two longs (since 1.16)."""
    if len(palette) <= 1 or not data:
        return [0] * count
    bits = max(4, (len(palette) - 1).bit_length())
    per = 64 // bits
    mask = (1 << bits) - 1
    out = []
    for word in data:
        w = word & 0xFFFFFFFFFFFFFFFF
        for k in range(per):
            if len(out) == count:
                break
            out.append((w >> (k * bits)) & mask)
    out += [0] * (count - len(out))
    return out


ODD = []  # palette entries the decoder didn't recognise (a few samples, written to the report)


def chunk_blocks(nbt):
    """{section y: list of 4096 state names} (y order, then z, then x, as stored)."""
    sections = nbt.get("sections") or nbt.get("Level", {}).get("Sections") or []
    out = {}
    for sec in sections:
        bs = sec.get("block_states")
        if not bs:
            continue
        pal = [state_name(e) for e in bs.get("palette", [])]
        if any(n.startswith("?") for n in pal) and len(ODD) < 5:
            ODD.append({"Y": sec.get("Y"), "palette": [n[:200] for n in pal if n.startswith("?")][:5]})
        idx = unpack_states(pal, bs.get("data") or [])
        out[sec.get("Y")] = [pal[i] if i < len(pal) else "<bad index %d>" % i for i in idx]
    return out


def canon(v):
    return json.dumps(v, sort_keys=True, separators=(",", ":"))


def first_difference(a, b, path=""):
    if type(a) is not type(b):
        return path or "/"
    if isinstance(a, dict):
        for k in sorted(set(a) | set(b)):
            if k not in a or k not in b:
                return "%s/%s" % (path, k)
            d = first_difference(a[k], b[k], "%s/%s" % (path, k))
            if d:
                return d
        return None
    if isinstance(a, list):
        if len(a) != len(b):
            return "%s[len %d!=%d]" % (path, len(a), len(b))
        for i, (x, y) in enumerate(zip(a, b)):
            d = first_difference(x, y, "%s[%d]" % (path, i))
            if d:
                return d
        return None
    if isinstance(a, float) and math.isnan(a) and math.isnan(b):
        return None
    return None if a == b else (path or "/")


# ---------------------------------------------------------------- comparisons

def diff_gen(da, db, res, max_ex, area=None):
    ca, cb = store_chunks(os.path.join(da, "region")), store_chunks(os.path.join(db, "region"))
    if area is not None:
        ca = {k: v for k, v in ca.items() if area(k)}
        cb = {k: v for k, v in cb.items() if area(k)}
    res.update(chunks=len(set(ca) | set(cb)), onlyA=0, onlyB=0, differ=0, unreadable=0)
    keys = res.setdefault("keys", [])
    for pos in sorted(set(ca) | set(cb)):
        if pos not in cb:
            res["onlyA"] += 1
            keys.append("c%d,%d" % pos)
            add_example(res, max_ex, {"chunk": pos, "what": "only in A"})
            continue
        if pos not in ca:
            res["onlyB"] += 1
            keys.append("c%d,%d" % pos)
            add_example(res, max_ex, {"chunk": pos, "what": "only in B"})
            continue
        (_, ra, ea), (_, rb, eb) = ca[pos], cb[pos]
        if ra is None or rb is None:
            res["unreadable"] += 1
            keys.append("c%d,%d" % pos)
            add_example(res, max_ex, {"chunk": pos, "what": ea or eb})
            continue
        if ra == rb:
            continue
        res["differ"] += 1
        keys.append("c%d,%d" % pos)
        path = None
        try:
            path = first_difference(read_nbt(ra), read_nbt(rb))
        except ValueError as e:
            path = "unparsable: %s" % e
        add_example(res, max_ex, {"chunk": pos, "what": "bytes differ", "first": path or "key order only"})


def entities_of(dim, area=None):
    """([(type, position, health, held item)], [unreadable entity chunk positions]). An item entity's record carries
    the stack it holds, so the right item in the wrong place and the wrong item in the right place both differ."""
    out, bad = [], []
    for pos, (_, raw, err) in store_chunks(os.path.join(dim, "entities")).items():
        if area is not None and not area(pos):
            continue
        if raw is None:
            bad.append(pos)
            continue
        for e in read_nbt(raw).get("Entities", []):
            p = e.get("Pos") or [0.0, 0.0, 0.0]
            out.append((e.get("id", "?"), tuple(p), e.get("Health"), canon(e["Item"]) if "Item" in e else None,
                        canon(e.get("UUID"))))
    return out, bad


def scheduled(nbt, kind):
    """{(x, y, z): canonical list of the pending ticks there} for a chunk's block_ticks or fluid_ticks."""
    out = {}
    for t in nbt.get(kind) or []:
        out.setdefault((t.get("x"), t.get("y"), t.get("z")), []).append(canon(t))
    return {k: sorted(v) for k, v in out.items()}


def diff_world(da, db, res, max_ex, area=None):
    ca, cb = store_chunks(os.path.join(da, "region")), store_chunks(os.path.join(db, "region"))
    if area is not None:
        ca = {k: v for k, v in ca.items() if area(k)}
        cb = {k: v for k, v in cb.items() if area(k)}
    res.update(chunks=len(set(ca) | set(cb)), onlyA=0, onlyB=0, unreadable=0, blocks=0, blockEntities=0, scheduledTicks=0)
    keys = res.setdefault("keys", [])
    for pos in sorted(set(ca) | set(cb)):
        if pos not in ca or pos not in cb:
            res["onlyA" if pos in ca else "onlyB"] += 1
            keys.append("c%d,%d" % pos)
            add_example(res, max_ex, {"chunk": pos, "what": "only in " + ("A" if pos in ca else "B")})
            continue
        (_, ra, ea), (_, rb, eb) = ca[pos], cb[pos]
        if ra is None or rb is None:
            res["unreadable"] += 1
            keys.append("c%d,%d" % pos)
            add_example(res, max_ex, {"chunk": pos, "what": ea or eb})
            continue
        if ra == rb:
            continue
        na, nb = read_nbt(ra), read_nbt(rb)
        ba, bb = chunk_blocks(na), chunk_blocks(nb)
        for y in sorted(set(ba) | set(bb), key=lambda v: (v is None, v)):
            sa, sb = ba.get(y), bb.get(y)
            if sa is None or sb is None:
                sa = sa or ["minecraft:air"] * 4096
                sb = sb or ["minecraft:air"] * 4096
            for i in range(4096):
                if sa[i] != sb[i]:
                    res["blocks"] += 1
                    x, z, yy = pos[0] * 16 + (i & 15), pos[1] * 16 + ((i >> 4) & 15), (y or 0) * 16 + (i >> 8)
                    if len(keys) < MAX_KEYS:
                        keys.append("b%d,%d,%d" % (x, yy, z))
                    add_example(res, max_ex, {"block": [x, yy, z], "a": sa[i], "b": sb[i]})
        # block entities: keyed by position and top-level field, so one field that varies in vanilla's own runs (a
        # bearing's angle) leaves the block entity's other fields gated
        bea = {(e.get("x"), e.get("y"), e.get("z")): e for e in na.get("block_entities", [])}
        beb = {(e.get("x"), e.get("y"), e.get("z")): e for e in nb.get("block_entities", [])}
        for p in sorted(set(bea) | set(beb), key=str):
            a, b = bea.get(p), beb.get(p)
            if canon(a) == canon(b):
                continue
            res["blockEntities"] += 1
            add_example(res, max_ex, {"blockEntity": list(p), "first": first_difference(a or {}, b or {})})
            if a is None or b is None:
                keys.append("e%s,%s,%s" % p)
                continue
            for f in sorted(set(a) | set(b)):
                if canon(a.get(f)) != canon(b.get(f)):
                    keys.append("e%s,%s,%s/%s" % (p + (f,)))
        # scheduled block and fluid ticks still pending when the world was saved
        for kind, tag in (("block_ticks", "sb"), ("fluid_ticks", "sf")):
            ta, tb = scheduled(na, kind), scheduled(nb, kind)
            for p in sorted(set(ta) | set(tb), key=str):
                if ta.get(p) != tb.get(p):
                    res["scheduledTicks"] += 1
                    keys.append("%s%s,%s,%s" % ((tag,) + p))
                    add_example(res, max_ex, {"scheduledTick": list(p), "kind": kind, "a": ta.get(p), "b": tb.get(p)})
    # entities, per type: the exact multiset of (position, health, held item), and a summary
    (ea, bad_a), (eb, bad_b) = entities_of(da, area), entities_of(db, area)
    for pos in sorted(set(bad_a) | set(bad_b)):
        res["unreadable"] += 1
        keys.append("ec%d,%d" % pos)
    res["entitiesA"], res["entitiesB"] = len(ea), len(eb)
    res["entitiesExact"] = sorted(map(canon, ea)) == sorted(map(canon, eb))
    # each entity by what it is: records (type, exact position, health, held stack) found on both sides cancel out,
    # and every record left over on either side is a difference keyed by its type and the block it is in, so one
    # entity that varies in vanilla's own runs leaves the others gated. UUIDs are not compared: entities that
    # worldgen spawns get UUIDs that don't repeat between runs, everything else about them does.
    left = {}
    for sign, lst in ((1, ea), (-1, eb)):
        for t, p, h, item, _ in lst:
            k = canon((t, p, h, item))
            left[k] = left.get(k, 0) + sign
    for k, n in left.items():
        if n:
            t, p = json.loads(k)[:2]
            keys.append("t%s@%d,%d,%d" % (t, math.floor(p[0]), math.floor(p[1]), math.floor(p[2])))
    types = {}
    for side, lst in (("a", ea), ("b", eb)):
        for t, p, h, item, _ in lst:
            types.setdefault(t, {"a": [], "b": []})[side].append((p, h, item))
    res["entityTypes"] = {}
    res["entities"] = 0
    for t in sorted(types):
        a, b = types[t]["a"], types[t]["b"]
        maxd, maxh = pair_distances(a, b)
        exact = sorted(map(canon, a)) == sorted(map(canon, b))
        res["entityTypes"][t] = {"a": len(a), "b": len(b), "maxPosDelta": round(maxd, 9), "maxHealthDelta": round(maxh, 9), "exact": exact}
        if not exact:
            # any difference at all, however small (a 1e-7 drift in position included): the comparison is exact;
            # the keys are the records left over (above)
            res["entities"] += 1
            add_example(res, max_ex, {"entityType": t, "a": len(a), "b": len(b), "maxPosDelta": maxd, "maxHealthDelta": maxh})


def pair_distances(a, b):
    """Greedy nearest pairing of same-type entities: the largest distance and health gap over the pairs."""
    rest = list(b)
    maxd = maxh = 0.0
    for p, h, _ in a:
        if not rest:
            break
        j = min(range(len(rest)), key=lambda k: dist(p, rest[k][0]))
        q, hb, _ = rest.pop(j)
        maxd = max(maxd, dist(p, q))
        if h is not None and hb is not None:
            maxh = max(maxh, abs(h - hb))
    return maxd, maxh


def dist(p, q):
    return math.sqrt(sum((x - y) ** 2 for x, y in zip(p, q)))


def add_example(res, max_ex, ex):
    exs = res.setdefault("examples", [])
    if len(exs) < max_ex:
        exs.append(ex)


MAX_KEYS = 500_000
DIFF_KEYS = ("onlyA", "onlyB", "unreadable", "differ", "blocks", "blockEntities", "scheduledTicks", "entities")


def dimension_dirs(dim):
    """Where a dimension id's chunks live: 26.x's dimensions/<namespace>/<path>, or the older layout."""
    ns, _, path = dim.partition(":")
    old = {"minecraft:overworld": ".", "minecraft:the_nether": "DIM-1", "minecraft:the_end": "DIM1"}.get(dim)
    return ["dimensions/%s/%s" % (ns, path)] + ([old] if old else [])


def read_areas(path):
    """{dimension dir: predicate on (cx, cz)} from lines 'dimension x1 z1 x2 z2' (chunk coordinates, inclusive)."""
    boxes = {}
    with open(path) as f:
        for line in f:
            line = line.strip()
            if not line or line.startswith("#"):
                continue
            p = line.split()
            box = tuple(int(v) for v in p[1:5])
            for d in dimension_dirs(p[0]):
                boxes.setdefault(os.path.normpath(d), []).append(box)
    return {d: (lambda bs: lambda c: any(x1 <= c[0] <= x2 and z1 <= c[1] <= z2 for x1, z1, x2, z2 in bs))(bs)
            for d, bs in boxes.items()}


def main(argv):
    args = [a for a in argv if not a.startswith("--")]
    opts = dict(a[2:].split("=", 1) if "=" in a else (a[2:], "1") for a in argv if a.startswith("--"))
    if len(args) != 3:
        print(__doc__)
        return 2
    wa, wb, out = args
    mode = opts.get("mode", "world")
    max_ex = int(opts.get("max-examples", "20"))
    da, db = dimensions(wa), dimensions(wb)
    areas = read_areas(opts["areas"]) if "areas" in opts else None
    report = {"mode": mode, "a": wa, "b": wb, "dimensions": {}, "total": {}}
    for key in sorted(set(da) | set(db)):
        res = {}
        area = None
        if areas is not None:
            area = areas.get(os.path.normpath(os.path.dirname(key)))
            if area is None:
                continue  # a dimension the area file doesn't list
        if key not in da or key not in db:
            res["missing"] = "A" if key not in da else "B"
        elif mode == "gen":
            diff_gen(da[key], db[key], res, max_ex, area)
        else:
            diff_world(da[key], db[key], res, max_ex, area)
        report["dimensions"][key] = res
    tot = report["total"]
    for res in report["dimensions"].values():
        for k in DIFF_KEYS + ("chunks",):
            tot[k] = tot.get(k, 0) + res.get(k, 0)
        tot["missingDimensions"] = tot.get("missingDimensions", 0) + (1 if "missing" in res else 0)
    tot["differences"] = sum(tot.get(k, 0) for k in DIFF_KEYS) + tot["missingDimensions"] if report["dimensions"] else 1
    if ODD:
        report["oddPalettes"] = ODD
    with open(out, "w") as f:
        json.dump(report, f, indent=1, default=list)
    print("%s diff %s vs %s: %d chunks, %d differences (%s)" % (
        mode, wa, wb, tot.get("chunks", 0), tot["differences"],
        ", ".join("%s %d" % (k, tot[k]) for k in DIFF_KEYS if tot.get(k))))
    return 0


if __name__ == "__main__":
    sys.exit(main(sys.argv[1:]))
