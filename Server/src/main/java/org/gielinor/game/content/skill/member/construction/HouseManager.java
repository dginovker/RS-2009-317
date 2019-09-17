package org.gielinor.game.content.skill.member.construction;

import org.gielinor.game.component.Component;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.*;
import org.gielinor.game.world.map.build.DynamicRegion;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.parser.player.SavingModule;
import org.gielinor.rs2.pulse.Pulse;

import java.nio.ByteBuffer;

/**
 * Manages the player's house.
 *
 * @author Emperor
 * @author Vexia
 */
public final class HouseManager implements SavingModule {

    /**
     * The player instance.
     */
    private final Player player;

    /**
     * The current region.
     */
    private DynamicRegion region;

    /**
     * The house location.
     */
    private HouseLocation location = HouseLocation.NOWHERE;

    /**
     * The house style.
     */
    private HousingStyle style = HousingStyle.BASIC_WOOD;

    /**
     * The player's rooms.
     */
    private final Room[][][] rooms = new Room[4][8][8];

    /**
     * If building mode is enabled.
     */
    private boolean buildingMode;

    /**
     * Constructs a new {@code HouseManager} {@code Object}.
     *
     * @param player the player.
     */
    public HouseManager(Player player) {
        this.player = player;
    }

    @Override
    public void save(ByteBuffer byteBuffer) {
        byteBuffer.put((byte) location.ordinal());
        for (int z = 0; z < 4; z++) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    Room room = rooms[z][x][y];
                    if (room != null) {
                        byteBuffer.put((byte) z).put((byte) x).put((byte) y);
                        byteBuffer.put((byte) room.getProperties().ordinal());
                        byteBuffer.put((byte) room.getRotation().toInteger());
                        for (int i = 0; i < room.getHotspots().length; i++) {
                            if (room.getHotspots()[i].getDecorationIndex() > -1) {
                                byteBuffer.put((byte) i);
                                byteBuffer.put((byte) room.getHotspots()[i].getDecorationIndex());
                            }
                        }
                        byteBuffer.put((byte) -1);
                    }
                }
            }
        }
        byteBuffer.put((byte) -1); // EOF
    }

    @Override
    public void parse(ByteBuffer byteBuffer) {
        location = HouseLocation.values()[byteBuffer.get() & 0xFF];
        int z = 0;
        while ((z = byteBuffer.get()) != -1) {
            Room room = rooms[z][byteBuffer.get()][byteBuffer.get()] = new Room(RoomProperties.values()[byteBuffer.get() & 0xFF]);
            room.configure(style);
            room.setRotation(Direction.get(byteBuffer.get() & 0xFF));
            int spot = 0;
            while ((spot = byteBuffer.get()) != -1) {
                room.getHotspots()[spot].setDecorationIndex(byteBuffer.get() & 0xFF);
            }
        }
    }

    /**
     * Opens the house settings component.
     */
    public void openSettings() {
        player.getInterfaceState().openSingleTab(new Component(398));
    }

    /**
     * Enters the player's house.
     *
     * @param player       The player entering.
     * @param buildingMode If building mode is enabled.
     */
    public void enter(final Player player, boolean buildingMode) {
        if (this.buildingMode != buildingMode || !isLoaded()) {
            this.buildingMode = buildingMode;
            construct();
        }
        player.setAttribute("poh_entry", this);
        player.lock(2);
        player.getProperties().setTeleportLocation(getEnterLocation());
        player.getInterfaceState().openComponent(399);
        player.getConfigManager().set(261, buildingMode);
        player.getConfigManager().set(262, getRoomAmount());
        Room room = rooms[0][4][4];
        if (room.isNewRoom()) {
            BuildHotspot buildHotspot = BuildHotspot.forId(15361);
            Decoration decoration = buildHotspot.getDecorations()[0];
            GameObject gameObject = new GameObject(15361, new Location(room.getProperties().getChunkX() + 35, room.getProperties().getChunkX() + 35, 0), 1);
            RoomBuilder.buildDecoration(player, buildHotspot, decoration, gameObject, false);
            room.setNewRoom(false);
        }
        World.submit(new Pulse(1, player) {

            @Override
            public boolean pulse() {
                player.getInterfaceState().close();
                return true;
            }
        });
    }

    /**
     * Leaves this house.
     *
     * @param player The player leaving.
     */
    public static void leave(Player player) {
        HouseManager house = player.getAttribute("poh_entry", player.getHouseManager());
        player.getProperties().setTeleportLocation(house.location.getExitLocation());
    }

    /**
     * Toggles the building mode.
     *
     * @param player The house owner.
     * @param enable If the building mode should be enabled.
     */
    public void toggleBuildingMode(Player player, boolean enable) {
        if (!isInHouse()) {
            player.getActionSender().sendMessage("Building mode really only helps if you're in a house.");
            return;
        }
        if (buildingMode != enable) {
            if (enable) {
                expelGuests(player);
            }
            reload(player, enable);
            player.getActionSender().sendMessage("Building mode is now " + (buildingMode ? "on" : " off") + ".");
        }
    }

    /**
     * Reloads the house.
     *
     * @param player       The player.
     * @param buildingMode If building mode should be enabled.
     */
    public void reload(Player player, boolean buildingMode) {
        int diffX = player.getLocation().getX() - region.getBaseLocation().getX();
        int diffY = player.getLocation().getY() - region.getBaseLocation().getY();
        int diffZ = player.getLocation().getZ() - region.getBaseLocation().getZ();
        enter(player, buildingMode);
        player.getProperties().setTeleportLocation(region.getBaseLocation().transform(diffX, diffY, diffZ));
    }

    /**
     * Expels the guests from the house.
     *
     * @param player The house owner.
     */
    public void expelGuests(Player player) {
        if (isLoaded()) {
            for (RegionPlane plane : region.getPlanes()) {
                for (Player p : plane.getPlayers()) {
                    if (p != player) {
                        leave(p);
                    }
                }
            }
        } else {
            player.getActionSender().sendMessages("You're not in a house! You can't expel people from the world, even if you don't like", "them!");
        }
    }

    /**
     * Gets the entering location.
     *
     * @return The entering location.
     */
    public Location getEnterLocation() {
        if (region == null) {
            System.err.println("House wasn't constructed yet!");
            return null;
        }
        for (int z = 0; z < 4; z++) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    Room room = rooms[z][x][y];
                    if (room != null) {
                        for (Hotspot h : room.getHotspots()) {
                            if (h.getDecorationIndex() > -1) {
                                Decoration d = h.getHotspot().getDecorations()[h.getDecorationIndex()];
                                if (d == Decoration.PORTAL) {
                                    return region.getBaseLocation().transform(x * 8 + h.getChunkX(), y * 8 + h.getChunkY() + 2, 0);
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Redecorates the house.
     *
     * @param style The new style.
     */
    public void redecorate(HousingStyle style) {
        this.style = style;
        for (int z = 0; z < 4; z++) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    Room room = rooms[z][x][y];
                    if (room != null) {
                        room.decorate(style);
                    }
                }
            }
        }
    }

    /**
     * Creates the default house.
     *
     * @param houseLocation The house location.
     */
    public void create(HouseLocation houseLocation) {
        Room room = rooms[0][4][4] = new Room(RoomProperties.GARDEN);
        room.configure(style);
        room.getHotspots()[0].setDecorationIndex(0);
        room.setNewRoom(true);
        this.location = houseLocation;
    }

    /**
     * Constructs the dynamic region for the house.
     *
     * @return The region.
     */
    public DynamicRegion construct() {
        Region from = RegionManager.forId(style.getRegionId());
        Region.load(from, true);
        RegionChunk defaultChunk = from.getPlanes()[style.getPlane()].getRegionChunk(1, 0);
        ZoneBorders borders = DynamicRegion.reserveArea(8, 8);
        region = new DynamicRegion(7503, borders.getSouthWestX() >> 6, borders.getSouthWestY() >> 6);
        region.setBorders(borders);
        RegionManager.getRegionCache().put(region.getId(), region);
        for (int z = 0; z < 1; z++) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    Room room = rooms[z][x][y];
                    if (room == null) {
                        region.setChunk(z, x, y, defaultChunk.copy(region.getPlanes()[0]));
                    } else {
                        RegionChunk copy = room.getChunk().copy(region.getPlanes()[0]);
                        region.replaceChunk(z, x, y, copy, room.getChunk().getPlane().getRegion());
                        BuildRegionChunk buildRegionChunk = new BuildRegionChunk(copy.getBase(), copy.getRotation(), copy.getPlane());
                        room.loadDecorations(buildRegionChunk, buildingMode);
                    }
                }
            }
        }
        return region;
    }

    /**
     * Resets the house.
     */
    public void reset() {
        for (int z = 0; z < 4; z++) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    getRooms()[z][x][y] = null;
                }
            }
        }
        create(getLocation());
    }

    /**
     * Checks if the player is in the house.
     *
     * @return {@code True} if so.
     */
    public boolean isInHouse() {
        return isLoaded() && player.getHouseManager().getRegion() == player.getViewport().getRegion();
    }

    /**
     * Checks if an exit exists on the given room.
     *
     * @param roomX     The x-coordinate of the room.
     * @param roomY     The y-coordinate of the room.
     * @param direction The exit direction.
     * @return {@code True} if so.
     */
    public boolean hasExit(int z, int roomX, int roomY, Direction direction) {
        Room room = rooms[z][roomX][roomY];
        boolean horizontal = direction.toInteger() % 2 != 0;
        int index = (direction.toInteger() + (horizontal ? 1 : 3)) % 4;
        if (room != null && room.getExits()[index]) {
            return true;
        }
        return false;
    }

    /**
     * Gets the amount of rooms.
     *
     * @return The amount of rooms.
     */
    public int getRoomAmount() {
        int count = 0;
        for (int z = 0; z < 4; z++) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    if (rooms[z][x][y] != null) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * Checks if the house region was constructed and active.
     *
     * @return {@code True} if an active region for the house exists.
     */
    public boolean isLoaded() {
        return region != null && region.isActive();
    }

    /**
     * Gets the hasHouse.
     *
     * @return The hasHouse.
     */
    public boolean hasHouse() {
        return location != HouseLocation.NOWHERE;
    }

    /**
     * Gets the rooms.
     *
     * @return The rooms.
     */
    public Room[][][] getRooms() {
        return rooms;
    }

    /**
     * Gets the location.
     *
     * @return The location.
     */
    public HouseLocation getLocation() {
        return location;
    }

    /**
     * Sets the location.
     *
     * @param location The location to set.
     */
    public void setLocation(HouseLocation location) {
        this.location = location;
    }

    /**
     * Checks if the building mode is enabled.
     *
     * @return {@code True} if so.
     */
    public boolean isBuildingMode() {
        return buildingMode;
    }

    /**
     * Gets the region.
     *
     * @return The region.
     */
    public Region getRegion() {
        return region;
    }

    /**
     * Gets the style.
     *
     * @return the style
     */
    public HousingStyle getStyle() {
        return style;
    }

    /**
     * Sets the style.
     *
     * @param style the style to set.
     */
    public void setStyle(HousingStyle style) {
        this.style = style;
    }

}
