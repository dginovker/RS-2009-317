package org.gielinor.game.node.entity.player.link.prayer;

import java.util.List;

import org.gielinor.game.content.skill.SkillBonus;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.utilities.string.TextUtils;

import plugin.activity.duelarena.DuelRule;

/**
 * Represents a prayer type.
 *
 * @author 'Vexia
 * @author Emperor
 */
public enum PrayerType {
    THICK_SKIN(1, 12, 83, 5609, 17202, PrayerCategory.BABY_BLUE, 10000, new SkillBonus(Skills.DEFENCE, 0.05)),
    BURST_OF_STRENGTH(4, 12, 84, 5610, 17203, PrayerCategory.GREEN, 2689, new SkillBonus(Skills.STRENGTH, 0.05)),
    CLARITY_OF_THOUGHT(7, 12, 85, 5611, 17204, PrayerCategory.PINK, 2664, new SkillBonus(Skills.ATTACK, 0.05)),
    SHARP_EYE(8, 12, 700, 19812, 17205, PrayerCategory.LIME_GREEN, 2685, new SkillBonus(Skills.RANGE, 0.05)),
    MYSTIC_WILL(9, 12, 701, 19814, 17206, PrayerCategory.LIME_GREEN, 2670, new SkillBonus(Skills.MAGIC, 0.05)),
    ROCK_SKIN(10, 6, 86, 5612, 17207, PrayerCategory.BABY_BLUE, 2684, new SkillBonus(Skills.DEFENCE, 0.1)),
    SUPERHUMAN_STRENGTH(13, 6, 87, 5613, 17208, PrayerCategory.GREEN, 2689, new SkillBonus(Skills.STRENGTH, 0.1)),
    IMPROVED_REFLEXES(16, 6, 88, 5614, 17209, PrayerCategory.PINK, 2662, new SkillBonus(Skills.ATTACK, 0.1)),
    RAPID_RESTORE(19, 26, 89, 5615, 17210, PrayerCategory.PURPLE, 2679),
    RAPID_HEAL(22, 18, 90, 5616, 17211, PrayerCategory.PURPLE, 2678),
    PROTECT_ITEMS(25, 18, 91, 5617, 17212, PrayerCategory.DARK_GREEN, 1982),
    HAWK_EYE(26, 6, 702, 19816, 17213, PrayerCategory.LIME_GREEN, 2666, new SkillBonus(Skills.RANGE, 0.1)),
    MYSTIC_LORE(27, 6, 703, 19818, 17214, PrayerCategory.LIME_GREEN, 2668, new SkillBonus(Skills.MAGIC, 0.1)),
    STEEL_SKIN(28, 3, 92, 5618, 17215, PrayerCategory.BABY_BLUE, 2687, new SkillBonus(Skills.DEFENCE, 0.15)),
    ULTIMATE_STRENGTH(31, 3, 93, 5619, 17216, PrayerCategory.GREEN, 2691, new SkillBonus(Skills.STRENGTH, 0.15)),
    INCREDIBLE_REFLEXES(34, 3, 94, 5620, 17217, PrayerCategory.PINK, 2667, new SkillBonus(Skills.ATTACK, 0.15)),
    PROTECT_FROM_MAGIC(37, 3, 95, 5621, 17218, PrayerCategory.LIGHT_BROWN, 2675),
    PROTECT_FROM_MISSILES(40, 3, 96, 5622, 17219, PrayerCategory.LIGHT_BROWN, 2677),
    PROTECT_FROM_MELEE(43, 4, 97, 5623, 17220, PrayerCategory.LIGHT_BROWN, 2676),
    EAGLE_EYE(44, 3, 704, 19821, 17221, PrayerCategory.LIME_GREEN, 2666, new SkillBonus(Skills.RANGE, 0.15)),
    MYSTIC_MIGHT(45, 3, 705, 19823, 17222, PrayerCategory.LIME_GREEN, 2669, new SkillBonus(Skills.MAGIC, 0.15)),
    RETRIBUTION(46, 12, 98, 683, 17223, PrayerCategory.LIGHT_BROWN, PrayerCategory.MAGENTA, new Audio(10000), false),
    REDEMPTION(49, 6, 99, 684, 17224, PrayerCategory.LIGHT_BROWN, PrayerCategory.MAGENTA, new Audio(2678), false),
    SMITE(52, 2, 100, 685, 17224, PrayerCategory.LIGHT_BROWN, PrayerCategory.MAGENTA, new Audio(2685), false),
    CHIVALRY(60, 2, 706, 19825, 17226, PrayerCategory.PINK, 1000, new SkillBonus(Skills.DEFENCE, 0.2), new SkillBonus(Skills.STRENGTH, 0.18), new SkillBonus(Skills.ATTACK, 0.15)),
    PIETY(70, 2, 707, 19827, 17227, PrayerCategory.PINK, 1000, new SkillBonus(Skills.DEFENCE, 0.25), new SkillBonus(Skills.STRENGTH, 0.23), new SkillBonus(Skills.ATTACK, 0.2)),
    /**
     * Ancient curses.
     */
    CURSE_PROTECT_ITEMS(50, 3, 724, 21357, -1, PrayerCategory.GREEN, 1982, true),
    SAP_WARRIOR(50, 3, 725, 21359, -1, PrayerCategory.PINK, 2689, true),
    SAP_RANGER(52, 3, 726, 21361, -1, PrayerCategory.LIME_GREEN, 2664, true),
    SAP_MAGE(54, 3, 727, 21363, -1, PrayerCategory.BABY_BLUE, 2685, true),
    SAP_SPIRIT(56, 3, 728, 21365, -1, PrayerCategory.PURPLE, 2670, true),
    BERSERKER(59, 6, 729, 21367, -1, PrayerCategory.ORANGE, 2684, true),
    DEFLECT_SUMMONING(62, 6, 730, 21369, -1, PrayerCategory.DARK_BROWN, PrayerCategory.RED, new Audio(2689), true),
    DEFLECT_MAGIC(65, 6, 731, 21371, -1, PrayerCategory.LIGHT_BROWN, 2662, true),
    DEFLECT_MISSILES(68, 6, 732, 21373, -1, PrayerCategory.LIGHT_BROWN, 2679, true),
    DEFLECT_MELEE(71, 6, 733, 21375, -1, PrayerCategory.LIGHT_BROWN, 2678, true),
    LEECH_ATTACK(74, 2, 734, 21377, -1, PrayerCategory.PINK, 1982, true, new SkillBonus(Skills.ATTACK, 0.05)),
    LEECH_RANGED(76, 2, 735, 21379, -1, PrayerCategory.LIME_GREEN, 2666, true, new SkillBonus(Skills.RANGE, 0.05)),
    LEECH_MAGIC(78, 2, 736, 21381, -1, PrayerCategory.BABY_BLUE, 2668, true, new SkillBonus(Skills.MAGIC, 0.05)),
    LEECH_DEFENSE(80, 2, 737, 21383, -1, PrayerCategory.MAGENTA, 2687, true, new SkillBonus(Skills.DEFENCE, 0.05)),
    LEECH_STRENGTH(82, 2, 738, 21385, -1, PrayerCategory.PINK, 2691, true, new SkillBonus(Skills.STRENGTH, 0.05)),
    LEECH_ENERGY(84, 2, 739, 21387, -1, PrayerCategory.PURPLE, 2667, true),
    LEECH_SPECIAL_ATTACK(86, 2, 740, 21389, -1, PrayerCategory.PURPLE, 2679, true),
    WRATH(89, 2, 741, 21391, -1, PrayerCategory.LIGHT_BROWN, PrayerCategory.RED, new Audio(2675), true),
    SOUL_SPLIT(92, 1, 742, 21393, -1, PrayerCategory.LIGHT_BROWN, PrayerCategory.RED, new Audio(2677), true),
    TURMOIL(95, 1, 743, 21395, -1, PrayerCategory.PINK, PrayerCategory.MAGENTA, new Audio(2676), true, new SkillBonus(Skills.DEFENCE, 0.30), new SkillBonus(Skills.STRENGTH, 0.27), new SkillBonus(Skills.ATTACK, 0.25));

    /**
     * Represents the a cache of objects related to prayers in order to decide what head icon to display.
     */
    private final static Object[][] ICON_CACHE_OLD = new Object[][]{
        { REDEMPTION, 5 },
        { RETRIBUTION, 3 },
        { SMITE, 4 },
        { PROTECT_FROM_MAGIC, 2, 10 },
        { PROTECT_FROM_MELEE, 0, 8 },
        { PROTECT_FROM_MISSILES, 1, 9 },
        { PROTECT_FROM_MELEE, 8, PROTECT_FROM_MISSILES, 9, PROTECT_FROM_MAGIC, 10 }
    };

    /**
     * Represents the a cache of objects related to prayers in order to decide what head icon to display.
     */
    private final static Object[][] ICON_CACHE = new Object[][]{
        { REDEMPTION, 5 },
        { RETRIBUTION, 3 },
        { SMITE, 4 },
        { PROTECT_FROM_MAGIC, 2, 10 },
        { PROTECT_FROM_MELEE, 0, 8 },
        { PROTECT_FROM_MISSILES, 1, 9 },
        //{PROTECT_FROM_SUMMONING, 7, PROTECT_FROM_MELEE, 8, PROTECT_FROM_MISSILES, 9, PROTECT_FROM_MAGIC, 10},
        { DEFLECT_MELEE, 9, 13 },
        { DEFLECT_MAGIC, 10, 14 },
        { DEFLECT_MISSILES, 11, 15 },
        { DEFLECT_SUMMONING, 12, DEFLECT_MELEE, 13, DEFLECT_MISSILES, 14, DEFLECT_MAGIC, 15 },
        { WRATH, 16 },
        { SOUL_SPLIT, 17 }
    };

    /**
     * The level required.
     */
    private final int level;

    /**
     * The drain rate.
     */
    private final int drain;

    /**
     * The configuration id of the prayer.
     */
    private final int config;

    /**
     * The button id.
     */
    private final int button;

    /**
     * The quick prayer button id.
     */
    private final int quickButton;

    /**
     * The restriction.
     */
    private final PrayerCategory restriction;

    /**
     * Represents the second restriction.
     */
    private final PrayerCategory secondRestriction;

    /**
     * The sound.
     */
    private Audio audio;

    /**
     * The skill bonuses for this type.
     */
    private final SkillBonus[] bonuses;

    /**
     * If the prayer is classified as an Ancient Curse.
     */
    private boolean curses;

    /**
     * Constructs a new {@code PrayerType} {@code Object}.
     *
     * @param level   the level.
     * @param drain   the drain, represents the seconds until a drain.
     * @param config  the config value to represent on and off.
     * @param button  the button value to turn the prayer off and on.
     * @param bonuses the skill bonuses for this type.
     */
    PrayerType(int level, int drain, int config, int button, int quickButton, PrayerCategory restriction, int soundId, SkillBonus... bonuses) {
        this(level, drain, config, button, quickButton, restriction, null, new Audio(soundId), false, bonuses);
    }

    /**
     * Constructs a new {@code PrayerType} {@code Object}.
     *
     * @param level   the level.
     * @param drain   the drain, represents the seconds until a drain.
     * @param config  the config value to represent on and off.
     * @param button  the button value to turn the prayer off and on.
     * @param bonuses the skill bonuses for this type.
     */
    PrayerType(int level, int drain, int config, int button, int quickButton, PrayerCategory restriction, int soundId, boolean curses, SkillBonus... bonuses) {
        this(level, drain, config, button, quickButton, restriction, null, new Audio(soundId), curses, bonuses);
    }

    /**
     * Constructs a new {@code PrayerType} {@code Object}.
     *
     * @param level   the level.
     * @param drain   the drain, represents the seconds until a drain.
     * @param config  the config value to represent on and off.
     * @param button  the button value to turn the prayer off and on.
     * @param bonuses the skill bonuses for this type.
     */
    PrayerType(int level, int drain, int config, int button, int quickButton, PrayerCategory restriction, PrayerCategory secondRestriction, Audio audio, boolean curses, SkillBonus... bonuses) {
        this.level = level;
        this.drain = drain;
        this.config = config;
        this.button = button;
        this.quickButton = quickButton;
        this.restriction = restriction;
        this.secondRestriction = secondRestriction;
        this.setSound(audio);
        this.curses = curses;
        this.bonuses = bonuses;
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
     * Gets the drain.
     *
     * @return The drain.
     */
    public int getDrain() {
        return drain;
    }

    /**
     * Gets the config.
     *
     * @return The config.
     */
    public int getConfig() {
        return config;
    }


    /**
     * Gets the button.
     *
     * @return The button.
     */
    public int getButton() {
        return button;
    }

    /**
     * Gets the restriction.
     *
     * @return The restriction.
     */
    public PrayerCategory getRestriction() {
        return restriction;
    }

    /**
     * Gets the bonuses.
     *
     * @return The bonuses.
     */
    public SkillBonus[] getBonuses() {
        return bonuses;
    }

    /**
     * Method used to check if the player has the required level to toggle this type.
     *
     * @param player the player.
     * @return <code>True</code> if it is permitted.
     */
    public boolean permitted(final Player player) {
        return permitted(player, true);
    }

    /**
     * Method used to check if the player has the required level to toggle this type.
     *
     * @param player the player.
     * @return <code>True</code> if it is permitted.
     */
    public boolean permitted(final Player player, boolean toggle) {
        if (toggle && DuelRule.NO_PRAYER.enforce(player, true)) {
            return false;
        }
        if (player.getSkills().getStaticLevel(Skills.PRAYER) < getLevel()) {
            //  player.getActionSender().sendSound(new Audio(2673));
            // TODO Sound
            player.getDialogueInterpreter().sendPlaneMessage("You need a <col=08088A>Prayer level of " + getLevel() + " to use " + TextUtils.formatDisplayName(name().toLowerCase().replace("_", " ")) + ".");
            return false;
        }
        return true;
    }

    /**
     * Method used to check if we need to toggle a prayer on or off.
     *
     * @param player the player.
     * @return <code>True</code> if toggled.
     */
    public boolean toggle(final Player player, final boolean on) {
        player.getConfigManager().set(getConfig(), on ? 1 : 0);
        if (on) {
            flag(player, this);
            player.getPrayer().getActive().add(this);
            player.getPrayer().getTask().init(player);
            iconify(player, getIcon(player, this));
            switch (this) {
                case CURSE_PROTECT_ITEMS:
                    player.visualize(Animation.create(12567), Graphics.create(2000));
                    break;
                case BERSERKER:
                    player.visualize(Animation.create(12589), Graphics.create(2035));
                    break;
                case TURMOIL:
                    player.visualize(Animation.create(12565), Graphics.create(2013));
                    break;
                case SAP_MAGE:
                case SAP_RANGER:
                case SAP_SPIRIT:
                case SAP_WARRIOR:
                case LEECH_ATTACK:
                case LEECH_DEFENSE:
                case LEECH_ENERGY:
                case LEECH_MAGIC:
                case LEECH_RANGED:
                case LEECH_SPECIAL_ATTACK:
                case LEECH_STRENGTH:
                case DEFLECT_SUMMONING:
                    // toggle(player, false);
                    //  player.getActionSender().sendMessage("Coming soon.");
                    //  break;
            }
            // TODO 317 sounds
            //  player.getActionSender().sendSound(getSound());
        } else {
            player.getPrayer().getActive().remove(this);
            findNextIcon(player);
            if (player.getPrayer().getActive().isEmpty()) {
                player.getPrayer().getTask().stop(player);
                return true;
            }
        }
        return true;
    }

    /**
     * Method used to flag others prayers that cannot be toggled together.
     *
     * @param player the player.
     */
    public void flag(final Player player, final PrayerType type) {
        final List<PrayerType> active = player.getPrayer().getActive();
        final PrayerType[] remove = new PrayerType[active.size() + 10];
        int index = 0;
        for (PrayerType anActive : active) {
            if (anActive.getRestriction() == type.getRestriction() || anActive.getSecondRestriction() != null
                && type.getSecondRestriction() != null && anActive.getSecondRestriction() == type.getSecondRestriction()) {
                remove[index++] = anActive;
                continue;
            }
            for (SkillBonus b : anActive.getBonuses()) {
                for (SkillBonus bb : type.getBonuses()) {
                    if ((bb.getSkillId() == b.getSkillId()) || (b.getSkillId() == Skills.STRENGTH ||
                        b.getSkillId() == Skills.ATTACK) && (bb.getSkillId() == Skills.MAGIC ||
                        bb.getSkillId() == Skills.RANGE) || (b.getSkillId() == Skills.RANGE ||
                        b.getSkillId() == Skills.MAGIC) && (bb.getSkillId() == Skills.ATTACK ||
                        bb.getSkillId() == Skills.STRENGTH) || (b.getSkillId() == Skills.DEFENCE &&
                        bb.getSkillId() == Skills.DEFENCE)) {
                        remove[index++] = anActive;
                    }
                }
            }
        }
        for (int i = 0; i < index; i++) {
            remove[i].toggle(player, false);
        }
    }

    /**
     * Method used to iconify the player.
     *
     * @param player The player.
     * @param icon   The icon.
     */
    public boolean iconify(final Player player, final int icon) {
        if (icon == -1) {
            return false;
        }
        player.getAppearance().setHeadIcon(icon);
        player.getAppearance().sync();
        return false;
    }

    /**
     * Method used to find the next icon in place.
     *
     * @param player the player.
     */
    public void findNextIcon(final Player player) {
        if (!hasIcon(player)) {
            player.getAppearance().setHeadIcon(-1);
            player.getAppearance().sync();
        }
//        if ((this == PROTECT_FROM_MELEE || this == PROTECT_FROM_MISSILES || this == PROTECT_FROM_MAGIC) &&
//                player.getPrayer().get(PROTECT_FROM_SUMMONING)) {
//            iconify(player, 7);
//        }
        if ((this == DEFLECT_MELEE || this == DEFLECT_MISSILES || this == DEFLECT_MAGIC) && player.getPrayer().get(DEFLECT_SUMMONING)) {
            iconify(player, 15);
        }
//        if (this == PROTECT_FROM_SUMMONING) {
//            for (PrayerType t : player.getPrayer().getActive()) {
//                iconify(player, getIcon(player, t));
//            }
//            if (player.getAppearance().getHeadIcon() == 7) {
//                player.getAppearance().setHeadIcon(-1);
//                player.getAppearance().sync();
//            }
//        }
        if (this == DEFLECT_SUMMONING) {
            for (PrayerType t : player.getPrayer().getActive()) {
                iconify(player, getIcon(player, t));
            }
            if (player.getAppearance().getHeadIcon() == 15) {
                player.getAppearance().setHeadIcon(-1);
                player.getAppearance().sync();
            }
        }
    }

    /**
     * Method used to get the icon value.
     *
     * @param player The player.
     * @param type   The {@code PrayerType}.
     * @return the icon.
     */
    public int getIcon(final Player player, final PrayerType type) {
        List<PrayerType> active = player.getPrayer().getActive();
        for (Object[] aICON_CACHE : ICON_CACHE) {
            if (aICON_CACHE.length == 2 && type == ((PrayerType) aICON_CACHE[0])) {
                return (int) aICON_CACHE[1];
            } else if (aICON_CACHE.length == 3 && type == ((PrayerType) aICON_CACHE[0])) {
//                if (active.contains(PROTECT_FROM_SUMMONING) || active.contains(DEFLECT_SUMMONING)) {
//                    return (int) ICON_CACHE[i][2];
//                }
                return (int) aICON_CACHE[1];
            } else if (aICON_CACHE.length == 8 && type == ((PrayerType) aICON_CACHE[0])) {
                for (int k = 2; k < aICON_CACHE.length; k++) {
                    if (active.contains(aICON_CACHE[k])) {
                        return (int) aICON_CACHE[k + 1];
                    }
                }
                return (int) aICON_CACHE[1];
            }
        }
        return -1;
    }

    /**
     * Method used to check if theres an icon present.
     *
     * @param player the player.
     * @return <code>True</code> if theres an icon present.
     */
    public boolean hasIcon(final Player player) {
        int count = 0;
        for (PrayerType type : player.getPrayer().getActive()) {
            if (getIcon(player, type) != -1) {
                count++;
            }
        }
        return count != 0;
    }

    /**
     * Method used to return the type by the button.
     *
     * @param button the button.
     * @return the type.
     */
    public static PrayerType forId(int button) {
        for (PrayerType type : PrayerType.values()) {
            if (type.getButton() == button) {
                return type;
            }
        }
        return null;
    }

    /**
     * Method used to return the type by the button.
     *
     * @param childId The button.
     * @return The type.
     */
    public static PrayerType forQuick(int childId) {
        for (PrayerType type : PrayerType.values()) {
            if (type.quickButton == childId) {
                return type;
            }
        }
        return null;
    }

    /**
     * Gets a {@link org.gielinor.game.node.entity.player.link.prayer.PrayerType} by ordinal.
     *
     * @param ordinal The ordinal.
     * @return The prayer type.
     */
    public static PrayerType forOrdinal(int ordinal) {
        for (PrayerType prayerType : PrayerType.values()) {
            if (prayerType.ordinal() == ordinal) {
                return prayerType;
            }
        }
        return null;
    }

    /**
     * Method used to get the melee types.
     *
     * @return the types.
     */
    public static PrayerType[] getMeleeTypes() {
        return getByBonus(Skills.ATTACK, Skills.STRENGTH);
    }

    /**
     * Method used to get the rage types.
     *
     * @return the types.
     */
    public static PrayerType[] getRangeTypes() {
        return getByBonus(Skills.RANGE);
    }

    /**
     * Method used to get the magic types.
     *
     * @return the types.
     */
    public static PrayerType[] getMagicTypes() {
        return getByBonus(Skills.MAGIC);
    }

    /**
     * Method used to get the prayer types by the bonuses.
     *
     * @param ids the ids.
     * @return the type.
     */
    public static PrayerType[] getByBonus(int... ids) {
        PrayerType[] types = new PrayerType[values().length];
        int count = 0;
        for (PrayerType type : values()) {
            for (SkillBonus b : type.getBonuses()) {
                for (int i : ids) {
                    if (i == b.getSkillId()) {
                        types[count] = type;
                        count++;
                    }
                }
            }
        }
        return types;
    }


    /**
     * Gets the secondRestriction.
     *
     * @return The secondRestriction.
     */
    public PrayerCategory getSecondRestriction() {
        return secondRestriction;
    }

    /**
     * Gets the sound.
     *
     * @return The sound.
     */
    public Audio getSound() {
        return audio;
    }

    /**
     * Sets the sound.
     *
     * @param audio The sound to set.
     */
    public void setSound(Audio audio) {
        this.audio = audio;
    }

    public boolean isCurses() {
        return curses;
    }
}
