package org.gielinor.game.content.global.distraction.treasuretrail.impl;

import org.gielinor.game.content.global.action.DigAction;
import org.gielinor.game.content.global.action.DigSpadeHandler;
import org.gielinor.game.content.global.distraction.treasuretrail.ClueLevel;
import org.gielinor.game.content.global.distraction.treasuretrail.ClueScrollPlugin;
import org.gielinor.game.interaction.Option;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.zone.ZoneBorders;

/**
 * A map clue scroll.
 *
 * @author Vexia
 */
public abstract class MapClueScroll extends ClueScrollPlugin {

    /**
     * The location of the x spot.
     */
    private final Location location;

    /**
     * The object id.
     */
    private final int object;

    /**
     * Constructs a new {@Code MapClueScroll} {@Code Object}
     *
     * @param name        the name.
     * @param clueId      the clue id.
     * @param level       the level.
     * @param interfaceId the interface id.
     * @param location    the location.
     * @param object      the object.
     */
    public MapClueScroll(String name, int clueId, ClueLevel level, int interfaceId, Location location, final int object, ZoneBorders... borders) {
        super(name, clueId, level, interfaceId, borders);
        this.location = location;
        this.object = object;
    }

    /**
     * Constructs a new {@Code MapClueScroll} {@Code Object}
     *
     * @param name        the name.
     * @param clueId      the clue id.
     * @param level       the level.
     * @param interfaceId the interface id.
     * @param location    the location.
     */
    public MapClueScroll(String name, int clueId, ClueLevel level, int interfaceId, Location location) {
        this(name, clueId, level, interfaceId, location, 0);
    }

    @Override
    public boolean interact(Entity e, Node target, Option option) {
        if (e instanceof Player) {
            Player p = e.asPlayer();
            if (target.getId() == object && option.getName().equals("Search")) {
                if (!p.getInventory().contains(clueId, 1) || !target.getLocation().equals(location)) {
                    p.getActionSender().sendMessage("You dig but find nothing.");
                    return false;
                }
                reward(p, false);
                level.open(p, null);
                return true;
            }
        }
        return super.interact(e, target, option);
    }

    @Override
    public void configure() {
        DigSpadeHandler.register(getLocation(), new MapDigAction());
        super.configure();
    }

    /**
     * Handles the map digging action.
     *
     * @author Vexia
     */
    public final class MapDigAction implements DigAction {

        @Override
        public void run(Player player) {
            if (!player.getInventory().contains(clueId, 1)) {
                return;
            }
            reward(player);
            player.getDialogueInterpreter().sendDoubleItemMessage(2714, -1, "You've found a casket!");
        }


    }

    /**
     * Gets the blocation.
     *
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Gets the bobject.
     *
     * @return the object
     */
    public int getObject() {
        return object;
    }

}
