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
| dimensions/minecraft/overworld/region | 1764 | 27 | 0 | 0 |

Keys compared: blocks, block entities, entity types. Noise examples in core-run-noise-b.json and -noise-c.json.

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
| vanilla-a | yes | 3 | 1.666 | 9.682 | 1 | 150.929 | 151.357 | 8785 | 472 |
| vanilla-b | yes | 16 | 1.659 | 7.638 | 1 | 149.247 | 149.77 | 7747 | 333 |
| vanilla-c | yes | 2 | 1.714 | 8.319 | 1 | 151.153 | 151.722 | 9566 | 528 |
| quicksilver | yes | 3 | 1.769 | 8.267 | 1 | 152.919 | 153.304 | 9506 | 480 |

Quicksilver server bench: `{"side": "dedicated", "ticks": 1003, "tps": 20.06, "msptAvg": 1.915, "msptP50": 1.378, "msptP99": 9.147, "msptMax": 79.708, "ticksOver50ms": 1, "heapUsedMB": 227.66, "heapUsedAfterGcMB": 154.295, "startupMs": 10985, "worldLoadMs": 1957, "network": {"counting": true, "serverBytesInWindow": 0, "serverBytesIn": 0, "serverBytesOut": 0, "clientBytesIn": 0, "clientBytesOut": 0}, "players": 0}`

## siftec

### Worldgen (byte for byte)

| dimension | chunks | vanilla noise (a vs b, a vs c) | quicksilver vs vanilla-a | quicksilver where vanilla agrees |
|---|---|---|---|---|
| dimensions/minecraft/overworld/region | 144 | 0 | 0 | 0 |
| dimensions/minecraft/the_nether/region | 144 | 0 | 0 | 0 |
| dimensions/the_sift/the_sift/region | 144 | 0 | 0 | 0 |

Keys compared: chunks. Noise examples in siftec-genbytes-noise-b.json and -noise-c.json.

### Worldgen (block by block)

| dimension | chunks | vanilla noise (a vs b, a vs c) | quicksilver vs vanilla-a | quicksilver where vanilla agrees |
|---|---|---|---|---|
| dimensions/minecraft/overworld/region | 144 | 0 | 0 | 0 |
| dimensions/minecraft/the_nether/region | 144 | 0 | 0 | 0 |
| dimensions/the_sift/the_sift/region | 144 | 0 | 0 | 0 |

Keys compared: blocks, block entities, scheduled ticks, entities. Noise examples in siftec-gen-noise-b.json and -noise-c.json.

### World diff after the scripted run

| dimension | chunks | vanilla noise (a vs b, a vs c) | quicksilver vs vanilla-a | quicksilver where vanilla agrees |
|---|---|---|---|---|
| dimensions/minecraft/overworld/region | 1764 | 95 | 0 | 0 |

Keys compared: blocks, block entities, entity types. Noise examples in siftec-run-noise-b.json and -noise-c.json.

### Scripted run (relative only)

| run | finished | load wait (ticks) | mspt avg | mspt p99 | ticks over 50 ms | heap after load MB | heap end MB | start-up ms | world load ms |
|---|---|---|---|---|---|---|---|---|---|
| vanilla-a | yes | 8 | 2.027 | 11.524 | 2 | 279.321 | 280.315 | 18666 | 547 |
| vanilla-b | yes | 5 | 1.787 | 7.669 | 1 | 279.302 | 280.166 | 15123 | 402 |
| vanilla-c | yes | 6 | 2.129 | 10.532 | 1 | 279.403 | 280.298 | 19055 | 524 |
| quicksilver | yes | 6 | 1.831 | 10.771 | 1 | 281.478 | 282.145 | 18219 | 506 |

Quicksilver server bench: `{"side": "dedicated", "ticks": 1005, "tps": 20.1, "msptAvg": 2.048, "msptP50": 1.371, "msptP99": 11.207, "msptMax": 183.309, "ticksOver50ms": 1, "heapUsedMB": 540.575, "heapUsedAfterGcMB": 283.154, "startupMs": 20644, "worldLoadMs": 2929, "network": {"counting": true, "serverBytesInWindow": 0, "serverBytesIn": 0, "serverBytesOut": 0, "clientBytesIn": 0, "clientBytesOut": 0}, "players": 0}`

### Create train

| run | result |
|---|---|
| vanilla-a | arrivals=11 B@136,A@235,B@335,A@434,B@534,A@633,B@733,A@832,B@932,A@1031,B@1131 speed=0.31499999295920134 derailed=false entry=1 state=IN_TRANSIT |
| vanilla-b | arrivals=11 B@136,A@235,B@335,A@434,B@534,A@633,B@733,A@832,B@932,A@1031,B@1131 speed=0.31499999295920134 derailed=false entry=1 state=IN_TRANSIT |
| vanilla-c | arrivals=11 B@136,A@235,B@335,A@434,B@534,A@633,B@733,A@832,B@932,A@1031,B@1131 speed=0.31499999295920134 derailed=false entry=1 state=IN_TRANSIT |
| quicksilver | arrivals=11 B@136,A@235,B@335,A@434,B@534,A@633,B@733,A@832,B@932,A@1031,B@1131 speed=0.31499999295920134 derailed=false entry=1 state=IN_TRANSIT |

### SIFTEC self-tests (passed/failed)

| suite | vanilla-a | vanilla-b | vanilla-c | quicksilver |
|---|---|---|---|---|
| always | 15/0 | 15/0 | 15/0 | 15/0 |
| always edges | 20/0 | 20/0 | 20/0 | 20/0 |
| buildingfix | 13/0 | 13/0 | 13/0 | 13/0 |
| buildings | 26/0 | 26/0 | 26/0 | 26/0 |
| buildings_a | 55/0 | 55/0 | 55/0 | 55/0 |
| buildings_b | 78/0 | 78/0 | 78/0 | 78/0 |
| datafix | 20/0 | 20/0 | 20/0 | 20/0 |
| designer | 41/0 | 41/0 | 41/0 | 41/0 |
| exploits | 40/0 | 40/0 | 40/0 | 40/0 |
| exploration | 34/0 | 34/0 | 34/0 | 34/0 |
| fixes | 21/0 | 21/0 | 21/0 | 21/0 |
| food | 31/0 | 31/0 | 31/0 | 31/0 |
| guide | 4/0 | 4/0 | 4/0 | 4/0 |
| logistics | 44/0 | 44/0 | 44/0 | 44/0 |
| machines | 60/0 | 60/0 | 60/0 | 60/0 |
| map | 12/0 | 12/0 | 12/0 | 12/0 |
| ownership | 15/0 | 15/0 | 15/0 | 15/0 |
| polish | 39/0 | 39/0 | 39/0 | 39/0 |
| powerfix | 10/0 | 10/0 | 10/0 | 10/0 |
| progression | 16/0 | 16/0 | 16/0 | 16/0 |
| quality | 8/0 | 8/0 | 8/0 | 8/0 |
| state gun | 22/0 | 22/0 | 22/0 | 22/0 |
| style | 85/0 | 85/0 | 85/0 | 85/0 |
| vehicles | 19/0 | 19/0 | 19/0 | 19/0 |
| world break | 9/0 | 9/0 | 9/0 | 9/0 |
| world clump | 5/0 | 5/0 | 5/0 | 5/0 |
| world deployer | 2/0 | 2/0 | 2/0 | 2/0 |
| world drops | 21/0 | 21/0 | 21/0 | 21/0 |
| world pads | 15/0 | 15/0 | 15/0 | 15/0 |
| world regrow | 10/0 | 10/0 | 10/0 | 10/0 |
| world restart setup | 1/0 | 1/0 | 1/0 | 1/0 |

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


## Network check

| run | client joined | run finished | client left early | protocol problems | server bytes in/out |
|---|---|---|---|---|---|
| net-vanilla-client | True | True | False | 0 | 6436/2484243 |
| net-qs-client | True | True | False | 0 | -/- |

## Result

**PASS**
