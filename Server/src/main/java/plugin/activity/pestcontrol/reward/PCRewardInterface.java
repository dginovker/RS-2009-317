package plugin.activity.pestcontrol.reward;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.herblore.Herbs;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the pest control reward interface.
 *
 * @author 'Vexia
 */
public final class PCRewardInterface extends ComponentPlugin {

    /**
     * Represents the red colour.
     */
    public static final String RED = "<col=FF0000>";
    /**
     * Represents the green colour.
     */
    public static final String GREEN = "<col=04B404>";
    /**
     * Represents the white colour.
     */
    public static final String WHITE = "<col=FFFFFF>";
    /**
     * Represents the skill headers and their ids.
     */
    private static final int[][] SKILLS = new int[][]{
        { 32773, Skills.ATTACK },
        { 32778, Skills.STRENGTH },
        { 32783, Skills.DEFENCE },
        { 32788, Skills.RANGE },
        { 32793, Skills.MAGIC },
        { 32798, Skills.HITPOINTS },
        { 32803, Skills.PRAYER }
    };
    /**
     * Represents the skill options array.
     */
    public static final int[] SKILL_POINTS = new int[]{ 1, 10, 100 };
    /**
     * Represents the points options strings.
     */
    private static final int[] POINT_CHILDREN = new int[]{
        32774, 32775, 32776,
        32779, 32780, 32781,
        32784, 32785, 32786,
        32789, 32790, 32791,
        32794, 32795, 32796,
        32799, 32800, 32801,
        32804, 32805, 32806,
        32810, 32813, 32816,
        32820, 32823, 32826,
        32829, 32832, 32835,
        32838, 32841
    };

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.forId(32762).setPlugin(this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        switch (button) {
            case 32768:
                confirm(player);
                return true;
            default:
                if (button >= 32774 && button <= 32841) {
                    if (player.getSavedData().getActivityData().getPestPoints() == 0) {
                        player.getActionSender().sendMessage("You don't have enough points.");
                        return true;
                    }
                    select(player, button);
                }
                break;
        }
        return true;
    }

    /**
     * Method used to open the pest control reward interface.
     *
     * @param player The player.
     */
    public static void open(final Player player) {
        player.removeAttribute("pc-reward");
        clear(player);
        sendString(player, "Points: " + player.getSavedData().getActivityData().getPestPoints(), 32770);
        player.getInterfaceState().open(new Component(32762));
    }

    /**
     * Method used to send the skill headers.
     *
     * @param player the player.
     */
    private static void sendSkills(final Player player) {
        for (int[] skill : SKILLS) {
            sendString(player, getSkillCondition(player, skill[1]), skill[0]);
        }
    }

    /**
     * Method used to send a string onto this interface.
     *
     * @param player The player instance.
     * @param string The string to send.
     * @param child  The child to send it on.
     */
    private static void sendString(final Player player, String string, int child) {
        player.getActionSender().sendString(string, child);
    }

    /**
     * Method used to select a reward.
     *
     * @param player   The player.
     * @param buttonId The button.
     */
    public void select(final Player player, final int buttonId) {
        final Reward reward = Reward.forButton(buttonId);
        final int option = reward.getOption(buttonId);
        if (!reward.checkRequirements(player, option)) {
            return;
        }
        cacheReward(player, reward, option);
    }

    /**
     * Method used to selected the current reward.
     *
     * @param player the player.
     */
    public boolean deselect(final Player player) {
        return deselect(player, getReward(player));
    }

    /**
     * Method used to deselect the reward.
     *
     * @param player the player.
     * @param reward the reward.
     */
    public boolean deselect(final Player player, final Reward reward) {
        if (reward == null) {
            return false;
        }
        clear(player);
        reward.deselect(player, getCachedOption(player));
        return true;
    }

    /**
     * Method used to cache the reward.
     *
     * @param player The player.
     * @param reward The reward to cache.
     * @param option The option selected.
     */
    public final void cacheReward(final Player player, final Reward reward, final int option) {
        deselect(player);
        reward.select(player, option);
        sendString(player, "<col=F7DF22>Confirm:", 32768);
        sendString(player, reward.getName(), 32769);
        player.setAttribute("pc-reward", reward);
        player.setAttribute("pc-reward:option", option);
    }

    /**
     * Method used to clear the interface with new data.
     *
     * @param player the player.
     */
    public static void clear(final Player player) {
        sendString(player, "<col=969696>Confirm:", 32768);
        sendString(player, "Points: " + player.getSavedData().getActivityData().getPestPoints(), 32770);
        sendString(player, "", 32769);
        for (int pointChild : POINT_CHILDREN) {
            player.getActionSender().sendInterfaceColour(pointChild, 0x855013, true);
        }
        sendSkills(player);
        for (Reward reward : Reward.values()) {
            if (reward.isSkillReward()) {
                continue;
            }
            sendString(player, (player.getSavedData().getActivityData().getPestPoints() <
                reward.getPoints() ? player.getSavedData().getActivityData().getPestPoints() < 1
                ? RED + ("You need at least 1 point.") : RED + ("You need " + reward.getPoints() + " points.") :
                (GREEN + reward.getName())), reward.getChildId());
        }
    }

    /**
     * Method used to calculate the experience the player can recieve in this skill.
     *
     * @param player  the player.
     * @param skillId the skill.
     * @return the experience as an integer.
     */
    public static double calculateExperience(final Player player, final int skillId) {
        int level = player.getSkills().getStaticLevel(skillId);
        double mod = 17.5;
        if (skillId == Skills.PRAYER) {
            mod = 34;
        } else if (skillId == Skills.MAGIC || skillId == Skills.RANGE) {
            mod = 19.1;
        }
        return (int) ((level * level) / mod) * (skillId == Skills.PRAYER ? Constants.NON_COMBAT_EXP_MODIFIER :
            Constants.COMBAT_EXP_MODIFIER);
    }

    /**
     * Method used to get the skill condition string to send.
     *
     * @param player  the player.
     * @param skillId the skillId.
     * @return the string to send.
     */
    public static String getSkillCondition(final Player player, final int skillId) {
        if (player.getSkills().getStaticLevel(skillId) < 25) {
            return RED + "Must reach level 25 first.";
        }
        return GREEN + getSkillXp(player, skillId);
    }

    /**
     * Method used to get the skill experience string.
     *
     * @param player  the player.
     * @param skillId the skill id.
     * @return the string.
     */
    public static String getSkillXp(final Player player, int skillId) {
        return Skills.SKILL_NAME[skillId] + " - " + (int) calculateExperience(player, skillId) + " xp";
    }

    /**
     * Method used to get the current reward.
     *
     * @param player the player.
     * @return the reward.
     */
    public static Reward getReward(final Player player) {
        return player.getAttribute("pc-reward", null);
    }

    /**
     * Method used to check if the player has a reward set.
     *
     * @param player the player.
     * @return the reward.
     */
    public static boolean hasReward(final Player player) {
        return getReward(player) != null;
    }

    /**
     * Method used to get the pest control reward option index.
     *
     * @param player the player.
     * @return the option index.
     */
    public static int getCachedOption(final Player player) {
        return player.getAttribute("pc-reward:option", 0);
    }


    /**
     * Method used to confirm the reward.
     *
     * @param player the player.
     */
    public final void confirm(final Player player) {
        if (!hasReward(player)) {
            player.getActionSender().sendMessage("Please choose a reward.");
            return;
        }
        final Reward reward = getReward(player);
        if (!reward.isSkillReward() && player.getInventory().freeSlots() == 0) {
            player.getActionSender().sendMessage("You don't have enough inventory space.");
            return;
        }
        final int option = getCachedOption(player);
        final int points = reward.getPoints(option);
        String message;
        player.getInterfaceState().close();
        if (player.getSavedData().getActivityData().getPestPoints() >= points) {
            player.getSavedData().getActivityData().decreasePestPoints(points);
            if (reward.isSkillReward()) {
                final double experience = (int) calculateExperience(player, reward.getSkill()) * points;
                player.getSkills().addExperienceNoMod(reward.getSkill(), experience);
                message = "The Void Knight has granted you " + (int) experience + " " + reward.getName() + ".";
            } else {
                if (!reward.checkItemRequirement(player, option)) {
                    return;
                }
                if (reward.getReward().length > 1) {
                    Item[] pack = reward.constructPack();
                    for (Item i : pack) {
                        if (!player.getInventory().add(i)) {
                            GroundItemManager.create(i, player);
                        }
                    }
                } else {
                    if (!player.getInventory().add(reward.getReward()[0])) {
                        GroundItemManager.create(reward.getReward()[0], player);
                    }
                }
                message = "The Void Knight has given you a " + reward.getName() + ".";
            }
            player.getDialogueInterpreter().sendPlaneMessage(message, "<col=571D07>Remaining Void Knight Commendation Points: " + player.getSavedData().getActivityData().getPestPoints());
        }
    }


    /**
     * Represents the rewards that are obtainable with this interface.
     *
     * @author 'Vexia
     */
    public enum Reward {
        ATTACK(Skills.ATTACK, SKILLS[0][0]),
        STRENGTH(Skills.STRENGTH, SKILLS[1][0]),
        DEFENCE(Skills.DEFENCE, SKILLS[2][0]),
        RANGE(Skills.RANGE, SKILLS[3][0]),
        MAGIC(Skills.MAGIC, SKILLS[4][0]),
        HITPOINTS(Skills.HITPOINTS, SKILLS[5][0]),
        PRAYER(Skills.PRAYER, SKILLS[6][0]),
        HERB_PACK("Herb Pack", 30, new Item[]{ Herbs.HARRALANDER.getHerb(), Herbs.RANARR.getHerb(), Herbs.TOADFLAX.getHerb(), Herbs.IRIT.getHerb(), Herbs.AVANTOE.getHerb(), Herbs.KWUARM.getHerb(), Herbs.GUAM.getHerb(), Herbs.MARRENTILL.getHerb() }, 32809) {
            @Override
            public boolean checkItemRequirement(final Player player, final int option) {
                if (player.getSkills().getLevel(Skills.HERBLORE) < 25) {
                    player.getActionSender().sendMessage("You need level 25 herblore to purchase this pack.");
                    return false;
                }
                return true;
            }
        },
        MINERAL_PACK("Mineral Pack", 15, new Item[]{ new Item(453), new Item(440) }, 32812) {
            @Override
            public boolean checkItemRequirement(final Player player, final int option) {
                if (player.getSkills().getLevel(Skills.MINING) < 25) {
                    player.getActionSender().sendMessage("You need level 25 mining to purchase this pack.");
                    return false;
                }
                return true;
            }
        },
        SEED_PACK("Seed Pack", 15, new Item[]{ new Item(5320), new Item(5322), new Item(5100) }, 32815) {
            @Override
            public boolean checkItemRequirement(final Player player, final int option) {
                if (player.getSkills().getLevel(Skills.FARMING) < 25) {
                    player.getActionSender().sendMessage("You need level 25 farming to purchase this pack.");
                    return false;
                }
                return true;
            }
        },
        VOID_MACE("Void Knight Mace", 250, new Item[]{ new Item(8841) }, 32819) {
            @Override
            public boolean checkItemRequirement(final Player player, final int option) {
                return hasVoidSkills(player);
            }
        },
        VOID_TOP("Void Knight Top", 250, new Item[]{ new Item(8839) }, 32831) {
            @Override
            public boolean checkItemRequirement(final Player player, final int option) {
                return hasVoidSkills(player);
            }
        },
        VOID_ROBES("Void Knight Robes", 250, new Item[]{ new Item(8840) }, 32822) {
            @Override
            public boolean checkItemRequirement(final Player player, final int option) {
                return hasVoidSkills(player);
            }
        },
        VOID_GLOVES("Void Knight Gloves", 150, new Item[]{ new Item(8842) }, 32834) {
            @Override
            public boolean checkItemRequirement(final Player player, final int option) {
                return hasVoidSkills(player);
            }
        },
        VOID_MAGE_HELM("Void Knight Mage Helm", 200, new Item[]{ new Item(11663) }, 32825) {
            @Override
            public boolean checkItemRequirement(final Player player, final int option) {
                return hasVoidSkills(player);
            }
        },
        VOID_RANGER_HELM("Void Knight Ranger Helm", 200, new Item[]{ new Item(11664) }, 32837) {
            @Override
            public boolean checkItemRequirement(final Player player, final int option) {
                return hasVoidSkills(player);
            }
        },
        VOID_MELEE_HELM("Void Knight Melee Helm", 200, new Item[]{ new Item(11665) }, 32828) {
            @Override
            public boolean checkItemRequirement(final Player player, final int option) {
                return hasVoidSkills(player);
            }
        },
        VOID_KNIGHT_SEAL("Void Knight Seal", 10, new Item[]{ new Item(11666) }, 32840);
//        SPINNER_CHARM("Spinner Charm", new Item(12166), 73, 77, 78, 79),
//        RAVAGER_CHARM("Ravager Charm", new Item(12164), 74, 86, 82, 83),
//        TORCHER_CHARM("Torcher Charm", new Item(12167), 76, 88, 80, 81),
//        SHIFTER_CHAR("Shifter Charm", new Item(12165), 75, 87, 84, 85);

        /**
         * Represents the void required skills.
         */
        private final int[] VOID_SKILLS = new int[]{
            Skills.HITPOINTS, Skills.ATTACK, Skills.DEFENCE,
            Skills.STRENGTH, Skills.RANGE, Skills.MAGIC,
            Skills.PRAYER };

        /**
         * Represents the base child id.
         */
        private int childId;
        /**
         * Represents the skill id.
         */
        private int skill = -1;
        /**
         * Represents the reward items.
         */
        private Item[] reward;
        /**
         * Represents the reward name.
         */
        private String name;
        /**
         * Represents the points required for this reward.
         */
        private int points;

        /**
         * Constructs a new {@code PCRewardInterface} {@code Object}.
         *
         * @param childId The base child id.
         */
        Reward(final int childId) {
            this.childId = childId;
        }

        /**
         * Constructs a new {@code PCRewardInterface} {@code Object}.
         *
         * @param skill   The skill.
         * @param childId The base child id.
         */
        Reward(int skill, final int childId) {
            this.skill = skill;
            this.childId = childId;
        }

        /**
         * Constructs a new {@code PCRewardInterfacce} {@code Object}.
         *
         * @param name    The name.
         * @param points  The points.
         * @param reward  The rewards.
         * @param childId The base child id.
         */
        Reward(String name, int points, final Item[] reward, final int childId) {
            this.name = name;
            this.points = points;
            this.reward = reward;
            this.childId = childId;
        }

        /**
         * Method used to check the requirements of a reward.
         *
         * @param player The player.
         * @param option The option.
         * @return <code>True</code> if so.
         */
        public boolean checkRequirements(final Player player, final int option) {
            if (player.getSavedData().getActivityData().getPestPoints() < getPoints(option)) {
                player.getActionSender().sendMessage("You don't have enough points.");
                return false;
            }
            return isSkillReward() ? checkSkillRequirement(player, option) : checkItemRequirement(player, option);
        }

        /**
         * Method used to select a reward.
         *
         * @param player The player.
         * @param option The option.
         */
        public void select(final Player player, final int option) {
            if (isSkillReward()) {
                skillSelect(player, option);
            } else {
                itemSelect(player, option);
            }
        }

        /**
         * Method used to deselect a reward.
         *
         * @param player The player.
         * @param option The option.
         */
        public void deselect(final Player player, final int option) {
            if (isSkillReward()) {
                skillDeselect(player, option);
            } else {
                itemDeselect(player, option);
            }
        }

        /**
         * Method used to select a skill.
         *
         * @param player The player.
         * @param option The option index.
         */
        public final void skillSelect(final Player player, final int option) {
            sendString(player, WHITE + getSkillXp(player, skill), getChildId());
            sendString(player, WHITE + getOptionString(option), getChildId() + option);
        }

        /**
         * Method used to handle the item select.
         *
         * @param player The player.
         * @param option The option.
         */
        public final void itemSelect(final Player player, final int option) {
            sendString(player, WHITE + getName(), getChildId());
        }

        /**
         * Method used to deselect a skill.
         *
         * @param player The player.
         * @param option The option.
         */
        public final void skillDeselect(final Player player, final int option) {
            sendString(player, "<col=784F1C>" + getOptionString(option), getChildId() + option);
        }

        /**
         * Method used to handle the item deselect.
         *
         * @param player The player.
         * @param option The option.
         */
        public final void itemDeselect(final Player player, final int option) {

        }

        /**
         * Method used to get the option string.
         *
         * @param option The option index.
         * @return The string.
         */
        public String getOptionString(int option) {
            return (option == 1 ? "(1 Pt)" : option == 2 ? "(10 Pts)" : "(100 Pts)");
        }

        /**
         * Method used to get the option choosen.
         *
         * @param buttonId The button.
         * @return The option.
         */
        public int getOption(int buttonId) {
            int index = 0;
            if (!isSkillReward()) {
                return 0;
            }
            for (int button = getChildId(); button < (getChildId() + 4); button++) {
                if (button == buttonId) {
                    return index;
                }
                index++;
            }
            return -1;
        }

        /**
         * Gets the amt of required points.
         *
         * @param option The options.
         * @return The points.
         */
        public int getPoints(final int option) {
            return isSkillReward() ? SKILL_POINTS[option - 1] : getPoints();
        }

        /**
         * Method used to check if the player has the skills to buy void.
         *
         * @param player The player.
         * @return <code>True</code> if so.
         */
        public boolean hasVoidSkills(final Player player) {
            for (int skill : VOID_SKILLS) {
                if (player.getSkills().getLevel(skill) < (skill != Skills.PRAYER ? 42 : 22)) {
                    player.getActionSender().sendMessage("You need level 42 in hitpoints, attack, defence, strength, ranged, magic, and");
                    player.getActionSender().sendMessage("22 prayer to purchase the " + name.toLowerCase().replace("_", " ").replace("void knight", "").trim() + ".");
                    return false;
                }
            }
            return true;
        }

        /**
         * Gets the name of this reward.
         *
         * @return The name.
         */
        public String getName() {
            return isSkillReward() ? Skills.SKILL_NAME[skill] + " xp" : name;
        }

        /**
         * Method used to check a skill requirement.
         *
         * @param player The player.
         * @return <code>True</code> if so.
         */
        public boolean checkSkillRequirement(final Player player, final int option) {
            if (player.getSkills().getLevel(skill) < 25) {
                player.getActionSender().sendMessage("The Void Knights will not offer training in skills which you have a level of");
                player.getActionSender().sendMessage("less than 25.");
                return false;
            }
            return true;
        }

        /**
         * Method used to check the item requirement reward.
         *
         * @param player The player.
         * @param option The option.
         * @return <code>True</code> if so.
         */
        public boolean checkItemRequirement(final Player player, final int option) {
            /*
             * empty.
             */
            return true;
        }

        /**
         * Represents the maximum build of an item array pack.
         */
        private static final int MAX_BUILD = 18;

        /**
         * Represents the minimum build of an item array pack.
         */
        private static final int MIN_BUILD = 13;

        /**
         * Method used to generate an item back.
         *
         * @return The item pack.
         */
        public Item[] constructPack() {
            int left = this == SEED_PACK || this == HERB_PACK ?
                RandomUtil.random(MIN_BUILD, MAX_BUILD) : RandomUtil.random(38, 43);
            List<Item> pack = new ArrayList<>();
            int amt = 0;
            for (Item i : getReward()) {
                amt = this == SEED_PACK || this == HERB_PACK ? RandomUtil.random(1, 5) : RandomUtil.random(16, 25);
                if (amt > left) {
                    amt = left;
                }
                if (amt < 1) {
                    continue;
                }
                pack.add(new Item(this != SEED_PACK ? ItemDefinition.forId(i.getId()).getNoteId() : i.getId(), amt));
                left -= amt;
            }
            return pack.toArray(new Item[pack.size()]);
        }

        /**
         * Method used to get the reward type based off the button.
         *
         * @param buttonId The button.
         * @return The reward type.
         */
        public static Reward forButton(final int buttonId) {
            for (Reward reward : values()) {
                if (!reward.isSkillReward()) {
                    if (buttonId == reward.getChildId()) { // + 1 ?
                        return reward;
                    }
                }
                for (int button = reward.getChildId(); button < (reward.getChildId() + 4); button++) {
                    if (button == buttonId) {
                        return reward;
                    }
                }
            }
            return null;
        }

        /**
         * Checks if this reward is a skill reward.
         *
         * @return <code>True</code> if so.
         */
        public boolean isSkillReward() {
            return skill >= 0;
        }

        /**
         * Gets the skill.
         *
         * @return The skill.
         */
        public int getSkill() {
            return skill;
        }

        /**
         * Gets the base child id.
         *
         * @return The child id.
         */
        public int getChildId() {
            return childId;
        }

        /**
         * Gets the points.
         *
         * @return The points.
         */
        public int getPoints() {
            return points;
        }

        /**
         * Gets the reward.
         *
         * @return The reward.
         */
        public Item[] getReward() {
            return reward;
        }
    }


}
