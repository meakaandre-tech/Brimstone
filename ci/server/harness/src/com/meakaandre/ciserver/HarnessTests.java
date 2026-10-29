package com.meakaandre.ciserver;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;

/**
 * CI only (phase 2, P2-0): GameTests for what players build. Each test builds its machine in an empty structure,
 * counts ticks itself, and when its machine has done its job logs one line,
 * {@code CI GAMETEST <name> tick=<tick it finished> result=<what it measured>}, which ci/server/compare-server.py
 * compares between vanilla's runs and Quicksilver's: the same result in the same tick.
 */
public final class HarnessTests {
    private static final String BOX8 = "ci_server_harness:box8";
    private static final String BOX16 = "ci_server_harness:box16";
    private static final String TALL = "ci_server_harness:tall";

    /** Counts ticks from the test's start and finishes it once, the first tick its check holds. */
    private static final class Run {
        final GameTestHelper h;
        final String name;
        int tick = 0;
        boolean done = false;

        Run(GameTestHelper h, String name) {
            this.h = h;
            this.name = name;
        }

        void finish(String result) {
            if (done) return;
            done = true;
            Harness.LOG.info("CI GAMETEST {} tick={} result={}", name, tick, result);
            h.succeed();
        }
    }

    private interface Step {
        void tick(Run r);
    }

    private static void each(GameTestHelper h, String name, Step step) {
        Run r = new Run(h, name);
        h.onEachTick(() -> {
            if (r.done) return;
            r.tick++;
            step.tick(r);
        });
    }

    // ------------------------------------------------------------------ helpers

    private static BlockState hopper(Direction d) {
        return Blocks.HOPPER.defaultBlockState().setValue(BlockStateProperties.FACING_HOPPER, d);
    }

    private static BlockState facing(Block b, Direction d) {
        return b.defaultBlockState().setValue(BlockStateProperties.FACING, d);
    }

    private static BlockState horizontal(Block b, Direction d) {
        return b.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, d);
    }

    private static void fill(GameTestHelper h, int x1, int y1, int z1, int x2, int y2, int z2, BlockState s) {
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                for (int z = z1; z <= z2; z++) h.setBlock(new BlockPos(x, y, z), s);
            }
        }
    }

    private static Container container(GameTestHelper h, int x, int y, int z) {
        return (Container) h.getLevel().getBlockEntity(h.absolutePos(new BlockPos(x, y, z)));
    }

    private static int count(Container c, Item item) {
        int n = 0;
        for (int i = 0; i < c.getContainerSize(); i++) {
            ItemStack s = c.getItem(i);
            if (item == null || s.is(item)) n += s.getCount();
        }
        return n;
    }

    private static BlockState at(GameTestHelper h, int x, int y, int z) {
        return h.getBlockState(new BlockPos(x, y, z));
    }

    // ------------------------------------------------------------------ tests

    /** 64 items from a chest through five hoppers into a chest. */
    @GameTest(structure = BOX8, maxTicks = 1200)
    public void hopperChain(GameTestHelper h) {
        h.setBlock(new BlockPos(0, 2, 0), Blocks.CHEST.defaultBlockState());
        container(h, 0, 2, 0).setItem(0, new ItemStack(Items.COBBLESTONE, 64));
        for (int x = 0; x <= 4; x++) h.setBlock(new BlockPos(x, 1, 0), hopper(Direction.EAST));
        h.setBlock(new BlockPos(5, 1, 0), Blocks.CHEST.defaultBlockState());
        each(h, "hopper_chain", r -> {
            int n = count(container(h, 5, 1, 0), Items.COBBLESTONE);
            if (n == 64) r.finish("moved=" + n);
        });
    }

    /** Two observers watching each other, lighting a lamp: pulses counted over 100 ticks. */
    @GameTest(structure = BOX8, maxTicks = 200)
    public void redstoneClock(GameTestHelper h) {
        h.setBlock(new BlockPos(1, 1, 2), Blocks.REDSTONE_LAMP.defaultBlockState());
        h.setBlock(new BlockPos(2, 1, 2), facing(Blocks.OBSERVER, Direction.EAST));
        h.setBlock(new BlockPos(3, 1, 2), facing(Blocks.OBSERVER, Direction.WEST));
        int[] lamp = {0}, obs = {0};
        boolean[] was = {false, false};
        each(h, "redstone_clock", r -> {
            boolean lit = at(h, 1, 1, 2).getValue(BlockStateProperties.LIT);
            boolean pow = at(h, 2, 1, 2).getValue(BlockStateProperties.POWERED);
            if (lit && !was[0]) lamp[0]++;
            if (pow && !was[1]) obs[0]++;
            was[0] = lit;
            was[1] = pow;
            if (r.tick == 100) r.finish("lampOn=" + lamp[0] + " observerPulses=" + obs[0]);
        });
    }

    /** A sticky piston pushing and pulling a line of three blocks, powered every 20 ticks. */
    @GameTest(structure = BOX8, maxTicks = 300)
    public void pistonMachine(GameTestHelper h) {
        h.setBlock(new BlockPos(1, 1, 3), facing(Blocks.STICKY_PISTON, Direction.EAST));
        h.setBlock(new BlockPos(2, 1, 3), Blocks.STONE.defaultBlockState());
        h.setBlock(new BlockPos(3, 1, 3), Blocks.COBBLESTONE.defaultBlockState());
        h.setBlock(new BlockPos(4, 1, 3), Blocks.OAK_PLANKS.defaultBlockState());
        int[] ext = {0};
        boolean[] was = {false};
        each(h, "piston_machine", r -> {
            if (r.tick % 20 == 1) h.setBlock(new BlockPos(0, 1, 3), Blocks.REDSTONE_BLOCK.defaultBlockState());
            if (r.tick % 20 == 11) h.setBlock(new BlockPos(0, 1, 3), Blocks.AIR.defaultBlockState());
            BlockState base = at(h, 1, 1, 3);
            // while it moves, the piston's block is a moving piston, which has no "extended"
            boolean e = base.hasProperty(BlockStateProperties.EXTENDED) && base.getValue(BlockStateProperties.EXTENDED);
            if (e && !was[0]) ext[0]++;
            was[0] = e;
            if (r.tick == 200) {
                StringBuilder line = new StringBuilder();
                for (int x = 2; x <= 5; x++) line.append(at(h, x, 1, 3).getBlock() == Blocks.AIR ? "_" : at(h, x, 1, 3).getBlock().getDescriptionId().replaceAll(".*\\.", "")).append(',');
                r.finish("extensions=" + ext[0] + " line=" + line);
            }
        });
    }

    /** A two-block piston door: closed by power at tick 10, opened again at tick 60. */
    @GameTest(structure = BOX8, maxTicks = 200)
    public void pistonDoor(GameTestHelper h) {
        h.setBlock(new BlockPos(0, 1, 2), facing(Blocks.STICKY_PISTON, Direction.EAST));
        h.setBlock(new BlockPos(1, 1, 2), Blocks.STONE.defaultBlockState());
        h.setBlock(new BlockPos(5, 1, 2), facing(Blocks.STICKY_PISTON, Direction.WEST));
        h.setBlock(new BlockPos(4, 1, 2), Blocks.STONE.defaultBlockState());
        int[] closedAt = {-1};
        each(h, "piston_door", r -> {
            if (r.tick == 10) {
                h.setBlock(new BlockPos(0, 0, 2), Blocks.REDSTONE_BLOCK.defaultBlockState());
                h.setBlock(new BlockPos(5, 0, 2), Blocks.REDSTONE_BLOCK.defaultBlockState());
            }
            boolean closed = at(h, 2, 1, 2).getBlock() == Blocks.STONE && at(h, 3, 1, 2).getBlock() == Blocks.STONE;
            if (closed && closedAt[0] < 0) closedAt[0] = r.tick;
            if (r.tick == 60) {
                h.setBlock(new BlockPos(0, 0, 2), Blocks.STONE.defaultBlockState());
                h.setBlock(new BlockPos(5, 0, 2), Blocks.STONE.defaultBlockState());
            }
            boolean open = at(h, 1, 1, 2).getBlock() == Blocks.STONE && at(h, 4, 1, 2).getBlock() == Blocks.STONE
                && at(h, 2, 1, 2).isAir() && at(h, 3, 1, 2).isAir();
            if (r.tick > 60 && closedAt[0] >= 0 && open) r.finish("closedAt=" + closedAt[0]);
        });
    }

    /**
     * An item sorter cell: a hopper line carries 20 diamonds and 20 cobblestone over a filter hopper that holds 18
     * diamonds and one each of four other items. A comparator reads the filter; past its resting strength the signal
     * reaches (through two dusts and a repeater) the block a redstone torch hangs on, which unlocks the hopper under
     * the filter for as long as the filter holds more than its 18 diamonds. Diamonds end in the side chest, cobblestone
     * at the end of the line.
     */
    @GameTest(structure = BOX16, maxTicks = 1500)
    public void itemSorter(GameTestHelper h) {
        BlockState stone = Blocks.STONE.defaultBlockState(), dust = Blocks.REDSTONE_WIRE.defaultBlockState();
        // supports for the comparator, the dusts, the repeater and the block the torch hangs on (B at 3 2 1)
        for (int[] p : new int[][] {{1, 2, 2}, {1, 2, 3}, {1, 2, 4}, {1, 2, 5}, {1, 2, 6}, {2, 2, 6}, {3, 2, 6}, {3, 2, 5}, {3, 2, 4},
            {3, 2, 3}, {3, 2, 2}, {3, 2, 1}}) h.setBlock(new BlockPos(p[0], p[1], p[2]), stone);
        h.setBlock(new BlockPos(1, 2, 0), Blocks.CHEST.defaultBlockState());
        h.setBlock(new BlockPos(1, 2, 1), hopper(Direction.NORTH));
        h.setBlock(new BlockPos(1, 3, 1), hopper(Direction.SOUTH));
        Container filter = container(h, 1, 3, 1);
        filter.setItem(0, new ItemStack(Items.DIAMOND, 18));
        filter.setItem(1, new ItemStack(Items.STICK, 1));
        filter.setItem(2, new ItemStack(Items.STRING, 1));
        filter.setItem(3, new ItemStack(Items.FEATHER, 1));
        filter.setItem(4, new ItemStack(Items.BONE, 1));
        h.setBlock(new BlockPos(2, 2, 1), Blocks.REDSTONE_WALL_TORCH.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST));
        h.setBlock(new BlockPos(1, 3, 2), horizontal(Blocks.COMPARATOR, Direction.NORTH));
        h.setBlock(new BlockPos(1, 3, 3), dust);
        h.setBlock(new BlockPos(1, 3, 4), dust);
        h.setBlock(new BlockPos(1, 3, 5), horizontal(Blocks.REPEATER, Direction.NORTH));
        for (int[] p : new int[][] {{1, 3, 6}, {2, 3, 6}, {3, 3, 6}, {3, 3, 5}, {3, 3, 4}, {3, 3, 3}, {3, 3, 2}, {3, 3, 1}}) {
            h.setBlock(new BlockPos(p[0], p[1], p[2]), dust);
        }
        for (int x = 0; x <= 2; x++) h.setBlock(new BlockPos(x, 4, 1), hopper(Direction.EAST));
        h.setBlock(new BlockPos(3, 4, 1), Blocks.CHEST.defaultBlockState());
        h.setBlock(new BlockPos(0, 5, 1), Blocks.CHEST.defaultBlockState());
        Container in = container(h, 0, 5, 1);
        in.setItem(0, new ItemStack(Items.DIAMOND, 20));
        in.setItem(1, new ItemStack(Items.COBBLESTONE, 20));
        String[] last = {""};
        int[] still = {0};
        each(h, "item_sorter", r -> {
            int sorted = count(container(h, 1, 2, 0), Items.DIAMOND);
            int passed = count(container(h, 3, 4, 1), Items.COBBLESTONE);
            int strays = count(container(h, 3, 4, 1), null) - passed + count(container(h, 1, 2, 0), null) - sorted;
            int rest = count(container(h, 1, 3, 1), Items.DIAMOND);
            int line = count(container(h, 0, 4, 1), null) + count(container(h, 1, 4, 1), null) + count(container(h, 2, 4, 1), null);
            String state = "sorted=" + sorted + " passed=" + passed + " strays=" + strays + " filter=" + rest + " lock="
                + count(container(h, 1, 2, 1), null) + " line=" + line + " input=" + count(container(h, 0, 5, 1), null)
                + " torch=" + at(h, 2, 2, 1).getValue(BlockStateProperties.LIT);
            if (r.tick % 100 == 0) Harness.LOG.info("CI GAMETEST STATE item_sorter tick={} {}", r.tick, state);
            // finished once nothing has moved for 100 ticks: where every item ended up is the result
            still[0] = state.equals(last[0]) ? still[0] + 1 : 0;
            last[0] = state;
            if (still[0] >= 100) r.finish(state);
        });
    }

    /**
     * Six wheat on hydrated farmland, bone-mealed by dispensers on an observer clock. Each bone meal grows a crop by a
     * random amount drawn from the level's shared random, which the GameTest world also draws on in ways that depend on
     * how fast its chunks loaded, so how many bone meals it takes varies between vanilla's own runs: the result is the
     * farm's outcome at tick 200 (all six grown), not the count.
     */
    @GameTest(structure = BOX16, maxTicks = 400)
    public void cropFarm(GameTestHelper h) {
        BlockState stone = Blocks.STONE.defaultBlockState();
        fill(h, 0, 0, 0, 9, 0, 3, stone);
        fill(h, 0, 1, 0, 9, 1, 0, stone);
        h.setBlock(new BlockPos(1, 1, 1), stone);
        h.setBlock(new BlockPos(8, 1, 1), stone);
        for (int x = 2; x <= 7; x++) {
            h.setBlock(new BlockPos(x, 1, 1), Blocks.WATER.defaultBlockState());
            h.setBlock(new BlockPos(x, 1, 2), Blocks.FARMLAND.defaultBlockState().setValue(BlockStateProperties.MOISTURE, 7));
            h.setBlock(new BlockPos(x, 2, 2), Blocks.WHEAT.defaultBlockState());
            h.setBlock(new BlockPos(x, 3, 2), facing(Blocks.DISPENSER, Direction.DOWN));
            container(h, x, 3, 2).setItem(0, new ItemStack(Items.BONE_MEAL, 64));
            h.setBlock(new BlockPos(x, 4, 2), Blocks.REDSTONE_WIRE.defaultBlockState());
        }
        h.setBlock(new BlockPos(1, 4, 2), facing(Blocks.OBSERVER, Direction.WEST));
        h.setBlock(new BlockPos(0, 4, 2), facing(Blocks.OBSERVER, Direction.EAST));
        each(h, "crop_farm", r -> {
            int grown = 0, left = 0;
            for (int x = 2; x <= 7; x++) {
                BlockState s = at(h, x, 2, 2);
                if (s.getBlock() == Blocks.WHEAT && s.getValue(BlockStateProperties.AGE_7) == 7) grown++;
                left += count(container(h, x, 3, 2), Items.BONE_MEAL);
            }
            if (r.tick == 200) r.finish("grown=" + grown + " dispensersEmpty=" + (left == 0));
        });
    }

    /** Four cows dropped 25 blocks onto a hopper; the drops end in a chest. */
    @GameTest(structure = TALL, maxTicks = 600)
    public void mobFarm(GameTestHelper h) {
        h.setBlock(new BlockPos(2, 0, 2), Blocks.CHEST.defaultBlockState());
        h.setBlock(new BlockPos(2, 1, 2), hopper(Direction.DOWN));
        BlockState glass = Blocks.GLASS.defaultBlockState();
        for (int y = 2; y <= 29; y++) {
            for (int x = 1; x <= 3; x++) {
                for (int z = 1; z <= 3; z++) {
                    if (x != 2 || z != 2) h.setBlock(new BlockPos(x, y, z), glass);
                }
            }
        }
        List<Cow> cows = new ArrayList<>();
        for (int i = 0; i < 4; i++) cows.add(h.spawn(EntityTypes.COW, new BlockPos(2, 26, 2)));
        each(h, "mob_farm", r -> {
            for (Cow c : cows) if (c.isAlive()) return;
            AABB box = new AABB(h.absolutePos(BlockPos.ZERO)).inflate(6, 32, 6);
            if (!h.getLevel().getEntitiesOfClass(ItemEntity.class, box).isEmpty()) return;
            if (count(container(h, 2, 1, 2), null) > 0) return;
            Container chest = container(h, 2, 0, 2);
            r.finish("drops=" + count(chest, null) + " beef=" + count(chest, Items.BEEF) + " leather=" + count(chest, Items.LEATHER));
        });
    }

    /** An unemployed villager takes the lectern's job; then one trade with it. */
    @GameTest(structure = BOX16, maxTicks = 2400)
    public void villagerTrade(GameTestHelper h) {
        BlockState stone = Blocks.STONE.defaultBlockState(), glass = Blocks.GLASS.defaultBlockState();
        fill(h, 0, 0, 0, 7, 0, 7, stone);
        for (int i = 0; i <= 7; i++) {
            fill(h, i, 1, 0, i, 2, 0, glass);
            fill(h, i, 1, 7, i, 2, 7, glass);
            fill(h, 0, 1, i, 0, 2, i, glass);
            fill(h, 7, 1, i, 7, 2, i, glass);
        }
        h.setBlock(new BlockPos(5, 1, 5), Blocks.LECTERN.defaultBlockState());
        Villager v = h.spawn(EntityTypes.VILLAGER, new BlockPos(2, 1, 2));
        each(h, "villager_trade", r -> {
            String data = String.valueOf(v.getVillagerData());
            if (!data.contains("librarian")) return;
            var offers = v.getOffers();
            if (offers.isEmpty()) return;
            var offer = offers.get(0);
            offer.increaseUses();
            v.notifyTrade(offer);
            r.finish("offers=" + offers.size() + " first=" + offer.getResult().getItem() + " xp=" + v.getVillagerXp());
        });
    }
}
