package org.gielinor.game.content.skill.member.summoning;

/**
 * Represents a summoning scroll.
 *
 * @author 'Vexia
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public enum SummoningScroll {
    HOWL_SCROLL(0, 12425, 0.1, 1, "Level 1: Howl (3 Special Move points)", "Makes NPCs flee for a short time", InterfaceType.NPCS, 12047),
    DREADFOWL_STRIKE_SCROLL(1, 12445, 0.1, 4, "Level 4: Dreadfowl Strike (3 Special Move points)", "A magical attack", InterfaceType.NPCS_PLAYERS, 12043),
    EGG_SPAWN_SCROLL(2, 12428, 0.2, 10, "Level 10: Egg Spawn (6 Special Move points)", "Spawns red spiders' eggs nearby", 12059),
    SLIME_SPRAY_SCROLL(3, 12459, 0.2, 13, "Level 13: Slime Spray (3 Special Move points)", "Damages an enemy", InterfaceType.NPCS_PLAYERS, 12019),
    STONY_SHELL_SCROLL(4, 12533, 0.2, 16, "Level 16: Stony Shell (12 Special Move points)", "Boosts your Defence by 4.", 12009),
    PESTER_SCROLL(5, 12838, 0.5, 17, "Level 17: Pester (3 Special Move points)", "Sends the spirit mosquito to attack an enemy", InterfaceType.NPCS_PLAYERS, 12778),
    ELECTRIC_LASH_SCROLL(6, 12460, 0.4, 18, "Level 18: Electric Lash (6 Special Move points)", "Mild damaging attack that causes a very short stun", InterfaceType.NPCS_PLAYERS, 12049),
    VENOM_SHOT_SCROLL(7, 12432, 0.9, 19, "Level 19: Venom Shot (6 Special Move points)", "Has a chance of making your next Ranged attack poisonous", 12055),
    FIREBALL_ASSAULT_SCROLL(8, 12839, 1.1, 22, "Level 22: Fireball Assault (6 Special Move points)", "Damages multiple targets", 12808),
    CHEESE_FEAST_SCROLL(9, 12430, 2.3, 23, "Level 23: Cheese Feast (6 Special Move points)", "Generates 4 cheese that appears in the Albino Rat's inventory.", 12067),
    SANDSTORM_SCROLL(10, 12446, 2.5, 25, "Level 25: Sandstorm (6 Special Move points)", "Deals damage to nearby opponents", 12063),
    GENERATE_COMPOST_SCROLL(11, 12440, 0.6, 28, "Level 28: Generate Compost (12 Special Move points)", "Fills an empty compost bin with compost", InterfaceType.OBJECTS, 12091),
    EXPLODE_SCROLL(12, 12834, 2.9, 29, "Level 29: Explode (3 Special Move points)", "Detonates the chinchompa", 12800),
    VAMPYRE_TOUCH_SCROLL(13, 12447, 1.5, 31, "Level 31: Vampire Touch (4 Special Move points)", "A magical attack that may also heal 2 of your Hitpoints", InterfaceType.NPCS_PLAYERS, 12053),
    INSANE_FEROCITY_SCROLL(14, 12433, 1.6, 32, "Level 32: Insane Ferocity (4 Special Move points)", "Boosts the Honey Badgers Attack and Strength, at the expense of Defence.", 12065),
    MULTICHOP_SCROLL(15, 12429, 0.7, 33, "Level 33: Multichop (6 Special Move points)", "Cuts up to 3 logs from a nearby tree", InterfaceType.OBJECTS, 12021),
    CALL_TO_ARMS_SCROLL1(16, 12443, 0.7, 34, "Level 34: Call To Arms (3 Special Move points)", "Teleports you to the Pest Control landing boat area", 12818),
    CALL_TO_ARMS_SCROLL2(17, 12443, 0.7, 34, "Level 34: Call To Arms (3 Special Move points)", "Teleports you to the Pest Control landing boat area", 12814),
    CALL_TO_ARMS_SCROLL3(18, 12443, 0.7, 34, "Level 34: Call To Arms (3 Special Move points)", "Teleports you to the Pest Control landing boat area", 12780),
    CALL_TO_ARMS_SCROLL4(19, 12443, 0.7, 34, "Level 34: Call To Arms (3 Special Move points)", "Teleports you to the Pest Control landing boat area", 12798),
    BRONZE_BULL_RUSH_SCROLL(64, 12461, 3.6, 36, "Level 36: Bronze Bull Rush (6 Special Move points)", "Damages the target with a chance to stun them; damage dealt depends on minotaur type", InterfaceType.NPCS_PLAYERS, 12073),
    UNBURDEN_SCROLL(20, 12431, 0.6, 40, "Level 40: Unburden (12 Special Move points)", "Restores run energy based on your current Agility level", 12087),
    HERBCALL_SCROLL(21, 12422, 0.8, 41, "Level 41: Herbcall (12 Special Move points)", "Has a chance to produce a grimy herb", 12071),
    EVIL_FLAMES_SCROLL(22, 12448, 2.1, 42, "Level 42: Evil Flames (6 Special Move points)", "Causes Magic-based damage and drains target's Ranged", InterfaceType.NPCS_PLAYERS, 12051),
    PETRIFYING_GAZE_SCROLL1(23, 12458, 0.9, 43, "Level 43: Petrifying Gaze (3 Special Move points)", "Damages a target and causes combat stat damage, unless target is wielding a mirror shield", InterfaceType.NPCS_PLAYERS, 12095),
    PETRIFYING_GAZE_SCROLL2(24, 12458, 0.9, 43, "Level 43: Petrifying Gaze (3 Special Move points)", "Damages a target and causes combat stat damage, unless target is wielding a mirror shield", InterfaceType.NPCS_PLAYERS, 12097),
    PETRIFYING_GAZE_SCROLL3(25, 12458, 0.9, 43, "Level 43: Petrifying Gaze (3 Special Move points)", "Damages a target and causes combat stat damage, unless target is wielding a mirror shield", InterfaceType.NPCS_PLAYERS, 12099),
    PETRIFYING_GAZE_SCROLL4(26, 12458, 0.9, 43, "Level 43: Petrifying Gaze (3 Special Move points)", "Damages a target and causes combat stat damage, unless target is wielding a mirror shield", InterfaceType.NPCS_PLAYERS, 12101),
    PETRIFYING_GAZE_SCROLL5(27, 12458, 0.9, 43, "Level 43: Petrifying Gaze (3 Special Move points)", "Damages a target and causes combat stat damage, unless target is wielding a mirror shield", InterfaceType.NPCS_PLAYERS, 12103),
    PETRIFYING_GAZE_SCROLL6(28, 12458, 0.9, 43, "Level 43: Petrifying Gaze (3 Special Move points)", "Damages a target and causes combat stat damage, unless target is wielding a mirror shield", InterfaceType.NPCS_PLAYERS, 12105),
    PETRIFYING_GAZE_SCROLL7(29, 12458, 0.9, 43, "Level 43: Petrifying Gaze (3 Special Move points)", "Damages a target and causes combat stat damage, unless target is wielding a mirror shield", InterfaceType.NPCS_PLAYERS, 12107),
    IRON_BULL_RUSH_SCROLL(65, 12462, 4.6, 46, "Level 46: Iron Bull Rush (6 Special Move points)", "Damages the target with a chance to stun them; damage dealt depends on minotaur type", InterfaceType.NPCS_PLAYERS, 12075),
    IMMENSE_HEAT_SCROLL(30, 12829, 2.3, 46, "Level 46: Immense Heat (6 Special Move points)", "Smelts a gold bar into an item of jewellery without need of a furnace", InterfaceType.ITEMS, 12816),
    THIEVING_FINGERS_SCROLL(31, 12426, 0.9, 47, "Level 47: Thieving Fingers (3 Special Move points)", "Boosts your Thieving", 12041),
    BLOOD_DRAIN_SCROLL(32, 12444, 2.4, 49, "Level 49: Blood Drain (6 Special Move points)", "Heals stat damage, poison and disease at the cost of Hitpoints.", 12061),
    TIRELESS_RUN_SCROLL(33, 12441, 0.8, 52, "Level 52: Tireless Run (8 Special Move points)", "Boosts your Agility and restores run energy", 12007),
    ABYSSAL_DRAIN_SCROLL(34, 12454, 1.1, 54, "Level 54: Abyssal Drain (1 Special Move points)", "Causes Magic-based damage and drains target's Magic", InterfaceType.NPCS_PLAYERS, 12035),
    DISSOLVE_SCROLL(35, 12453, 5.5, 55, "Level 55: Dissolve (6 Special Move points)", "Causes Magic-based damage and drains target's Attack", InterfaceType.NPCS_PLAYERS, 12027),
    STEEL_BULL_RUSH_SCROLL(66, 12463, 5.6, 56, "Level 56: Steel Bull Rush (6 Special Move points)", "Damages the target with a chance to stun them; damage dealt depends on minotaur type", InterfaceType.NPCS_PLAYERS, 12077),
    FISH_RAIN_SCROLL(36, 12424, 1.1, 56, "Level 56: Fish Rain (12 Special Move points)", "Makes fish appear nearby", 12531),
    AMBUSH_SCROLL(37, 12836, 5.7, 57, "Level 57: Ambush (3 Special Move points)", "Brings your spirit kyatt to attack an enemy you are fighting.", 12812),
    RENDING_SCROLL(38, 12840, 5.7, 57, "Level 57: Rending (6 Special Move points)", "Causes Magic-based damage and drains target's Strength", InterfaceType.NPCS_PLAYERS, 12784),
    GOAD_SCROLL(39, 12835, 5.7, 57, "Level 57: Goad (3 Special Move points)", "Sends your spirit graahk to attack an enemy", 12810),
    DOOMSPHERE_SCROLL(40, 12455, 5.8, 58, "Level 58: Doomsphere Device (3 Special Move points)", "", -1),
    // TODO
    DUST_CLOUD_SCROLL(41, 12468, 3.0, 61, "Level 61: Dust Cloud (6 Special Move points)", "Affects the area around you, damaging all enemies", 12085),
    ABYSSAL_STEALTH_SCROLL(42, 12427, 1.9, 62, "Level 62: Abyssal Stealth (3 Special Move points)", "Boosts your Thieving and Agility", 12037),
    OPHIDIAN_INCUBATION_SCROLL(43, 12436, 3.1, 63, "Level 63: Ophidian Incubation (3 Special Move points)", "Turns a bird egg into a cockatrice egg", InterfaceType.ITEMS, 12015),
    POISONOUS_BLAST_SCROLL(44, 12467, 3.2, 64, "Level 64: Poisonous Blast (6 Special Move points)", "A poison-inducing attack", 12045),
    MITHRIL_BULL_RUSH_SCROLL(67, 12464, 6.6, 66, "Level 66: Mithril Bull Rush (6 Special Move points)", "Damages the target with a chance to stun them; damage dealt depends on minotaur type", InterfaceType.NPCS_PLAYERS, 12079),
    TOAD_BARK_SCROLL(45, 12452, 1.0, 66, "Level 66: Toad Bark (6 Special Move points)", "Damages a target", 12123),
    TESTUDO_SCROLL(46, 12439, 0.7, 67, "Level 67: Testudo (20 Special Move points)", "Boosts your Defence by 8.", 12031),
    SWALLOW_WHOLE_SCROLL(47, 12438, 1.4, 68, "Level 68: Swallow Whole (3 Special Move points)", "Allows you to eat a fish without having to cook it", 12029),
    FRUITFALL_SCROLL(48, 12423, 1.4, 69, "Level 69: Fruitfall (6 Special Move points)", "Produces random fruit nearby", 12033),
    FAMINE_SCROLL(49, 12830, 1.5, 70, "Level 70: Famine (12 Special Move points)", "Destroys target player's food", 12820),
    ARCTIC_BLAST_SCROLL(50, 12451, 1.1, 71, "Level 71: Arctic Blast (6 Special Move points)", "Magical attack that can stun the target", 12057),
    //RISE_FROM_THE_ASHES_SCROLL(51, 14622, 8, 277, "", "", -1),
    VOLCANIC_STRENGTH_SCROLL(51, -1, 7.3, 73, "Level 73: Volcanic Strength (12 Special Move points)", "Gives a 9 point Strength boost", 12792),
    CRUSHING_CLAW_SCROLL(52, 12449, 3.7, 74, "Level 74: Crushing Claw (6 Special Move points)", "Causes Magic-based damage and drains target's Defence", 12069),
    MANTIS_STRIKE_SCROLL(53, 12450, 3.7, 75, "Level 75: Mantis Strike (6 Special Move points)", "Causes Magic-based damage, drains other player's Prayer and binds", 12011),
    INFERNO_SCROLL(54, 12841, 1.5, 76, "Level 76: Inferno (6 Special Move points)", "Damaging magical attack that can disarm the target's weapon or shield", 12782),
    ADAMANT_BULL_RUSH_SCROLL(68, -1, 7.6, 76, "Level 76: Adamant Bull Rush (6 Special Move points)", "Damages the target with a chance to stun them; damage dealt depends on minotaur type", InterfaceType.NPCS_PLAYERS, 12081),
    DEADLY_CLAW_SCROLL(55, 12831, 11, 77, "Level 77: Deadly Claw (6 Special Move points)", "Three magical attacks at once", 12162),
    // TODO
    ACORN_MISSILE_SCROLL(56, 12457, 1.6, 78, "Level 78: Acorn Missile (6 Special Move points)", "Inflicts up to 11 damage on up to 3 opponents. Chance of acorn being dropped.", 12013),
    TITANS_CONSTITUTION_SCROLL1(57, 12824, 7.9, 79, "Level 79: Titan's Constitution (20 Special Move points)", "Boosts your Defence and Hitpoints significantly", 12802),
    TITANS_CONSTITUTION_SCROLL2(58, 12824, 7.9, 79, "Level 79: Titan's Constitution (20 Special Move points)", "Boosts your Defence and Hitpoints significantly", 12806),
    TITANS_CONSTITUTION_SCROLL3(59, 12824, 7.9, 79, "Level 79: Titan's Constitution (20 Special Move points)", "Boosts your Defence and Hitpoints significantly", 12804),
    REGROWTH_SCROLL(60, 12442, 1.6, 80, "Level 80: Regrowth (6 Special Move points)", "Will cause a farmed tree that has been chopped down to regrow faster.", 12025),
    SPIKE_SHOT_SCROLL(61, 12456, 4.1, 83, "Level 83: Spike Shot (6 Special Move points)", "Damaging strike that stuns the target", 12017),
    EBON_THUNDER_SCROLL(62, 12837, 8.3, 83, "Level 83: Ebon Thunder (4 Special Move points)", "Magical attack that also drains points from the target's Special Attack bar", 12788),
    SWAMP_PLAGUE_SCROLL(63, 12832, 4.1, 85, "Level 85: Swamp Plague (6 Special Move points)", "Area-effect, magical poison attack", 12776),
    RUNE_BULL_RUSH_SCROLL(69, 12466, 8.6, 86, "", "", 12083),
    // TODO
    HEALING_AURA_SCROLL(70, 12434, 1.8, 88, "Level 88: Healing Aura (20\\nSpecial Move points)", "Heals 15% of your Hitpoints.", 12039),
    BOIL_SCROLL(71, 12833, 8.9, 89, "Level 89: Boil (6 Special Move points)", "Damages a player, with extra damage based on how much armour they are wearing", 12786),
    MAGIC_FOCUS_SCROLL(72, 12437, 4.6, 92, "Level 92: Magic Focus (10 Special Move points)", "Boosts your Magic by 7.", 12089),
    ESSENCE_SHIPMENT_SCROLL(73, 12827, 1.9, 93, "Level 93: Essence Shipment (10 Special Move points)", "Transports all the pure essence from your inventory and all pure essence held by the Abyssal Titan to your bank", InterfaceType.NPCS_PLAYERS, 12796),
    IRON_WITHIN_SCROLL(74, 12828, 4.7, 95, "Level 95: Iron Within (12 Special Move points)", "Makes the Iron Titan strike 3 times on it's next attack.", 12822),
    WINTER_STORAGE_SCROLL(75, 12435, 4.8, 96, "Level 96: Winter Storage (12 Special Move points)", "Banks one item from your inventory", InterfaceType.ITEMS, 12093),
    STEEL_OF_LEGENDS_SCROLL(76, 12825, 4.9, 99, "Level 99: Steel of Legends (12 Special Move points)", "Makes the Steel Titan's next attack 4 attacks at once.", 12790),;

    /**
     * The level required.
     */
    private int level;

    /**
     * The item id needed.
     */
    private int itemId;

    /**
     * The slot id.
     */
    private int slotId;

    /**
     * The xp gained.
     */
    private double xp;

    /**
     * The full name.
     */
    private String fullName;

    /**
     * The description.
     */
    private String description;

    /**
     * The items required.
     */
    private int[] items;

    /**
     * The {@link org.gielinor.game.content.skill.member.summoning.InterfaceType}.
     */
    private final InterfaceType interfaceType;

    /**
     * Creates a new <code>SummoningScroll</code>.
     *
     * @param slotId        The id of the slot.
     * @param itemId        The scroll item id.
     * @param xp            The experience given for casting.
     * @param level         The level required to cast.
     * @param fullName      The full name.
     * @param description   The description.
     * @param interfaceType The {@link org.gielinor.game.content.skill.member.summoning.InterfaceType}.
     * @param items         The items required to make this scroll.
     */
    SummoningScroll(int slotId, int itemId, double xp, int level, String fullName, String description, InterfaceType interfaceType, int... items) {
        this.level = level;
        this.itemId = itemId;
        this.xp = xp;
        this.slotId = slotId;
        this.fullName = fullName;
        this.description = description;
        this.interfaceType = interfaceType;
        this.items = items;
    }

    /**
     * Creates a new <code>SummoningScroll</code>.
     *
     * @param slotId      The id of the slot.
     * @param itemId      The scroll item id.
     * @param xp          The experience given for casting.
     * @param level       The level required to cast.
     * @param fullName    The full name.
     * @param description The description.
     * @param items       The items required to make this scroll.
     */
    SummoningScroll(int slotId, int itemId, double xp, int level, String fullName, String description, int... items) {
        this.level = level;
        this.itemId = itemId;
        this.xp = xp;
        this.slotId = slotId;
        this.fullName = fullName;
        this.description = description;
        this.interfaceType = InterfaceType.CLICK;
        this.items = items;
    }

    /**
     * Gets the level.
     *
     * @return The level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the itemId.
     *
     * @return The itemId.
     */
    public int getItemId() {
        return itemId;
    }

    /**
     * Gets the slotId.
     *
     * @return The slotId.
     */
    public int getSlotId() {
        return slotId;
    }

    /**
     * Gets the xp.
     *
     * @return The xp.
     */
    public double getExperience() {
        return xp;
    }

    /**
     * Gets the items.
     *
     * @return The items.
     */
    public int[] getItems() {
        return items;
    }

    /**
     * Gets the summoning value for the id.
     *
     * @param id
     * @return
     */
    public static SummoningScroll forId(int id) {
        for (SummoningScroll scroll : SummoningScroll.values()) {
            if (scroll.slotId == id) {
                return scroll;
            }
        }
        return null;
    }

    /**
     * Gets the scroll for the given pouch item id.
     *
     * @param pouchId The pouch item id.
     * @return The scroll.
     */
    public static SummoningScroll forPouch(int pouchId) {
        for (SummoningScroll scroll : SummoningScroll.values()) {
            if (scroll.items[0] == pouchId) {
                return scroll;
            }
        }
        return null;
    }

    /**
     * The full name.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the {@link org.gielinor.game.content.skill.member.summoning.InterfaceType}.
     *
     * @return The interface type.
     */
    public InterfaceType getInterfaceType() {
        return interfaceType;
    }
}
