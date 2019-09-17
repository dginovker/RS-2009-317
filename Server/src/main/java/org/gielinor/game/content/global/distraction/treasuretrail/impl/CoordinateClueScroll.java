package org.gielinor.game.content.global.distraction.treasuretrail.impl;

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
 * Represents a coordinate clue scroll.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public abstract class CoordinateClueScroll extends ClueScrollPlugin {

    /**
     * The location of the coordinates.
     */
    private final Location location;
    /**
     * The coordinate string.
     */
    private final String coordinates;
    /**
     * The id of the wizard NPC.
     */
    private int wizard;

    /**
     * Constructs a new {@code CoordinateClueScroll} {@code Object}
     *
     * @param name     The name.
     * @param clueId   The clue id.
     * @param level    The level.
     * @param location The location.
     * @param wizard   The id of the wizard NPC.
     */
    public CoordinateClueScroll(String name, int clueId, ClueLevel level, Location location, final int wizard, String coordinates, ZoneBorders... borders) {
        super(name, clueId, level, 6965, borders);
        this.location = location;
        this.wizard = wizard;
        this.coordinates = coordinates;
    }

    /**
     * Constructs a new {@code CoordinateClueScroll} {@code Object}
     *
     * @param name     The name.
     * @param clueId   The clue id.
     * @param level    The level.
     * @param location The location.
     */
    public CoordinateClueScroll(String name, int clueId, ClueLevel level, Location location, String coordinates) {
        this(name, clueId, level, location, -1, coordinates);
    }

    @Override
    public boolean interact(Entity e, Node target, Option option) {
        return super.interact(e, target, option);
    }

    @Override
    public void configure() {
        DigSpadeHandler.register(getLocation(), player -> {
            if (!player.getInventory().contains(clueId, 1)) {
                return;
            }
            // TODO Attack
            reward(player);
            player.getDialogueInterpreter().sendDoubleItemMessage(2714, -1, "You've found a casket!");
        });
        super.configure();
    }

    @Override
    public void read(Player player) {
        for (int lineId = 6968; lineId <= 6975; lineId++) {
            player.getActionSender().sendString(lineId, "");
        }
        super.read(player);
        player.getActionSender().sendString("<br><br><br><br><br>" + coordinates, 6968);
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
     * Gets the id of the wizard NPC.
     *
     * @return The id.
     */
    public int getWizard() {
        return wizard;
    }

}
