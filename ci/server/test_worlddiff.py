#!/usr/bin/env python3
"""Self-test for ci/server/worlddiff.py on synthetic worlds (run in CI before the server suite's comparisons).

usage: python3 -I ci/server/test_worlddiff.py
"""
import json
import os
import struct
import sys
import tempfile
import zlib

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import worlddiff as wd  # noqa: E402


# ---- a minimal NBT writer: ("type", value) pairs for the tags Python can't infer
def enc(v):
    if isinstance(v, tuple):
        t, x = v
        if t == "L":
            return wd.LONG_ARRAY, struct.pack(">i", len(x)) + b"".join(struct.pack(">q", n) for n in x)
        return {"b": (wd.BYTE, ">b"), "f": (wd.FLOAT, ">f"), "l": (wd.LONG, ">q")}[t][0], \
            struct.pack({"b": ">b", "f": ">f", "l": ">q"}[t], x)
    if isinstance(v, bool):
        return wd.BYTE, struct.pack(">b", int(v))
    if isinstance(v, int):
        return wd.INT, struct.pack(">i", v)
    if isinstance(v, float):
        return wd.DOUBLE, struct.pack(">d", v)
    if isinstance(v, str):
        b = v.encode()
        return wd.STRING, struct.pack(">H", len(b)) + b
    if isinstance(v, list):
        if not v:
            return wd.LIST, bytes([wd.END]) + struct.pack(">i", 0)
        items = [enc(x) for x in v]
        return wd.LIST, bytes([items[0][0]]) + struct.pack(">i", len(v)) + b"".join(p for _, p in items)
    if isinstance(v, dict):
        out = b""
        for k, x in v.items():
            t, p = enc(x)
            kb = k.encode()
            out += bytes([t]) + struct.pack(">H", len(kb)) + kb + p
        return wd.COMPOUND, out + bytes([wd.END])
    raise TypeError(v)


def nbt(root):
    return bytes([wd.COMPOUND]) + struct.pack(">H", 0) + enc(root)[1]


def write_region(path, chunks):
    """chunks: {(cx, cz): root dict}; all in region 0,0."""
    header = bytearray(8192)
    body = bytearray()
    sector = 2
    for (cx, cz), root in chunks.items():
        data = zlib.compress(nbt(root))
        blob = struct.pack(">I", len(data) + 1) + bytes([2]) + data
        blob += bytes((-len(blob)) % 4096)
        n = len(blob) // 4096
        idx = (cx & 31) + (cz & 31) * 32
        header[idx * 4:idx * 4 + 4] = struct.pack(">I", (sector << 8) | n)
        body += blob
        sector += n
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "wb") as f:
        f.write(bytes(header) + bytes(body))


def pack(indices, bits):
    per = 64 // bits
    words = []
    for i in range(0, len(indices), per):
        w = 0
        for k, v in enumerate(indices[i:i + per]):
            w |= v << (k * bits)
        words.append(w - (1 << 64) if w >= 1 << 63 else w)
    return words


def chunk(stone_at=None, chest_items=1, palette_order=("minecraft:air", "minecraft:stone"), tick_delay=None):
    idx = [0] * 4096
    s = palette_order.index("minecraft:stone")
    a = palette_order.index("minecraft:air")
    idx = [a] * 4096
    for i in range(256):
        idx[i] = s
    if stone_at is not None:
        idx[stone_at] = s
    pal = [{"Name": n} for n in palette_order]
    ticks = [] if tick_delay is None else [{"i": "minecraft:sand", "x": 3, "y": 2, "z": 3, "t": tick_delay, "p": 0}]
    return {"DataVersion": 9999, "xPos": 0, "zPos": 0, "Status": "minecraft:full", "LastUpdate": ("l", 100),
            "block_ticks": ticks,
            "sections": [{"Y": ("b", 0), "block_states": {"palette": pal, "data": ("L", pack(idx, 4))}}],
            "block_entities": [{"id": "minecraft:chest", "x": 1, "y": 1, "z": 1,
                                "Items": [{"id": "minecraft:dirt", "count": chest_items}]}]}


def ents(*mobs):
    def one(m):
        t, x, z, h = m[:4]
        e = {"id": t, "Pos": [x, 64.0, z], "Health": ("f", h), "UUID": [1, 2, 3, int(x * 1000) % 7 if t == "x" else 4]}
        if len(m) > 4:
            e["Item"] = {"id": m[4], "count": 1}
        return e
    return {"DataVersion": 9999, "Position": ("L", [0]), "Entities": [one(m) for m in mobs]}


def world(root, **kw):
    write_region(os.path.join(root, "region", "r.0.0.mca"), {(0, 0): chunk(**{k: v for k, v in kw.items() if k != "mobs"})})
    write_region(os.path.join(root, "entities", "r.0.0.mca"), {(0, 0): ents(*kw.get("mobs", [("minecraft:cow", 1.5, 2.5, 10.0)]))})
    write_region(os.path.join(root, "DIM-1", "region", "r.0.0.mca"), {(0, 0): chunk()})


def run(a, b, mode):
    out = os.path.join(os.path.dirname(a), "out-%s.json" % mode)
    wd.main([a, b, out, "--mode=" + mode])
    with open(out) as f:
        return json.load(f)["total"]


def main():
    fails = []

    def check(name, cond, got):
        print(("ok   " if cond else "FAIL ") + name + "  " + json.dumps(got))
        if not cond:
            fails.append(name)

    with tempfile.TemporaryDirectory() as t:
        def w(name, **kw):
            p = os.path.join(t, name)
            world(p, **kw)
            return p
        base, same = w("base"), w("same")
        tot = run(base, same, "world")
        check("identical worlds: no difference", tot["differences"] == 0 and tot["chunks"] == 2, tot)
        tot = run(base, same, "gen")
        check("identical worlds, gen: no difference", tot["differences"] == 0, tot)
        tot = run(base, w("block", stone_at=4000), "world")
        check("one changed block found", tot["blocks"] == 1 and tot["differences"] == 1, tot)
        tot = run(base, w("pal", palette_order=("minecraft:stone", "minecraft:air")), "world")
        check("palette order alone: no block difference", tot["blocks"] == 0 and tot["differences"] == 0, tot)
        tot = run(base, w("pal2", palette_order=("minecraft:stone", "minecraft:air")), "gen")
        check("palette order: a byte difference in gen mode", tot["differ"] == 1, tot)
        tot = run(base, w("chest", chest_items=2), "world")
        check("block entity change found", tot["blockEntities"] == 1, tot)
        tot = run(base, w("cowmove", mobs=[("minecraft:cow", 1.5, 2.75, 10.0)]), "world")
        check("entity moved", tot["entities"] == 1, tot)
        tot = run(base, w("cowhurt", mobs=[("minecraft:cow", 1.5, 2.5, 9.0)]), "world")
        check("entity health changed", tot["entities"] == 1, tot)
        tot = run(base, w("cow2", mobs=[("minecraft:cow", 1.5, 2.5, 10.0), ("minecraft:cow", 3.0, 3.0, 10.0)]), "world")
        check("extra entity", tot["entities"] == 1, tot)
        tot = run(base, w("drift", mobs=[("minecraft:cow", 1.5 + 1e-7, 2.5, 10.0)]), "world")
        check("entity moved by 1e-7 (exact comparison)", tot["entities"] == 1, tot)
        i1 = w("item1", mobs=[("minecraft:item", 1.5, 2.5, 5.0, "minecraft:diamond")])
        tot = run(i1, w("item2", mobs=[("minecraft:item", 1.5, 2.5, 5.0, "minecraft:dirt")]), "world")
        check("item entity holding another stack", tot["entities"] == 1, tot)
        tot = run(w("tick1", tick_delay=2), w("tick2", tick_delay=3), "world")
        check("scheduled tick differs", tot["scheduledTicks"] == 1, tot)
        out = os.path.join(t, "befield.json")
        wd.main([base, w("chest3", chest_items=3), out, "--mode=world"])
        with open(out) as f:
            keys = json.load(f)["dimensions"]["region"]["keys"]
        check("block entity keyed by field", keys == ["e1,1,1/Items"], {"keys": keys})
        odd = [{"": "minecraft:wheat", "properties": {"age": "7"}}, {"": "minecraft:wheat", "properties": {"age": "0"}}]
        check("unknown palette shape never collapses two states", wd.state_name(odd[0]) != wd.state_name(odd[1]), {})
        nether = w("nonether")
        import shutil
        shutil.rmtree(os.path.join(nether, "DIM-1"))
        tot = run(base, nether, "world")
        check("missing dimension", tot["missingDimensions"] == 1 and tot["differences"] == 1, tot)
        # --areas: only listed dimensions and chunks
        af = os.path.join(t, "areas.txt")
        with open(af, "w") as f:
            f.write("# test\nminecraft:overworld 0 0 0 0\n")
        out = os.path.join(t, "areas.json")
        wd.main([base, w("pal3", palette_order=("minecraft:stone", "minecraft:air")), out, "--mode=gen", "--areas=" + af])
        with open(out) as f:
            rep = json.load(f)
        check("areas: overworld chunk compared, nether left out",
              list(rep["dimensions"]) == ["region"] and rep["total"]["differ"] == 1, rep["total"])
        with open(af, "w") as f:
            f.write("minecraft:overworld 5 5 6 6\n")
        wd.main([base, same, out, "--mode=gen", "--areas=" + af])
        with open(out) as f:
            rep = json.load(f)
        check("areas: chunks outside the area left out", rep["total"]["chunks"] == 0, rep["total"])
        check("palette entries in 26.x's forms", wd.state_name({"": "minecraft:clay"}) == "minecraft:clay"
              and wd.state_name({"id": "minecraft:cave_vines", "properties": {"berries": "true", "age": "23"}})
              == "minecraft:cave_vines[age=23,berries=true]", {})
        check("palette entries as strings", wd.state_name("minecraft:stone") == "minecraft:stone"
              and wd.state_name({"Name": "minecraft:stone", "Properties": {"b": "1", "a": "2"}}) == "minecraft:stone[a=2,b=1]", {})
        # a word straddling the limit: 5-bit palettes leave 4 unused bits per long
        vals = [i % 17 for i in range(4096)]
        got = wd.unpack_states(["x"] * 17, pack(vals, 5))
        check("5-bit unpack", got == vals, {"n": len(got)})
    print("worlddiff self-test: %s" % ("FAILED " + ", ".join(fails) if fails else "passed"))
    return 1 if fails else 0


if __name__ == "__main__":
    sys.exit(main())
