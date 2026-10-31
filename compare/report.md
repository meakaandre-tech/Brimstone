# Server suite (phase 2)

Mode: full (suites expected: core, siftec, terralith; quick mode runs core only)

## core

### Worldgen (byte for byte)

| dimension | chunks | vanilla noise (a vs b, a vs c) | quicksilver vs vanilla-a | quicksilver where vanilla agrees |
|---|---|---|---|---|
| dimensions/minecraft/overworld/region | 144 | 0 | 0 | 0 |
| dimensions/minecraft/the_end/region | 144 | 0 | 0 | 0 |
| dimensions/minecraft/the_nether/region | 144 | 0 | 0 | 0 |

Keys compared: chunks. Noise examples in core-genbytes-noise-b.json and -noise-c.json.

### Worldgen (block by block)

| dimension | chunks | vanilla noise (a vs b, a vs c) | quicksilver vs vanilla-a | quicksilver where vanilla agrees |
|---|---|---|---|---|
| dimensions/minecraft/overworld/region | 144 | 0 | 0 | 0 |
| dimensions/minecraft/the_end/region | 144 | 0 | 0 | 0 |
| dimensions/minecraft/the_nether/region | 144 | 0 | 0 | 0 |

Keys compared: blocks, block entities, scheduled ticks, entities. Noise examples in core-gen-noise-b.json and -noise-c.json.

### World diff after the scripted run

| dimension | chunks | vanilla noise (a vs b, a vs c) | quicksilver vs vanilla-a | quicksilver where vanilla agrees |
|---|---|---|---|---|
| dimensions/minecraft/overworld/region | 1764 | 0 | 0 | 0 |

Keys compared: blocks, block entities, entity types. Noise examples in core-run-noise-b.json and -noise-c.json.

### World diff after the random-tick run

| dimension | chunks | vanilla noise (a vs b, a vs c) | quicksilver vs vanilla-a | quicksilver where vanilla agrees |
|---|---|---|---|---|
| dimensions/minecraft/overworld/region | 1764 | 0 | 0 | 0 |

Keys compared: blocks, block entities, entity types. Noise examples in core-random-noise-b.json and -noise-c.json.

### GameTests (tick finished, result)

| test | vanilla-a | vanilla-b | vanilla-c | quicksilver |
|---|---|---|---|---|
| hopper_chain | 542: moved=64 | 542: moved=64 | 542: moved=64 | 542: moved=64 |
| redstone_clock | 100: lampOn=1 observerPulses=17 | 100: lampOn=1 observerPulses=17 | 100: lampOn=1 observerPulses=17 | 100: lampOn=1 observerPulses=17 |
| piston_machine | 200: extensions=10 line=stone,_,cobblestone,oak_planks, | 200: extensions=10 line=stone,_,cobblestone,oak_planks, | 200: extensions=10 line=stone,_,cobblestone,oak_planks, | 200: extensions=10 line=stone,_,cobblestone,oak_planks, |
| piston_door | 63: closedAt=13 | 63: closedAt=13 | 63: closedAt=13 | 63: closedAt=13 |
| item_sorter | 436: sorted=19 passed=20 strays=0 filter=18 lock=1 line=0 input=0 torch=true | 436: sorted=19 passed=20 strays=0 filter=18 lock=1 line=0 input=0 torch=true | 436: sorted=19 passed=20 strays=0 filter=18 lock=1 line=0 input=0 torch=true | 436: sorted=19 passed=20 strays=0 filter=18 lock=1 line=0 input=0 torch=true |
| crop_farm | 200: grown=6 dispensersEmpty=false | 200: grown=6 dispensersEmpty=false | 200: grown=6 dispensersEmpty=false | 200: grown=6 dispensersEmpty=false |
| mob_farm | 141: drops=14 beef=9 leather=5 | 141: drops=14 beef=9 leather=5 | 141: drops=14 beef=9 leather=5 | 141: drops=14 beef=9 leather=5 |
| villager_trade | 46: offers=2 first=minecraft:bookshelf xp=1 | 46: offers=2 first=minecraft:bookshelf xp=1 | 46: offers=2 first=minecraft:bookshelf xp=1 | 46: offers=2 first=minecraft:bookshelf xp=1 |

### Scripted run (relative only)

| run | finished | load wait (ticks) | mspt avg | mspt p99 | ticks over 50 ms | heap after load MB | heap end MB | start-up ms | world load ms |
|---|---|---|---|---|---|---|---|---|---|
| vanilla-a | yes | 4 | 1.581 | 7.441 | 1 | 151.254 | 151.807 | 9585 | 457 |
| vanilla-b | yes | 2 | 1.323 | 6.567 | 1 | 151.164 | 151.642 | 9159 | 447 |
| vanilla-c | yes | 3 | 1.736 | 7.959 | 1 | 151.007 | 151.535 | 9765 | 544 |
| quicksilver | yes | 4 | 1.919 | 12.365 | 2 | 140.512 | 142.287 | 12454 | 532 |

### Random-tick run

| run | finished | random ticks, pins and spawns |
|---|---|---|
| vanilla-a | yes | positions=122035 ticks=892 sectionsOn=4501 sectionsOff=259054 precipOn=2021 precipOff=29994 monsters=4 creatures=0 |
| vanilla-b | yes | positions=122035 ticks=892 sectionsOn=4501 sectionsOff=259054 precipOn=2021 precipOff=29994 monsters=4 creatures=0 |
| vanilla-c | yes | positions=122035 ticks=892 sectionsOn=4501 sectionsOff=259054 precipOn=2021 precipOff=29994 monsters=4 creatures=0 |
| quicksilver | yes | positions=122035 ticks=892 sectionsOn=4501 sectionsOff=259054 precipOn=2021 precipOff=29994 monsters=4 creatures=0 |

### Heap run (live heap after a full GC, MiB; ten minutes of game time)

| run | after load | after 10 min | finished |
|---|---|---|---|
| vanilla-a | 149.573 | 150.345 | yes |
| vanilla-b | 149.447 | 150.303 | yes |
| vanilla-c | 149.467 | 150.341 | yes |
| quicksilver | 138.05 | 138.888 | yes |
Heap after load: Quicksilver 138.1 MiB, vanilla 149.4 to 149.6 MiB (11.4 MiB, 8% below the lowest)
Heap after ten minutes: Quicksilver 138.9 MiB, vanilla 150.3 to 150.3 MiB (11.4 MiB, 8% below the lowest)

Where it goes: core-heap.md.

Quicksilver server bench: `{"side": "dedicated", "ticks": 1004, "tps": 20.08, "msptAvg": 2.042, "msptP50": 1.433, "msptP99": 12.687, "msptMax": 110.6, "ticksOver50ms": 2, "heapUsedMB": 263.633, "heapUsedAfterGcMB": 143.278, "startupMs": 14762, "worldLoadMs": 2838, "network": {"counting": true, "serverBytesInWindow": 0, "serverBytesIn": 0, "serverBytesOut": 0, "clientBytesIn": 0, "clientBytesOut": 0}, "players": 0}`

## siftec

### Worldgen (byte for byte)

| dimension | chunks | vanilla noise (a vs b, a vs c) | quicksilver vs vanilla-a | quicksilver where vanilla agrees |
|---|---|---|---|---|
| missing | | | | |

### Worldgen (block by block)

| dimension | chunks | vanilla noise (a vs b, a vs c) | quicksilver vs vanilla-a | quicksilver where vanilla agrees |
|---|---|---|---|---|
| missing | | | | |

### World diff after the scripted run

| dimension | chunks | vanilla noise (a vs b, a vs c) | quicksilver vs vanilla-a | quicksilver where vanilla agrees |
|---|---|---|---|---|
| missing | | | | |

### Scripted run (relative only)

| run | finished | load wait (ticks) | mspt avg | mspt p99 | ticks over 50 ms | heap after load MB | heap end MB | start-up ms | world load ms |
|---|---|---|---|---|---|---|---|---|---|
| vanilla-a | no | None | None | None | None | None | None | None | None |
| vanilla-b | no | None | None | None | None | None | None | None | None |
| vanilla-c | no | None | None | None | None | None | None | None | None |
| quicksilver | no | None | None | None | None | None | None | None | None |

### Heap run (live heap after a full GC, MiB; ten minutes of game time)

| run | after load | after 10 min | finished |
|---|---|---|---|
| vanilla-a | None | None | no (None) |
| vanilla-b | None | None | no (None) |
| vanilla-c | None | None | no (None) |
| quicksilver | None | None | no (None) |

Where it goes: siftec-heap.md.

### Create train

| run | result |
|---|---|
| vanilla-a | missing |
| vanilla-b | missing |
| vanilla-c | missing |
| quicksilver | missing |

### SIFTEC self-tests (passed/failed)

| suite | vanilla-a | vanilla-b | vanilla-c | quicksilver |
|---|---|---|---|---|

## terralith

### Worldgen (byte for byte)

| dimension | chunks | vanilla noise (a vs b, a vs c) | quicksilver vs vanilla-a | quicksilver where vanilla agrees |
|---|---|---|---|---|
| dimensions/minecraft/overworld/region | 144 | 0 | 0 | 0 |

Keys compared: chunks. Noise examples in terralith-genbytes-noise-b.json and -noise-c.json.

### Worldgen (block by block)

| dimension | chunks | vanilla noise (a vs b, a vs c) | quicksilver vs vanilla-a | quicksilver where vanilla agrees |
|---|---|---|---|---|
| dimensions/minecraft/overworld/region | 144 | 0 | 0 | 0 |

Keys compared: blocks, block entities, scheduled ticks, entities. Noise examples in terralith-gen-noise-b.json and -noise-c.json.


### Heap run (live heap after a full GC, MiB; ten minutes of game time)

| run | after load | after 10 min | finished |
|---|---|---|---|
| vanilla-a | 262.261 | 262.788 | yes |
| vanilla-b | 262.237 | 262.884 | yes |
| vanilla-c | 262.307 | 263.043 | yes |
| quicksilver | 178.902 | 180.636 | yes |
Heap after load: Quicksilver 178.9 MiB, vanilla 262.2 to 262.3 MiB (83.3 MiB, 32% below the lowest)
Heap after ten minutes: Quicksilver 180.6 MiB, vanilla 262.8 to 263.0 MiB (82.2 MiB, 31% below the lowest)

Where it goes: terralith-heap.md.

## Network check

| run | client joined | run finished | client left early | protocol problems | server bytes in/out |
|---|---|---|---|---|---|
| net-vanilla-client | True | True | False | 0 | 6410/2476168 |
| net-qs-client | True | True | False | 0 | -/- |

## Result

**FAIL**
- siftec worldgen vanilla-a: the harness did not finish (no result)
- siftec worldgen vanilla-a: no CI HARNESS GEN DONE in its log
- siftec worldgen vanilla-b: the harness did not finish (no result)
- siftec worldgen vanilla-b: no CI HARNESS GEN DONE in its log
- siftec worldgen vanilla-c: the harness did not finish (no result)
- siftec worldgen vanilla-c: no CI HARNESS GEN DONE in its log
- siftec worldgen quicksilver: the harness did not finish (no result)
- siftec worldgen quicksilver: no CI HARNESS GEN DONE in its log
- siftec worldgen (byte for byte): a world is missing or the comparison failed
- siftec worldgen (block by block): a world is missing or the comparison failed
- siftec world diff after the scripted run: a world is missing or the comparison failed
- siftec scripted run vanilla-a: did not finish (no result)
- siftec scripted run vanilla-b: did not finish (no result)
- siftec scripted run vanilla-c: did not finish (no result)
- siftec scripted run quicksilver: did not finish (no result)
- siftec: Quicksilver not in the quicksilver run's mod list
- siftec heap run vanilla-a: no finished heap run (no result)
- siftec heap run vanilla-b: no finished heap run (no result)
- siftec heap run vanilla-c: no finished heap run (no result)
- siftec heap run quicksilver: no finished heap run (no result)
- siftec heap run: no live heap after load in every run
- siftec heap run: no live heap after ten minutes in every run
- siftec: no server bench report in the quicksilver run
- siftec train: no result in vanilla-a
- siftec train: no result in vanilla-b
- siftec train: no result in vanilla-c
- siftec train: no result in quicksilver
- siftec self-tests: no results in vanilla-a
- siftec self-tests: no results in vanilla-b
- siftec self-tests: no results in vanilla-c
- siftec self-tests: no results in quicksilver
