package plugin.ttrail;

import org.gielinor.game.content.global.distraction.treasuretrail.ClueLevel;
import org.gielinor.game.content.global.distraction.treasuretrail.impl.MapClueScroll;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Initializes the map clues.
 *
 * @author Vexia
 */
public final class MapCluePlugin extends MapClueScroll {

    /**
     * Constructs a new {@code MapCluePlugin} {@code Object}
     */
    public MapCluePlugin() {
        this(null, -1, null, -1, null);
    }

    /**
     * Constructs a new {@code MapCluePlugin} {@code Object}
     *
     * @param name        the name.
     * @param clueId      the clue id.
     * @param level       the level.
     * @param interfaceId the interface id.
     * @param location    the location.
     */
    public MapCluePlugin(String name, int clueId, ClueLevel level, int interfaceId, Location location) {
        super(name, clueId, level, interfaceId, location);
    }

    /**
     * Constructs a new {@code MapCluePlugin} {@code Object}
     *
     * @param name        the name.
     * @param clueId      the clue id.
     * @param level       the level.
     * @param interfaceId the interface id.
     * @param location    the location.
     * @param object      the object.
     * @param borders     the borders.
     */
    public MapCluePlugin(String name, int clueId, ClueLevel level, int interfaceId, Location location, int object, ZoneBorders... borders) {
        super(name, clueId, level, interfaceId, location, object, borders);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        register(new MapCluePlugin("mc-grubber-crate", 2743, ClueLevel.MEDIUM, 9196, Location.create(2658, 3488, 0), 357, new ZoneBorders(2657, 3487, 2660, 3489)));
        register(new MapCluePlugin("ardy-crate", 2747, ClueLevel.MEDIUM, 9720, Location.create(2565, 3248, 0), 354, new ZoneBorders(2564, 3247, 2566, 3249)));
        register(new MapCluePlugin("wildy-crate", 2773, ClueLevel.HARD, 9507, Location.create(3026, 3628, 0), 354, new ZoneBorders(3025, 3627, 3027, 3629)));
        register(new MapCluePlugin("goblin-crate", 2774, ClueLevel.HARD, 9454, Location.create(2460, 3179, 0), 356, new ZoneBorders(2458, 3178, 2461, 3180)));
        register(new MapCluePlugin("miscellania-dig", 2776, ClueLevel.MEDIUM, 17687, Location.create(2536, 3865, 0)));
        register(new MapCluePlugin("crafting-guild-dig", 2778, ClueLevel.MEDIUM, 4305, Location.create(2906, 3294, 0)));
        register(new MapCluePlugin("wizard-tower-dig", 2745, ClueLevel.EASY, 9275, Location.create(3110, 3152, 0)));
        register(new MapCluePlugin("east-dig", 2805, ClueLevel.EASY, 9632, Location.create(2651, 3231, 0)));
        register(new MapCluePlugin("rimmington", 2780, ClueLevel.MEDIUM, 9839, Location.create(2924, 3209, 0)));
        register(new MapCluePlugin("west-ardy", 2782, ClueLevel.HARD, 9359, Location.create(2488, 3308, 0)));
        register(new MapCluePlugin("yanille", 2783, ClueLevel.HARD, 9043, Location.create(2616, 3077, 0)));
        register(new MapCluePlugin("mcgrubber", 2785, ClueLevel.EASY, 9108, Location.create(2611, 3481, 0)));
        register(new MapCluePlugin("falador-stone", 2786, ClueLevel.EASY, 7271, Location.create(3043, 3399, 0)));
        register(new MapCluePlugin("champion's-guild", 2788, ClueLevel.EASY, 6994, Location.create(3166, 3360, 0)));
        register(new MapCluePlugin("falador-statue", 2790, ClueLevel.EASY, 17537, Location.create(2970, 3415, 0)));
        register(new MapCluePlugin("varrock-mine", 2792, ClueLevel.EASY, 7045, Location.create(3289, 3373, 0)));
        register(new MapCluePlugin("khazard-altar", 2793, ClueLevel.MEDIUM, 17888, Location.create(2455, 3230, 0)));
        register(new MapCluePlugin("light-house", 2794, ClueLevel.MEDIUM, 17907, Location.create(2578, 3597, 0)));
        register(new MapCluePlugin("north-seers", 2796, ClueLevel.MEDIUM, 18055, Location.create(2666, 3562, 0)));
        register(new MapCluePlugin("legends-guild", 2797, ClueLevel.HARD, 17634, Location.create(2723, 3338, 0)));
        register(new MapCluePlugin("wilderness", 2799, ClueLevel.HARD, 17620, Location.create(3021, 3911, 0)));
        register(new MapCluePlugin("draynor", 2801, ClueLevel.MEDIUM, 7113, Location.create(3092, 3227, 0)));
        register(new MapCluePlugin("abandoned-mine", 2803, ClueLevel.MEDIUM, 17774, Location.create(3434, 3266, 0)));
        return this;
    }


}
