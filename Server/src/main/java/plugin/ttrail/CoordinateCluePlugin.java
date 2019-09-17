package plugin.ttrail;

import org.gielinor.game.content.global.distraction.treasuretrail.ClueLevel;
import org.gielinor.game.content.global.distraction.treasuretrail.impl.CoordinateClueScroll;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents a coordinate map clue scroll.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class CoordinateCluePlugin extends CoordinateClueScroll {

    /**
     * Constructs a new {@code CoordinateCluePlugin} {@code Object}
     */
    public CoordinateCluePlugin() {
        this(null, -1, null, null, null);
    }

    /**
     * Constructs a new {@code CoordinateClueScroll} {@code Object}
     *
     * @param name     The name.
     * @param clueId   The clue id.
     * @param level    The level.
     * @param location The location.
     */
    public CoordinateCluePlugin(String name, int clueId, ClueLevel level, Location location, String coordinates) {
        super(name, clueId, level, location, coordinates);
    }

    /**
     * Constructs a new {@code CoordinateClueScroll} {@code Object}
     *
     * @param name     The name.
     * @param clueId   The clue id.
     * @param level    The level.
     * @param location The location.
     * @param wizard   The id of the wizard NPC.
     */
    public CoordinateCluePlugin(String name, int clueId, ClueLevel level, Location location, int wizard, String coordinates, ZoneBorders... borders) {
        super(name, clueId, level, location, wizard, coordinates, borders);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        //(String name, int clueId, ClueLevel level,  Location location, String coordinates)
        register(new CoordinateCluePlugin("cc-tree-gnome-west", 2809, ClueLevel.MEDIUM, Location.create(2478, 3158, 0), "00 degrees 05 minutes south<br>01 degrees 13 minutes east"));
        return this;
    }


}
