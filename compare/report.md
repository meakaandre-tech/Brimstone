# Server suite (phase 2)

Mode: quick (suites expected: core; quick mode runs core only)

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
| vanilla-a | yes | 3 | 1.934 | 12.111 | 2 | 151.057 | 151.591 | 12357 | 510 |
| vanilla-b | yes | 4 | 1.504 | 11.72 | 2 | 151.114 | 151.741 | 12252 | 567 |
| vanilla-c | yes | 3 | 1.711 | 7.191 | 1 | 151.255 | 151.512 | 8173 | 366 |
| quicksilver | yes | 7 | 1.663 | 8.074 | 1 | 139.217 | 141.372 | 9278 | 461 |

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
| vanilla-a | 149.609 | 150.465 | yes |
| vanilla-b | 149.274 | 150.131 | yes |
| vanilla-c | 148.07 | 148.985 | yes |
| quicksilver | 137.74 | 138.645 | yes |
Heap after load: Quicksilver 137.7 MiB, vanilla 148.1 to 149.6 MiB (10.3 MiB, 7% below the lowest)
Heap after ten minutes: Quicksilver 138.6 MiB, vanilla 149.0 to 150.5 MiB (10.3 MiB, 7% below the lowest)

Where it goes: core-heap.md.

Quicksilver server bench: `{"side": "dedicated", "ticks": 1003, "tps": 20.06, "msptAvg": 1.801, "msptP50": 1.286, "msptP99": 8.423, "msptMax": 78.139, "ticksOver50ms": 1, "heapUsedMB": 207.767, "heapUsedAfterGcMB": 142.367, "startupMs": 10956, "worldLoadMs": 2138, "network": {"counting": true, "serverBytesInWindow": 0, "serverBytesIn": 0, "serverBytesOut": 0, "clientBytesIn": 0, "clientBytesOut": 0}, "players": 0}`

## Network check

| run | client joined | run finished | client left early | protocol problems | server bytes in/out |
|---|---|---|---|---|---|
| net-vanilla-client | True | True | False | 0 | 6366/2510878 |
| net-qs-client | True | True | False | 0 | -/- |

## SIFTEC: report only

Until SIFTEC starts with MapMod's latest build again, SIFTEC's suites fail nothing (docs/progress.md, open items). Core and Terralith gate as usual.

SIFTEC started in every run.

No SIFTEC failures.


## Result

**PASS**
