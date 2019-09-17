package plugin.skill.construction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.construction.Room;
import org.gielinor.game.content.skill.member.construction.RoomBuilder;
import org.gielinor.game.content.skill.member.construction.RoomProperties;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.node.object.ObjectBuilder;
import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.map.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the building a room dialogue.
 *
 * @author Emperor
 */
public final class BuildRoomDialogue extends DialoguePlugin {

    private static final Logger log = LoggerFactory.getLogger(BuildRoomDialogue.class);

    /**
     * The boundary door offsets.
     */
    private static final int[][] BOUNDARY_OFFSETS = { { 7, 4 }, { 4, 0 }, { 0, 4 }, { 4, 7 } };

    /**
     * The door hotspot.
     */
    private GameObject door;

    /**
     * The direction.
     */
    private Direction[] directions;

    /**
     * The room exits.
     */
    private boolean[] exits;

    /**
     * The rotation index.
     */
    private int index;

    /**
     * The boundaries of the room to build.
     */
    private List<GameObject> boundaries = new ArrayList<>();

    /**
     * The room we're building.
     */
    private Room room;

    /**
     * The room x-coordinate.
     */
    private int roomX;

    /**
     * The room y-coordinate.
     */
    private int roomY;

    /**
     * Constructs a new {@code BuildRoomDialogue} {@code Object}
     */
    public BuildRoomDialogue() {
        super();
    }

    /**
     * Constructs a new {@code BuildRoomDialogue} {@code Object}
     *
     * @param player The player.
     */
    public BuildRoomDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new BuildRoomDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        player.getInterfaceState().close();
        RoomProperties props = (RoomProperties) args[0];
        if (player.getSkills().getStaticLevel(Skills.CONSTRUCTION) < props.getLevel()) {
            interpreter.sendPlaneMessage(false, "You need a Construction level of " + props.getLevel() + " to buy this room.");
            stage = 2;
            return true;
        }
        if (!player.getInventory().contains(Item.COINS, props.getCost())) {
            interpreter.sendPlaneMessage(false, "You need " + props.getCost() + " coins to buy this room.");
            stage = 2;
            return true;
        }
        this.room = Room.create(player, props);
        this.door = player.getAttribute("con:hsobject");
        this.exits = room.getExits();
        int[] pos = RoomBuilder.getRoomPosition(door);
        roomX = pos[0];
        roomY = pos[1];
        this.directions = RoomBuilder.getAvailableRotations(player, exits, door, roomX, roomY);
        while (directions[index] == null) {
            if (++index == directions.length) {
                interpreter.sendPlaneMessage(false, "There's no space to build this room.");
                stage = 2;
                return false;
            }
        }
        options("Rotate clockwise", "Rotate anticlockwise", "Build", "Cancel");
        stage = 1;
        drawBoundaries();
        return true;
    }

    @Override
    public boolean close() {
        for (GameObject object : boundaries) {
            ObjectBuilder.remove(object);
        }
        boundaries.clear();
        return super.close();
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 1:
                switch (buttonId) {
                    case 1:
                    case 2:
                        rotate(buttonId == 2);
                        options("Rotate clockwise", "Rotate anticlockwise", "Build", "Cancel");
                        return true;
                    case 3:
                        if (player.getInventory().remove(new Item(Item.COINS, room.getProperties().getCost()))) {
                            RoomBuilder.buildRoom(player, room, door.getLocation().getZ(), roomX, roomY);
                        } else {
                            interpreter.sendPlaneMessage(false, "You need " + room.getProperties().getCost() + " coins to buy this room.");
                            stage = 2;
                            return true;
                        }
                    case 4:
                        end();
                        return true;
                }
                break;
            case 2:
                end();
                return true;
        }
        return false;
    }

    /**
     * Rotates the room.
     *
     * @param counter If we're rotating counter clockwise.
     */
    private void rotate(boolean counter) {
        Direction direction = null;
        while ((direction = directions[index = (index + (counter ? 3 : 1)) % 4]) == null) {
            log.debug("Missing direction: {}.", index);
        }
        room.setRotation(direction);
        drawBoundaries();
    }

    /**
     * Draws the current boundaries of the room to build.
     */
    private void drawBoundaries() {
        for (GameObject object : boundaries) {
            ObjectBuilder.remove(object);
        }
        boundaries.clear();
        boolean[] exit = Arrays.copyOf(exits, exits.length);
        for (int j = 0; j < exit.length; j++) {
            exit[(j + index) % 4] = exits[j];
        }
        int doorId = player.getHouseManager().getStyle().getDoorId();
        Location base = player.getViewport().getRegion().getBaseLocation().transform(roomX << 3, roomY << 3, 0);
        for (int i = 0; i < 4; i++) {
            if (exit[i]) {
                int x = BOUNDARY_OFFSETS[i][0];
                int y = BOUNDARY_OFFSETS[i][1];
                boundaries.add(ObjectBuilder.add(new GameObject(doorId + 1, base.transform(x, y, 0), 0, (i + 2) % 4)));
                if (x == 4) {
                    boundaries.add(ObjectBuilder.add(new GameObject(doorId, base.transform(3, y, 0), 0, (i + 2) % 4)));
                } else if (y == 4) {
                    boundaries.add(ObjectBuilder.add(new GameObject(doorId, base.transform(x, 3, 0), 0, (i + 2) % 4)));
                }
            }
        }
    }

    @Override
    public int[] getIds() {
        return new int[]{ DialogueInterpreter.getDialogueKey("con:room") };
    }

}
