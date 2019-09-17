package org.gielinor.utilities.voting;

import org.gielinor.game.content.global.EquipmentSet;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Represents a voting reward.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public enum VoteReward {

    MYSTERY_BOX(200, new Item(6199), "Mystery Box", new Item(6199)),
    //    DOUBLE_EXP_2HR(100, null, "Double experience (2 hours)") {
//        @Override
//        public String[] canApplyReward(Player player) {
//            if (player.getSavedData().getGlobalData().getExperienceModifierTime() > 1) {
//                return new String[]{"Sorry! You can't claim that reward.", "You still have " + player.getSavedData().getGlobalData().getExperienceModifierTime() + " minutes left of bonus", "experience."};
//            }
//            return null;
//        }
//
//        @Override
//        public void applyReward(Player player) {
//            player.getSavedData().getGlobalData().setExperienceModifierTime(120);
//            player.getSavedData().getGlobalData().setExperienceModifier(2);
//            player.getActionSender().sendMessage("<col=254F00>You now have double experience for 2 hours.");
//        }
//    },
    MAGIC_SECATEURS(100, new Item(7409), "Magic secateurs", new Item(7409)),
    LUMBERJACK_SET(100, new Item(10945), "Lumberjack set", EquipmentSet.LUMBERJACK.getSet()) {
        @Override
        public String[] canApplyReward(Player player) {
            if (player.getSkills().getStaticLevel(Skills.WOODCUTTING) < 44) {
                return new String[]{ "Sorry! You can't claim that reward.", "You need a Woodcutting level of at least 44." };
            }
            return null;
        }
    },
    BLUE_NAVAL_SET(50, new Item(8959), "Blue naval set", new Item(8959), new Item(8952), new Item(8991)),
    GREEN_NAVAL_SET(50, new Item(8960), "Green naval set", new Item(8960), new Item(8953), new Item(8992)),
    RED_NAVAL_SET(50, new Item(8961), "Red naval set", new Item(8961), new Item(8954), new Item(8993)),
    BROWN_NAVAL_SET(50, new Item(8962), "Brown naval set", new Item(8962), new Item(8955), new Item(8994)),
    BLACK_NAVAL_SET(50, new Item(8963), "Black naval set", new Item(8963), new Item(8956), new Item(8995)),
    PURPLE_NAVAL_SET(50, new Item(8964), "Purple naval set", new Item(8964), new Item(8957), new Item(8996)),
    GREY_NAVAL_SET(50, new Item(8965), "Grey naval set", new Item(8965), new Item(8958), new Item(8997)),
    INFERNAL_ADZE(300, new Item(13661), "Inferno adze", new Item(13661)),
    LESSER_DEMON_MASK(15, new Item(40020), "Lesser demon mask", new Item(40020)),
    GREATER_DEMON_MASK(20, new Item(40023), "Greater demon mask", new Item(40023)),
    BLACK_DEMON_MASK(30, new Item(40026), "Black demon mask", new Item(40026)),
    JUNGLE_DEMON_MASK(30, new Item(40032), "Jungle demon mask", new Item(40032)),
    OLD_DEMON_MASK(50, new Item(40029), "Old demon mask", new Item(40029)),
    DRAGON_FULL_HELM_ORNAMENT_KIT(10, new Item(32538), "Dragon full helm ornament kit", new Item(32538)),
    DRAGON_CHAINBODY_ORNAMENT_KIT(10, new Item(32534), "Dragon chainbody ornament kit", new Item(32534)),
    DRAGON_LEGS_SKIRT_ORNAMENT_KIT(10, new Item(32536), "Dragon legs/skirt ornament kit", new Item(32536)),
    DRAGON_SQ_SHIELD_ORNAMENT_KIT(10, new Item(32532), "Dragon sq shield ornament kit", new Item(32532)),
    DRAGON_SCIMITAR_ORNAMENT_KIT(10, new Item(40002), "Dragon scimitar ornament kit", new Item(40002)),
    SHAYZIEN_HOUSE_HOOD(10, new Item(40125), "Shayzien house hood", new Item(40125)),
    BLOODHOUND_PET(150, new Item(19730), "Bloodhound pet", new Item(19730)),;

    /**
     * The cost of the reward in points.
     */
    private final int cost;

    /**
     * The item to show.
     */
    private Item item;

    /**
     * The text to display.
     */
    private final String text;

    /**
     * The reward items given.
     */
    private Item[] rewards;

    /**
     * Creates a new voting reward.
     *
     * @param cost The cost of the reward in points.
     * @param text The text to display.
     */
    private VoteReward(int cost, String text) {
        this.cost = cost;
        this.text = text;
    }

    /**
     * Creates a new voting reward.
     *
     * @param cost    The cost of the reward in points.
     * @param item    The item to display.
     * @param text    The text to display.
     * @param rewards The reward items given.
     */
    private VoteReward(int cost, Item item, String text, Item... rewards) {
        this.cost = cost;
        this.item = item;
        this.text = text;
        this.rewards = rewards;
    }

    /**
     * Gets the cost of the reward in points.
     *
     * @return The cost.
     */
    public int getCost() {
        return cost;
    }

    /**
     * Gets the item to show.
     *
     * @return The item.
     */
    public Item getItem() {
        return item;
    }

    /**
     * Gets the text to display.
     *
     * @return The text.
     */
    public String getText() {
        return text;
    }

    /**
     * Gets the reward items given.
     *
     * @return The items.
     */
    public Item[] getRewards() {
        return rewards;
    }

    /**
     * Whether or not the reward can be applied to the player's account.
     *
     * @param player The player.
     * @return <code>True</code> if so.
     */
    public String[] canApplyReward(Player player) {
        return null;
    }

    /**
     * Applies the reward to the player's account.
     *
     * @param player The player.
     */
    public void applyReward(Player player) {

    }
}
