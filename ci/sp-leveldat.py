"""Package 4c (CI): sets Data.allowCommands=1 and Data.GameType=3 in a gzipped level.dat, with no dependency (a minimal
NBT reader and writer: Java's big-endian named-tag format)."""
import gzip, struct, sys

def read_payload(b, i, t):
    if t == 1: return b[i:i+1], i + 1
    if t == 2: return b[i:i+2], i + 2
    if t in (3, 5): return b[i:i+4], i + 4
    if t in (4, 6): return b[i:i+8], i + 8
    if t == 7:
        n = struct.unpack_from(">i", b, i)[0]; return b[i:i+4+n], i + 4 + n
    if t == 8:
        n = struct.unpack_from(">H", b, i)[0]; return b[i:i+2+n], i + 2 + n
    if t == 9:
        et, n = b[i], struct.unpack_from(">i", b, i + 1)[0]
        items, j = [], i + 5
        for _ in range(n):
            v, j = read_payload(b, j, et); items.append(v)
        return ("list", et, items), j
    if t == 10:
        out, j = [], i
        while True:
            tt = b[j]; j += 1
            if tt == 0: break
            n = struct.unpack_from(">H", b, j)[0]; name = b[j+2:j+2+n].decode("utf-8"); j += 2 + n
            v, j = read_payload(b, j, tt); out.append([tt, name, v])
        return ("compound", out), j
    if t == 11:
        n = struct.unpack_from(">i", b, i)[0]; return b[i:i+4+4*n], i + 4 + 4 * n
    if t == 12:
        n = struct.unpack_from(">i", b, i)[0]; return b[i:i+4+8*n], i + 4 + 8 * n
    raise ValueError("tag type %d" % t)

def write_payload(t, v):
    if t == 9:
        _, et, items = v
        return bytes([et]) + struct.pack(">i", len(items)) + b"".join(write_payload(et, x) for x in items)
    if t == 10:
        out = b""
        for tt, name, x in v[1]:
            nb = name.encode("utf-8")
            out += bytes([tt]) + struct.pack(">H", len(nb)) + nb + write_payload(tt, x)
        return out + b"\x00"
    return v

def put(comp, t, name, raw):
    for e in comp[1]:
        if e[1] == name:
            e[0], e[2] = t, raw; return
    comp[1].append([t, name, raw])

path = sys.argv[1]
b = gzip.decompress(open(path, "rb").read())
t = b[0]; n = struct.unpack_from(">H", b, 1)[0]; rootname = b[3:3+n]
root, _ = read_payload(b, 3 + n, t)
data = next(e[2] for e in root[1] if e[1] == "Data")
put(data, 1, "allowCommands", b"\x01")
put(data, 3, "GameType", struct.pack(">i", 3))
out = bytes([t]) + struct.pack(">H", n) + rootname + write_payload(t, root)
open(path, "wb").write(gzip.compress(out))
print("level.dat: commands allowed, spectator")
