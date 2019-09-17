package org.gielinor.game.world.map;

import org.gielinor.cache.index.impl.MapIndex;
import org.gielinor.cache.util.ZipUtils;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.music.MusicZone;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.build.DynamicRegion;
import org.gielinor.game.world.map.build.LandscapeParser;
import org.gielinor.game.world.map.build.MapscapeParser;
import org.gielinor.game.world.map.zone.RegionZone;
import org.gielinor.game.world.update.UpdateSequence;
import org.gielinor.rs2.pulse.Pulse;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a region.
 *
 * @author Emperor
 */
public class Region {

    /**
     * The default size of a region.
     */
    public static final int SIZE = 64;

    /**
     * The region x-coordinate.
     */
    private final int x;

    /**
     * The region y-coordinate.
     */
    private final int y;

    /**
     * The region planes.
     */
    private final RegionPlane[] planes = new RegionPlane[4];

    /**
     * The activity pulse.
     */
    private final Pulse activityPulse;

    /**
     * The region zones lying in this region.
     */
    private final List<RegionZone> regionZones = new ArrayList<>();

    /**
     * The music zones lying in this region.
     */
    private final List<MusicZone> musicZones = new ArrayList<>();

    /**
     * If the region is active.
     */
    private boolean active;

    /**
     * The amount of objects in this region.
     */
    private int objectCount;

    /**
     * If the region has flags.
     */
    private boolean hasFlags;

    /**
     * If the region has been loaded.
     */
    private boolean loaded;

    /**
     * Constructs a new {@code Region} {@code Object}.
     *
     * @param x The x-coordinate of the region.
     * @param y The y-coordinate of the region.
     */
    public Region(int x, int y) {
        this.x = x;
        this.y = y;
        for (int plane = 0; plane < 4; plane++) {
            planes[plane] = new RegionPlane(this, plane);
        }
        this.activityPulse = new Pulse(50) {

            @Override
            public boolean pulse() {
                flagInactive();
                return true;
            }
        };
        activityPulse.stop();
    }

    /**
     * Gets the base location.
     *
     * @return The base location.
     */
    public Location getBaseLocation() {
        return Location.create(x << 6, y << 6, 0);
    }

    public Location getCenterLocation() {
        return Location.create(((getId() >> 8) << 6), ((getId() & 0xFF) << 6), 0);
    }

    /**
     * Adds a region zone to this region.
     *
     * @param zone The region zone.
     */
    public void add(RegionZone zone) {
        regionZones.add(zone);
        for (RegionPlane plane : planes) {
            for (NPC npc : plane.getNpcs()) {
                npc.getZoneMonitor().updateLocation(npc.getLocation());
            }
            for (Player p : plane.getPlayers()) {
                p.getZoneMonitor().updateLocation(p.getLocation());
            }
        }
    }

    /**
     * Adds a player to this region.
     *
     * @param player The player.
     */
    public void add(Player player) {
        planes[player.getLocation().getZ()].add(player);
        flagActive();
    }

    /**
     * Adds an npc to this region.
     *
     * @param npc The npc.
     */
    public void add(NPC npc) {
        planes[npc.getLocation().getZ()].add(npc);
    }

    /**
     * Removes an NPC from this region.
     *
     * @param npc The NPC.
     */
    public void remove(NPC npc) {
        if (npc.getViewport().getCurrentPlane() != null) {
            npc.getViewport().getCurrentPlane().remove(npc);
        } else {
            planes[npc.getLocation().getZ()].remove(npc);
        }
    }

    /**
     * Removes a player from this region.
     *
     * @param player The player.
     */
    public void remove(Player player) {
        player.getViewport().getCurrentPlane().remove(player);
        checkInactive();
    }

    /**
     * Checks if the region is inactive, if so it will start the inactivity flagging.
     *
     * @return <code>True</code> if the region is inactive.
     */
    public boolean checkInactive() {
        boolean setInactive = true;
        for (RegionPlane p : planes) {
            if (!p.getPlayers().isEmpty()) {
                setInactive = false;
            }
        }
        if (setInactive && !activityPulse.isRunning()) {
            activityPulse.start();
            World.submit(activityPulse);
        }
        return setInactive;
    }

    /**
     * Checks if this region has the inactivity flagging pulse running.
     *
     * @return <code>True</code> if so.
     */
    public boolean isPendingRemoval() {
        return activityPulse.isRunning();
    }

    /**
     * Flags the region as active.
     */
    public void flagActive() {
        activityPulse.stop();
        if (!active) {
            active = true;
            load(this);
            for (RegionPlane r : planes) {
                r.getNpcs().stream().filter(Node::isActive).forEach(n -> {
                    UpdateSequence.getRenderableNpcs().add(n);
                });
            }
        }
    }

    /**
     * Flags the region as inactive.
     */
    protected void flagInactive() {
        unload(this);
        active = false;
    }

    /**
     * Loads the flags for a region.
     *
     * @param r The region.
     */
    public static void load(Region r) {
        load(r, false);
    }

    /**
     * Loads the flags for a region.
     *
     * @param r            The region.
     * @param storeObjects if all objects in this region should be stored (rather than just the ones with options).
     */
    public static void load2(Region r, boolean storeObjects) {
        try {
            if (r.isLoaded()) {
                return;
            }
            boolean dynamic = r instanceof DynamicRegion;
            int regionId = dynamic ? r.getRegionId() : r.getId();
            MapIndex mapIndex = World.getWorld().getCache().getIndexTable().getMapIndex(regionId);
            if (mapIndex == null) {
                //loadPreloaded(r, storeObjects);
                return;
            }
            int mapscapeId = mapIndex.getMapFile();
            byte[][][] mapscapeData = new byte[4][SIZE][SIZE];
            for (RegionPlane plane : r.planes) {
                plane.getFlags().setLandscape(new boolean[SIZE][SIZE]);
                plane.getFlags().setClippingFlags(new int[SIZE][SIZE]);
                plane.getProjectileFlags().setClippingFlags(new int[SIZE][SIZE]);
            }
            if (mapscapeId > -1) {
                try {
                    ByteBuffer mapscape = World.getWorld().getCache().getFile(4, mapscapeId).getBuffer();
                    MapscapeParser.parse(r, mapscapeData, mapscape);
                } catch (Throwable t) {
                    if (regionId == 7503) {
                        t.printStackTrace();
                    }
                }
            }
            r.hasFlags = dynamic;
            r.setLoaded(true);
            int landscapeId = mapIndex.getLandscapeFile();
            if (landscapeId > -1) {
                try {
                    byte[] landscape = World.getWorld().getCache().getFile(4, landscapeId).getBytes();
                    if (landscape == null || landscape.length < 4) {
                        return;
                    }
                    ByteBuffer landscapeData = ZipUtils.unzip(World.getWorld().getCache().getFile(4, landscapeId));
                    r.hasFlags = true;
                    LandscapeParser.parse(r, mapscapeData, landscapeData, storeObjects);
                } catch (Throwable t) {
                    if (regionId == 7503) {
                        t.printStackTrace();
                    }
                }
            }
            MapscapeParser.clipMapscape(r, mapscapeData);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void load(Region r, boolean storeObjects) {
        try {
            if (r.isLoaded()) {
                return;
            }
            boolean dynamic = r instanceof DynamicRegion;
            int regionId = dynamic ? r.getRegionId() : r.getId();
            MapIndex mapIndex = World.getWorld().getCache().getIndexTable().getMapIndex(regionId);
            if (mapIndex == null) {
                return;
            }
            int mapscapeId = mapIndex.getMapFile();
            byte[][][] mapscapeData = new byte[4][SIZE][SIZE];
            for (RegionPlane plane : r.planes) {
                plane.getFlags().setLandscape(new boolean[SIZE][SIZE]);
                plane.getFlags().setClippingFlags(new int[SIZE][SIZE]);
                plane.getProjectileFlags().setClippingFlags(new int[SIZE][SIZE]);
            }
            if (mapscapeId > -1) {
                try { // TODO 317
                    ByteBuffer mapscape = ZipUtils.unzip(World.getWorld().getCache().getFile(8, mapscapeId));
                    MapscapeParser.parse(r, mapscapeData, mapscape);
                } catch (Throwable t) {
                    if (regionId == 7503) {
                        t.printStackTrace();
                    }
                }
            }
            r.hasFlags = dynamic;
            r.setLoaded(true);
            int landscapeId = mapIndex.getLandscapeFile();
            if (landscapeId > -1) {
                try {
                    byte[] landscape = World.getWorld().getCache().getFile(8, landscapeId).getBytes();
                    if (landscape == null || landscape.length < 4) {
                        return;
                    }
                    ByteBuffer landscapeData = ZipUtils.unzip(World.getWorld().getCache().getFile(8, landscapeId));
                    r.hasFlags = true;
                    LandscapeParser.parse(r, mapscapeData, landscapeData, storeObjects);
                } catch (Throwable t) {
                    if (regionId == 7503) {
                        t.printStackTrace();
                    }
                }
            }
            MapscapeParser.clipMapscape(r, mapscapeData);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    /**
     * Unloads a region.
     *
     * @param r The region.
     */
    private static void unload(Region r) {
        for (RegionPlane p : r.planes) {
            if (!p.getPlayers().isEmpty()) {
                System.err.println("Players still in region!");
                r.flagActive();
                return;
            }
        }
        for (RegionPlane p : r.planes) {
            p.clear();
            if (!(r instanceof DynamicRegion)) {
                for (NPC n : p.getNpcs()) {
                    n.onRegionInactivity();
                }
            }
        }
    }

    /**
     * Gets the active.
     *
     * @return The active.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active.
     *
     * @param active The active to set.
     * @deprecated This should not be used, instead use the {@link #flagInactive()},
     * {@link #flagActive()} & {@link #checkInactive()} methods to safely change the activity state.
     */
    @Deprecated
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets the region id.
     *
     * @return The region id.
     */
    public int getId() {
        return x << 8 | y;
    }

    /**
     * Gets the real region id (this returns the copied region id for dynamic regions).
     *
     * @return The region  id.
     */
    public int getRegionId() {
        return getId();
    }

    /**
     * Gets the x.
     *
     * @return The x.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y.
     *
     * @return The y.
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the planes.
     *
     * @return The planes.
     */
    public RegionPlane[] getPlanes() {
        return planes;
    }

    /**
     * Gets the regionZones.
     *
     * @return The regionZones.
     */
    public List<RegionZone> getRegionZones() {
        return regionZones;
    }

    /**
     * Gets the musicZones.
     *
     * @return The musicZones.
     */
    public List<MusicZone> getMusicZones() {
        return musicZones;
    }

    /**
     * Gets the object count.
     *
     * @return The object count.
     */
    public int getObjectCount() {
        return objectCount;
    }

    /**
     * Sets the object count.
     *
     * @param objectCount The object count.
     */
    public void setObjectCount(int objectCount) {
        this.objectCount = objectCount;
    }

    /**
     * Gets the hasFlags.
     *
     * @return The hasFlags.
     */
    public boolean isHasFlags() {
        return hasFlags;
    }

    /**
     * Sets the hasFlags.
     *
     * @param hasFlags The hasFlags to set.
     */
    public void setHasFlags(boolean hasFlags) {
        this.hasFlags = hasFlags;
    }

    /**
     * Sets the region time out duration.
     *
     * @param ticks The amount of ticks before the region is flagged as inactive.
     */
    public void setRegionTimeOut(int ticks) {
        activityPulse.setDelay(ticks);
    }

    /**
     * Gets the loaded.
     *
     * @return The loaded.
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Sets the loaded.
     *
     * @param loaded The loaded to set.
     */
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
}
