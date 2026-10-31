# Server suite (phase 2)

Mode: full (suites expected: core, siftec, terralith; quick mode runs core only)

## core

### Worldgen: settling before the save

SIFTEC's pieces (ore nodes, collectibles) are placed by the harness before the first tick, while every generated chunk is loaded; the save waits for the light engine and SIFTEC's own queue. Where the harness used to stop (light quiet for 20 ticks) is shown for the record.

| run | chunks given SIFTEC pieces | light quiet at tick | then: SIFTEC queue / generated chunks unloaded | saved at tick | at the save: queue / unloaded |
|---|---|---|---|---|---|
| vanilla-a | 0 | 20 | 0 / 0 | 40 | 0 / 0 |
| vanilla-b | 0 | 20 | 0 / 0 | 40 | 0 / 0 |
| vanilla-c | 0 | 20 | 0 / 0 | 40 | 0 / 0 |
| quicksilver | 0 | 20 | 0 / 0 | 40 | 0 / 0 |

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
| vanilla-a | yes | 3 | 1.567 | 12.084 | 2 | 149.606 | 150.232 | 12340 | 549 |
| vanilla-b | yes | 3 | 1.466 | 12.127 | 2 | 151.117 | 151.607 | 12109 | 561 |
| vanilla-c | yes | 3 | 1.542 | 11.191 | 2 | 150.991 | 151.474 | 12276 | 575 |
| quicksilver | yes | 2 | 1.473 | 6.713 | 1 | 139.652 | 141.694 | 8147 | 401 |

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
| vanilla-a | 149.374 | 150.176 | yes |
| vanilla-b | 149.632 | 150.477 | yes |
| vanilla-c | 149.53 | 150.475 | yes |
| quicksilver | 137.566 | 138.408 | yes |
Heap after load: Quicksilver 137.6 MiB, vanilla 149.4 to 149.6 MiB (11.8 MiB, 8% below the lowest)
Heap after ten minutes: Quicksilver 138.4 MiB, vanilla 150.2 to 150.5 MiB (11.8 MiB, 8% below the lowest)

Where it goes: core-heap.md.

Quicksilver server bench: `{"side": "dedicated", "ticks": 1003, "tps": 20.06, "msptAvg": 1.574, "msptP50": 1.204, "msptP99": 7.45, "msptMax": 57.7, "ticksOver50ms": 1, "heapUsedMB": 205.751, "heapUsedAfterGcMB": 142.789, "startupMs": 9549, "worldLoadMs": 1802, "network": {"counting": true, "serverBytesInWindow": 0, "serverBytesIn": 0, "serverBytesOut": 0, "clientBytesIn": 0, "clientBytesOut": 0}, "players": 0}`

## siftec

### Worldgen: settling before the save

SIFTEC's pieces (ore nodes, collectibles) are placed by the harness before the first tick, while every generated chunk is loaded; the save waits for the light engine and SIFTEC's own queue. Where the harness used to stop (light quiet for 20 ticks) is shown for the record.

| run | chunks given SIFTEC pieces | light quiet at tick | then: SIFTEC queue / generated chunks unloaded | saved at tick | at the save: queue / unloaded |
|---|---|---|---|---|---|
| vanilla-a | None | None | None / None | None | None / None |
| vanilla-b | None | None | None / None | None | None / None |
| vanilla-c | None | None | None / None | None | None / None |
| quicksilver | None | None | None / None | None | None / None |

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

### Worldgen: settling before the save

SIFTEC's pieces (ore nodes, collectibles) are placed by the harness before the first tick, while every generated chunk is loaded; the save waits for the light engine and SIFTEC's own queue. Where the harness used to stop (light quiet for 20 ticks) is shown for the record.

| run | chunks given SIFTEC pieces | light quiet at tick | then: SIFTEC queue / generated chunks unloaded | saved at tick | at the save: queue / unloaded |
|---|---|---|---|---|---|
| vanilla-a | 0 | 20 | 0 / 0 | 40 | 0 / 0 |
| vanilla-b | 0 | 20 | 0 / 0 | 40 | 0 / 0 |
| vanilla-c | 0 | 20 | 0 / 0 | 40 | 0 / 0 |
| quicksilver | 0 | 20 | 0 / 0 | 40 | 0 / 0 |

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
| vanilla-a | 262.039 | 262.615 | yes |
| vanilla-b | 262.754 | 263.355 | yes |
| vanilla-c | 261.751 | 262.644 | yes |
| quicksilver | 179.41 | 179.952 | yes |
Heap after load: Quicksilver 179.4 MiB, vanilla 261.8 to 262.8 MiB (82.3 MiB, 31% below the lowest)
Heap after ten minutes: Quicksilver 180.0 MiB, vanilla 262.6 to 263.4 MiB (82.7 MiB, 31% below the lowest)

Where it goes: terralith-heap.md.

## Network check

| run | client joined | run finished | client left early | protocol problems | server bytes in/out |
|---|---|---|---|---|---|
| net-vanilla-client | True | True | False | 0 | 6379/2551199 |
| net-qs-client | True | True | False | 0 | -/- |

## SIFTEC: report only

Until SIFTEC starts with MapMod's latest build again, SIFTEC's suites fail nothing (docs/progress.md, open items). Core and Terralith gate as usual.

**SIFTEC did not start** in 5 of 5 run(s):

- siftec-base: `the pack: pack changed: siftec-26.3.jar (sha256 ef811d34e0893e801979540cc61598f5976522324715e83fbb25dab01e57210c, locked 20910aec59b1f5e0255cc5c523acca0afc4e163d2e6ee13e5; pack changed: siftec-selftest-26.3.jar (sha256 54d07dc0897a76ebc93914ce82d4c10bda1de8924749e39dca7fcf0cfd6c5c03, locked 0e08ce6e5e97b4e195df04c30b4d1929806f5a75; could not assemble SIFTEC's pack`
- siftec-quicksilver: `the pack: pack changed: siftec-26.3.jar (sha256 ef811d34e0893e801979540cc61598f5976522324715e83fbb25dab01e57210c, locked 20910aec59b1f5e0255cc5c523acca0afc4e163d2e6ee13e5; pack changed: siftec-selftest-26.3.jar (sha256 54d07dc0897a76ebc93914ce82d4c10bda1de8924749e39dca7fcf0cfd6c5c03, locked 0e08ce6e5e97b4e195df04c30b4d1929806f5a75; could not assemble SIFTEC's pack`
- siftec-vanilla-a: `the pack: pack changed: siftec-26.3.jar (sha256 ef811d34e0893e801979540cc61598f5976522324715e83fbb25dab01e57210c, locked 20910aec59b1f5e0255cc5c523acca0afc4e163d2e6ee13e5; pack changed: siftec-selftest-26.3.jar (sha256 54d07dc0897a76ebc93914ce82d4c10bda1de8924749e39dca7fcf0cfd6c5c03, locked 0e08ce6e5e97b4e195df04c30b4d1929806f5a75; could not assemble SIFTEC's pack`
- siftec-vanilla-b: `the pack: pack changed: siftec-26.3.jar (sha256 ef811d34e0893e801979540cc61598f5976522324715e83fbb25dab01e57210c, locked 20910aec59b1f5e0255cc5c523acca0afc4e163d2e6ee13e5; pack changed: siftec-selftest-26.3.jar (sha256 54d07dc0897a76ebc93914ce82d4c10bda1de8924749e39dca7fcf0cfd6c5c03, locked 0e08ce6e5e97b4e195df04c30b4d1929806f5a75; could not assemble SIFTEC's pack`
- siftec-vanilla-c: `the pack: pack changed: siftec-26.3.jar (sha256 ef811d34e0893e801979540cc61598f5976522324715e83fbb25dab01e57210c, locked 20910aec59b1f5e0255cc5c523acca0afc4e163d2e6ee13e5; pack changed: siftec-selftest-26.3.jar (sha256 54d07dc0897a76ebc93914ce82d4c10bda1de8924749e39dca7fcf0cfd6c5c03, locked 0e08ce6e5e97b4e195df04c30b4d1929806f5a75; could not assemble SIFTEC's pack`

SIFTEC failures (not gating):

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

## Result

**PASS**
