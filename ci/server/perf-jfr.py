#!/usr/bin/env python3
"""Summarises a Java Flight Recorder recording of a perf run (phase 2, P2-2; ci/server/perf-run.sh, round 4).

usage: perf-jfr.py RECORDING.jfr OUT.json

Reads the server thread's execution samples (`jfr print --events jdk.ExecutionSample`) taken while the server ticks
(stacks through MinecraftServer.tickServer), and writes:
  - samples: how many;
  - phases: for each phase in PHASES, the share of samples whose stack passes through it (inclusive; a sample can be
    in several phases, e.g. entities and entity movement);
  - self: the methods samples were in (the top frame), top 60;
  - inclusive: the methods on the stack, each counted once per sample, top 120.
Method names only (Class.method, no code): the report is published on the ci-logs branch.
"""
import json
import re
import subprocess
import sys
from collections import Counter

# phases by the frame that marks them (Class.method, matched against frames without their package); the first group
# is the tick's top level, the rest are inside entity ticking
PHASES = [
    ("tick (all)", r"^MinecraftServer\.tickServer$"),
    ("levels", r"^ServerLevel\.tick$"),
    ("chunk source (chunks, tickets, tracking)", r"^ServerChunkCache\.tick$"),
    ("chunk ticking (random ticks, precipitation, spawning)", r"^ServerChunkCache\.tickChunks$"),
    ("random ticks and precipitation", r"^ServerLevel\.tickChunk$"),
    ("natural spawning", r"^NaturalSpawner\.spawnForChunk$|^NaturalSpawner\.spawnCategoryForChunk$"),
    ("scheduled block and fluid ticks", r"^LevelTicks\.tick$"),
    ("block events", r"^ServerLevel\.runBlockEvents$"),
    ("block entities", r"^Level\.tickBlockEntities$"),
    ("entities", r"^EntityTickList\.forEach$"),
    ("entity tracking (sending)", r"^ChunkMap\.tick$|^ServerEntity\.sendChanges$"),
    ("entity sections", r"^PersistentEntitySectionManager\.tick$|^EntitySectionStorage\."),
    ("  entity movement (Entity.move)", r"^Entity\.move$"),
    ("  collisions (Entity.collide)", r"^Entity\.collide$"),
    ("  block collisions", r"^BlockCollisions\.|^CollisionGetter\.getBlockCollisions$|^Shapes\.collide$|^VoxelShape\.collide"),
    ("  entity collisions (shapes of entities)", r"^CollisionGetter\.getEntityCollisions$|^EntityGetter\.getEntityCollisions$|^Level\.getEntityCollisions$"),
    ("  pushing (LivingEntity.pushEntities)", r"^LivingEntity\.pushEntities$"),
    ("  entity lookups (getEntities)", r"^Level\.getEntities$|^EntityGetter\.getEntities|^EntitySectionStorage\.getEntities|^LevelEntityGetter"),
    ("  inside-block effects", r"^Entity\.checkInsideBlocks$|^Entity\.applyEffectsFromBlocks$"),
    ("  fluids (in water, pushing)", r"^Entity\.updateInWaterStateAndDoFluidPushing$|^Entity\.updateFluidHeightAndDoFluidPushing$|^Entity\.updateFluidInteraction"),
    ("  AI goals and brains", r"^GoalSelector\.tick$|^Brain\.tick$|^Mob\.serverAiStep$"),
    ("  pathfinding", r"^PathNavigation\.|^PathFinder\."),
    ("  travel (LivingEntity.travel)", r"^LivingEntity\.travel$"),
    ("  base tick (Entity.baseTick)", r"^Entity\.baseTick$|^LivingEntity\.baseTick$"),
    ("  block state reads (getBlockState)", r"^Level\.getBlockState$|^LevelChunk\.getBlockState$|^ServerLevel\.getBlockState$|^PathNavigationRegion\.getBlockState$"),
    ("  chunk lookups (getChunk)", r"^ServerChunkCache\.getChunk|^Level\.getChunk$|^ServerChunkCache\.getChunkNow$|^ChunkSource\.getChunk"),
    ("Create (com.simibubi)", r"^@create$"),
]

FRAME = re.compile(r"^\s+([\w$.]+)\(")


def short(full):
    parts = full.split(".")
    # Class.method (inner classes keep their $)
    return ".".join(parts[-2:]) if len(parts) >= 2 else full


def samples(path):
    p = subprocess.Popen(["jfr", "print", "--events", "jdk.ExecutionSample", "--stack-depth", "256", path],
                         stdout=subprocess.PIPE, text=True, errors="replace")
    thread, stack, in_stack = None, [], False
    for line in p.stdout:
        s = line.strip()
        if s.startswith("jdk.ExecutionSample"):
            thread, stack, in_stack = None, [], False
        elif s.startswith("sampledThread"):
            m = re.search(r'"([^"]*)"', s)
            thread = m.group(1) if m else None
        elif s.startswith("stackTrace = ["):
            in_stack = True
        elif in_stack and s == "]":
            in_stack = False
        elif in_stack:
            m = FRAME.match(line)
            if m:
                stack.append(m.group(1))
        elif s == "}":
            if thread == "Server thread" and stack:
                yield stack
            thread, stack = None, []
    p.wait()


def main(rec, out):
    n = 0
    self_c, incl = Counter(), Counter()
    phase_c = Counter()
    pats = [(name, re.compile(rx)) for name, rx in PHASES]
    for stack in samples(rec):
        names = [short(f) for f in stack]
        if not any(x == "MinecraftServer.tickServer" for x in names):
            continue
        n += 1
        self_c[names[0]] += 1
        seen = set(names)
        for x in seen:
            incl[x] += 1
        create = any(f.startswith("com.simibubi.") for f in stack)
        for name, rx in pats:
            if (rx.pattern == "^@create$" and create) or any(rx.search(x) for x in seen):
                phase_c[name] += 1
    res = {
        "samples": n,
        "phases": [{"phase": name, "share": round(phase_c[name] / n, 4) if n else 0} for name, _ in PHASES],
        "self": [{"method": k, "share": round(v / n, 4)} for k, v in self_c.most_common(60)] if n else [],
        "inclusive": [{"method": k, "share": round(v / n, 4)} for k, v in incl.most_common(120)] if n else [],
    }
    with open(out, "w") as f:
        json.dump(res, f, indent=1)
    print("%s: %d server-thread samples while ticking" % (rec, n))
    return 0 if n else 1


if __name__ == "__main__":
    if len(sys.argv) != 3:
        print(__doc__)
        sys.exit(2)
    sys.exit(main(sys.argv[1], sys.argv[2]))
