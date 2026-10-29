package com.meakaandre.ciserver;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * CI only (phase 2, P2-0): a Create train in SIFTEC's scripted run, set up at fixed ticks in every run alike. Create
 * has no commands for assembling a train or giving it a schedule, so this does those two steps by reflection (the
 * harness is compiled without Create); everything else is placed with ordinary commands.
 *
 * <p>Script lines (ci/server/scripted-run-siftec.txt): {@code at T ci-train stations AX AY AZ BX BY BZ} names the two
 * stations "CI A" and "CI B"; {@code at T ci-train build} puts station A in assembly mode and builds a one-bogey
 * carriage behind it (a bogey, three casings, a blaze burner and two controls facing it, so the blaze is a conductor
 * both ways), with Super Glue over it; {@code at T ci-train assemble} assembles it; {@code at T ci-train schedule} gives the train a cycling schedule A to B and back
 * (1 second's wait at each). Each tick after that the train's station is read; at the end of the run one line,
 * {@code CI TRAIN result=...}, lists every arrival with its tick, the train's final speed and schedule state. CI
 * compares that line between vanilla's runs and Quicksilver's (it must match exactly, with at least 2 arrivals).
 */
final class CiTrain {
    private static Object stationA, stationB;  // StationBlockEntity
    private static BlockPos posA;
    private static final List<String> arrivals = new ArrayList<>();
    private static String lastStation = null, error = null;
    private static boolean scheduled = false;

    private CiTrain() {
    }

    static void command(MinecraftServer server, int tick, String args) {
        String[] p = args.trim().split("\\s+");
        try {
            switch (p[0]) {
                case "stations" -> stations(server, p);
                case "build" -> build(server, tick);
                case "assemble" -> assemble(server);
                case "schedule" -> schedule(server);
                default -> throw new IllegalArgumentException("unknown ci-train step " + p[0]);
            }
            Harness.LOG.info("CI TRAIN STEP t={} {} done", tick, p[0]);
        } catch (Throwable e) {
            error = p[0] + ": " + e;
            Harness.LOG.warn("CI TRAIN STEP t={} {} FAILED", tick, p[0], e);
        }
    }

    private static void stations(MinecraftServer server, String[] p) throws Exception {
        ServerLevel level = server.overworld();
        posA = new BlockPos(Integer.parseInt(p[1]), Integer.parseInt(p[2]), Integer.parseInt(p[3]));
        BlockPos posB = new BlockPos(Integer.parseInt(p[4]), Integer.parseInt(p[5]), Integer.parseInt(p[6]));
        stationA = level.getBlockEntity(posA);
        stationB = level.getBlockEntity(posB);
        name(stationA, "CI A");
        name(stationB, "CI B");
    }

    private static void name(Object be, String name) throws Exception {
        if (be == null) throw new IllegalStateException("no station block entity");
        Object station = call(be, "getStation");
        if (station == null) throw new IllegalStateException("station has no edge point on the track yet");
        set(station, "name", name);
    }

    /** Station A in assembly mode and the carriage's blocks placed behind it (assembled a few ticks later). */
    private static void build(MinecraftServer server, int tick) throws Exception {
        ServerLevel level = server.overworld();
        Object entered = call(stationA, "enterAssemblyMode", (Object) null);
        Direction d = (Direction) call(stationA, "getAssemblyDirection");
        Object edge = get(stationA, "edgePoint");
        BlockPos track = (BlockPos) call(edge, "getGlobalPosition");
        Harness.LOG.info("CI TRAIN assembly mode {} at track {}, assembly direction {}", entered, track, d);
        String axis = d.getAxis().getName();
        BlockPos up = BlockPos.ZERO.above();
        BlockPos bogey = track.relative(d).offset(up);
        BlockPos blaze = track.relative(d, 2).offset(up).above(2);
        List<String> cmds = new ArrayList<>();
        cmds.add(set(bogey, "create:small_bogey[axis=" + axis + "]"));
        for (int i = 1; i <= 3; i++) cmds.add(set(track.relative(d, i).above(2), "create:railway_casing"));
        cmds.add(set(blaze, "create:blaze_burner[blaze=smouldering]"));
        // controls next to the blaze on either side, each facing it: the blaze conducts in both directions
        cmds.add(set(blaze.relative(d.getOpposite()), "create:controls[facing=" + d.getName() + "]"));
        cmds.add(set(blaze.relative(d), "create:controls[facing=" + d.getOpposite().getName() + "]"));
        // Super Glue over the whole carriage: a carriage takes only what is glued to its bogey (Create:
        // "nothing_attached" without it)
        List<BlockPos> parts = new ArrayList<>(List.of(bogey, blaze, blaze.relative(d), blaze.relative(d.getOpposite())));
        for (int i = 1; i <= 3; i++) parts.add(track.relative(d, i).above(2));
        int x1 = Integer.MAX_VALUE, y1 = Integer.MAX_VALUE, z1 = Integer.MAX_VALUE, x2 = Integer.MIN_VALUE, y2 = Integer.MIN_VALUE, z2 = Integer.MIN_VALUE;
        for (BlockPos q : parts) {
            x1 = Math.min(x1, q.getX());
            y1 = Math.min(y1, q.getY());
            z1 = Math.min(z1, q.getZ());
            x2 = Math.max(x2, q.getX());
            y2 = Math.max(y2, q.getY());
            z2 = Math.max(z2, q.getZ());
        }
        // the glue entity keeps its box centred on its position in x and z, with its bottom at the position's y: so
        // it is summoned at the carriage's centre (exact decimals, which summon doesn't move)
        cmds.add(String.format(java.util.Locale.ROOT, "summon create:super_glue %.1f %.1f %.1f {Box:[0d,0d,0d,%dd,",
            (x1 + x2 + 1) / 2.0, (double) y1, (z1 + z2 + 1) / 2.0, x2 - x1 + 1)
            + (y2 - y1 + 1) + "d," + (z2 - z1 + 1) + "d]}");
        for (String c : cmds) Harness.runQuiet(server, tick, c);
    }

    private static void assemble(MinecraftServer server) throws Exception {
        call(stationA, "assemble", new UUID(0x51C5L, 0x7EC0L));
        if (train(server.overworld()) != null) {
            // as the station screen does once a train is built: a station in assembly mode is not a destination
            call(stationA, "exitAssemblyMode");
        } else {
            // Create reports a failed assembly on the station, not by throwing
            Object ex = get(stationA, "lastException");
            Object msg = ex == null ? null : get(ex, "component");
            throw new IllegalStateException("no train after assembly: " + msg + ", carriage " + get(stationA, "failedCarriageIndex")
                + ", bogeys at " + java.util.Arrays.toString((int[]) get(stationA, "bogeyLocations"))
                + ", assembly length " + get(stationA, "assemblyLength") + ", assembling " + call(stationA, "isAssembling"));
        }
    }

    private static String set(BlockPos p, String state) {
        return "setblock " + p.getX() + " " + p.getY() + " " + p.getZ() + " " + state;
    }

    private static void schedule(MinecraftServer server) throws Exception {
        Object train = train(server.overworld());
        if (train == null) throw new IllegalStateException("no train");
        CompoundTag tag = new CompoundTag();
        ListTag entries = new ListTag();
        for (String to : new String[] {"CI B", "CI A"}) {
            CompoundTag instruction = new CompoundTag();
            instruction.putString("Id", "create:destination");
            CompoundTag data = new CompoundTag();
            data.putString("Text", to);
            instruction.put("Data", data);
            CompoundTag delay = new CompoundTag();
            delay.putString("Id", "create:delay");
            CompoundTag delayData = new CompoundTag();
            delayData.putInt("Value", 1);
            delayData.putInt("TimeUnit", 1);  // seconds
            delay.put("Data", delayData);
            ListTag column = new ListTag();
            column.add(delay);
            ListTag conditions = new ListTag();
            conditions.add(column);
            CompoundTag entry = new CompoundTag();
            entry.put("Instruction", instruction);
            entry.put("Conditions", conditions);
            entries.add(entry);
        }
        tag.put("Entries", entries);
        tag.putBoolean("Cyclic", true);
        Class<?> scheduleClass = Class.forName("com.zurrtum.create.content.trains.schedule.Schedule");
        Method decode = null;
        for (Method m : scheduleClass.getMethods()) {
            if (m.getName().equals("decode") && m.getParameterCount() == 2) decode = m;
        }
        Object schedule = decode.invoke(null, NbtOps.INSTANCE, tag);
        call(get(train, "runtime"), "setSchedule", schedule, true);
        scheduled = true;
    }

    /** Every tick of the run: the train's current station, recording each arrival with its tick. */
    static void tick(MinecraftServer server, int tick) {
        if (!scheduled || error != null) return;
        try {
            Object train = train(server.overworld());
            if (train == null) {
                error = "the train is gone at tick " + tick;
                return;
            }
            Object station = call(train, "getCurrentStation");
            String name = station == null ? null : (String) get(station, "name");
            if (name != null && !name.equals(lastStation)) arrivals.add(name.substring(3) + "@" + tick);
            lastStation = name;
        } catch (Throwable e) {
            error = "tick: " + e;
        }
    }

    /** The run's result line, or null when the script had no train. */
    static String result(MinecraftServer server) {
        if (stationA == null && error == null) return null;
        StringBuilder b = new StringBuilder();
        b.append("arrivals=").append(arrivals.size()).append(' ').append(String.join(",", arrivals));
        try {
            Object train = train(server.overworld());
            if (train != null) {
                Object runtime = get(train, "runtime");
                b.append(" speed=").append(get(train, "speed")).append(" derailed=").append(get(train, "derailed"))
                    .append(" entry=").append(get(runtime, "currentEntry")).append(" state=").append(get(runtime, "state"));
                if (arrivals.size() < 2) {
                    // why a train didn't run its schedule: conductors, Create's status flags, the stations it can see
                    Object status = get(train, "status");
                    List<String> names = new ArrayList<>();
                    Object graph = get(train, "graph");
                    if (graph != null) {
                        Class<?> types = Class.forName("com.zurrtum.create.content.trains.graph.EdgePointType");
                        Object stationType = types.getField("STATION").get(null);
                        for (Object st : (java.util.Collection<?>) call(graph, "getPoints", stationType)) names.add(String.valueOf(get(st, "name")));
                    }
                    b.append(" conductors=").append(call(train, "hasForwardConductor")).append('/').append(call(train, "hasBackwardConductor"))
                        .append(" failedNavigation=").append(get(status, "navigation")).append(" missingConductor=").append(get(status, "conductor"))
                        .append(" graph=").append(graph != null).append(" stations=").append(String.join("|", names).replace(' ', '_'));
                }
            }
        } catch (Throwable e) {
            b.append(" read=").append(e);
        }
        if (error != null) b.append(" error=").append(error.replace(' ', '_'));
        return b.toString();
    }

    @SuppressWarnings("unchecked")
    private static Object train(ServerLevel level) throws Exception {
        Object manager = Class.forName("com.zurrtum.create.Create").getField("RAILWAYS").get(null);
        Object sided = call(manager, "sided", level);
        Map<UUID, ?> trains = (Map<UUID, ?>) get(sided, "trains");
        return trains.isEmpty() ? null : trains.values().iterator().next();
    }

    // ---------------------------------------------------------------- reflection

    private static Object call(Object target, String name, Object... args) throws Exception {
        for (Class<?> c = target.getClass(); c != null; c = c.getSuperclass()) {
            for (Method m : c.getDeclaredMethods()) {
                if (m.getName().equals(name) && m.getParameterCount() == args.length) {
                    m.setAccessible(true);
                    return m.invoke(target, args);
                }
            }
        }
        throw new NoSuchMethodException(target.getClass().getName() + "." + name + "/" + args.length);
    }

    private static Field field(Object target, String name) throws Exception {
        for (Class<?> c = target.getClass(); c != null; c = c.getSuperclass()) {
            try {
                Field f = c.getDeclaredField(name);
                f.setAccessible(true);
                return f;
            } catch (NoSuchFieldException e) {
                // the superclass, then
            }
        }
        throw new NoSuchFieldException(target.getClass().getName() + "." + name);
    }

    private static Object get(Object target, String name) throws Exception {
        return field(target, name).get(target);
    }

    private static void set(Object target, String name, Object value) throws Exception {
        field(target, name).set(target, value);
    }
}
