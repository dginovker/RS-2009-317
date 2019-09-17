package org.gielinor.game.content.skill.free.magic;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;

/**
 * Represents a teleport location.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public enum TeleportLocation {
    TRAINING(true, new String[]{
        "Home",
        "Low Level", "Medium Level", "High Level",
        "Cave Horrors", "Slayer Tower", "Rellekka Slayer",
        "Smoke Dungeon", "Brine Rats"
    },
        new Location(2848, 5099, 0, 2),
        new Location(2149, 5097, 0, 2), new Location(3555, 9945, 0, 2),
        new Location(2740, 5084, 0, 3), new Location(3751, 2973, 0, 1),
        new Location(3482, 3477, 0), new Location(2663, 3644, 0),
        new Location(3357, 2967, 0), new Location(2696, 10121, 0, 2)
    ),
    MONSTERS(true, new String[]{
        "Dragons", "Taverley Dungeon", "Ancient Cavern",
        "Waterfall Dungeon", "Ape Atoll Temple", "Tai Bwo Wannai",
        "Catacombs of Kourend",

    },
        new Location(2710, 9466, 0, 2), new Location(2884, 9798, 0),
        new Location(1772, 5366, 0), new Location(2576, 9876, 0),
        new Location(2784, 2786, 0), new Location(2798, 3083, 0),
        new Location(1665, 10046, 0)
    ),
    MINIGAMES(true, new String[]{
        "Castle wars", "Barrows",
        "Games room", "Duel arena", "Pest control",
        "Tzhaar fight cave", "Tzhaar fight pit", "Warriors guild"
    },
        new Location(2442, 3089, 0, 3), new Location(3566, 3316, 0),
        new Location(2208, 4939, 0, 1), new Location(3317, 3234, 0, 5), new Location(2659, 2676, 0),
        new Location(2439, 5171, 0, 3), new Location(2399, 5178, 0, 1), new Location(2882, 3547, 0, 4)
    ),
    BOSSING(true, new String[]{
        "God Wars", "Dagannoth Kings", "King Black Dragon",
        "Chaos Elemental", "Kalphite Queen", "Corporeal Beast"
    },
        new String[]{
            "<col=8A0808>Warning!<br>The location you are attempting to teleport to<br>is considered dangerous!<br>Are you sure you wish to teleport there?",
            null,
            null,
            null,
            "<col=8A0808>Warning!<br>The location you are attempting to teleport to<br>is considered dangerous!<br>Are you sure you wish to teleport there?",
            null
        },
        new Location(2882, 5311, 2), new Location(1910, 4367, 0),
        new Location(3067, 10253, 0), new Location(3285, 3922, 0),
        new Location(3508, 9493, 0), new Location(2917, 4383, 0)
    ),
    WILDERNESS(true, new String[]{
        "Edgeville",
        "Mage Bank",
        "East Dragons",
        "Bone Yard"
    },
        new Location(3088, 3519, 0), new Location(2539, 4716, 0),
        new Location(3348, 3661, 0), new Location(3231, 3740, 0)
    ),
    SKILLING(true, new String[]{
        "Agility",
        "Farming (Catherby)", "Farming (Falador)",
        "Farming (Canifis)", "Mining & Smithing",
        "Crafting Guild", "Fishing",
        "Woodcutting",
        "Thieving"
    },
        new Location(-1, -1, -1),
        new Location(2816, 3463, 0, 1), new Location(3056, 3310, 0, 1),
        new Location(3607, 3532, 0, 1), new Location(3021, 9739, 0, 2),
        new Location(2933, 3294, 0, 1), new Location(2604, 3402, 0, 2),
        new Location(2722, 3470, 0, 1),
        new Location(-1, -1, -1)

    ),
    CITIES(true, new String[]{
        "Lumbridge", "Al Kharid", "Varrock",
        "Edgeville", "Canafis", "Draynor Village",
        "Falador", "Taverley", "Burthorpe",
        "Camelot", "East Ardougne", "West Ardougne",
        // below don't fit in interface.
        "Yanille", "Relekka"
    },
        new Location(3234, 3219, 0), new Location(3293, 3183, 0), new Location(3212, 3428, 0),
        new Location(3087, 3490, 0), new Location(3496, 3489, 0), new Location(3094, 3248, 0),
        new Location(2965, 3380, 0), new Location(2893, 3466, 0), new Location(2897, 3538, 0),
        new Location(2727, 3484, 0), new Location(2660, 3303, 0), new Location(2531, 3306, 0),
        // below don't fit in interface.
        new Location(2605, 3091, 0), new Location(2658, 3659, 0)

        ),
    DONOR(true, new String[]{
        "Donor Zone", "Catacombs"
    },
        new Location(2330, 3171, 0, 2), new Location(1640, 10047, 0, 2)) {
        @Override
        public boolean canTeleport(Player player, int index) {
            switch (index) {
                case 0:
                    if (!player.getDonorManager().hasMembership()) {
                        player.getActionSender().sendMessage("You must be a member to teleport here.");
                        return false;
                    }
                    return true;
            }
            return false;
        }
    },
    THIEVING(false, new String[]{
        "Ardougne", "Rogues Den"
    },
        new Location(2664, 3305, 0, 3),
        new Location(3044, 4972, 1)
    ),
    AGILITY(false, new String[]{
        "Gnome Stronghold", "Barbarian Outpost",
        "Agility Pyramid", "Brimhaven",
        "Wilderness"
    },
        new Location(2469, 3435, 0, 2), new Location(2543, 3570, 0, 2),
        new Location(3354, 2827, 0, 1), new Location(2809, 3192, 0, 2),
        new Location(2998, 3910, 0, 3)
    ),
    AL_KAHRID(Location.create(3301, 3203, 0)),
    LUMBRIDGE(Location.create(3222, 3218, 0)),
    LUMBRIDGE_SWAMP(Location.create(3220, 3163, 0)),
    DRAYNOR(Location.create(3080, 3250, 0)),
    PORT_SARIM(Location.create(3022, 3242, 0)),
    RIGNMINTON(Location.create(2956, 3224, 0)),
    WEST_FALLADOR(Location.create(3038, 3356, 0)),
    EAST_VARROCK(Location.create(2966, 3380, 0)),
    EDGEVIILE(Location.create(3086, 3502, 0)),
    GRAND_EXCHANGE(Location.create(3164, 3472, 0)),
    TAVERLLY(Location.create(2919, 3437, 0)),
    WHITE_WOLF_MOUNTAIN(Location.create(2864, 3448, 0)),
    CAMELOT(Location.create(2757, 3477, 0)),
    FLAX(Location.create(2742, 3443, 0)),
    BARBARIAN_VILLAGE(Location.create(3083, 3418, 0)),
    PARTY_ROOM(Location.create(3046, 3374, 0)),
    SEERS_VILLAGE(Location.create(2694, 3486, 0)),
    EAST_ADROUGONE(Location.create(2662, 3307, 0)),
    WEST_ADROUGONE(Location.create(2608, 3295, 0)),
    GNOME_STRONGHOLD(Location.create(2458, 3418, 0)),
    WIZARD_WATCH_TOWER(Location.create(3114, 3209, 0)),;

    boolean randomize;
    private final String[] names;
    private final Location[] locations;
    private final String[] warningMessages;

    TeleportLocation(boolean randomize, String[] names, String[] warningMessages, Location... locations) {
        this.randomize = randomize;
        this.names = names;
        this.locations = locations;
        this.warningMessages = warningMessages;
    }

    TeleportLocation(boolean randomize, String[] names, Location... locations) {
        this(randomize, names, null, locations);
    }

    TeleportLocation(Location location) {
        this(false, null, new String[0], location);
    }

    /**
     * Gets the teleport names.
     *
     * @return The names.
     */
    public String[] getNames() {
        return names;
    }

    /**
     * Gets the teleport locations.
     *
     * @return The locations.
     */
    public Location[] getLocations() {
        return locations;
    }

    /**
     * Gets the first teleport location.
     *
     * @return The location.
     */
    public Location getLocation() {
        return locations[0];
    }

    /**
     * Whether or not to randomize the landing location.
     *
     * @return <code>True</code> if so.
     */
    public boolean randomize() {
        return randomize;
    }

    /**
     * Gets the warning messages.
     *
     * @return The messages.
     */
    public String[] getWarningMessages() {
        return warningMessages;
    }

    /**
     * Checks if the player can teleport to this area.
     *
     * @param player The player.
     * @param index  The index of the area button.
     * @return <code>True</code> if so.
     */
    public boolean canTeleport(Player player, int index) {
        return true;
    }
}
