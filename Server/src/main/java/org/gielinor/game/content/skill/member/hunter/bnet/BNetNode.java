package org.gielinor.game.content.skill.member.hunter.bnet;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.model.container.impl.Equipment;

/**
 * A butterfly net node.
 *
 * @author Vexia
 */
public class BNetNode {

    /**
     * The butterfly jar item.
     */
    private static final Item BUTTERFLY_JAR = new Item(10012);

    /**
     * The impling jar item.
     */
    protected static final Item IMPLING_JAR = new Item(11260);

    /**
     * The npcs related to this node.
     */
    private final int[] npcs;

    /**
     * The levels needed.
     */
    private final int[] levels;

    /**
     * The experience gained.
     */
    private final double[] experience;

    /**
     * The graphics related to the node.
     */
    private final Graphics[] graphics;

    /**
     * The reward item.
     */
    private final Item reward;

    /**
     * Constructs a new {@code ButterflyNetNode} {@code Object}.
     *
     * @param npcs       the npcs.
     * @param levels     the levels.
     * @param experience the experience.
     * @param graphics   the graphics.
     * @param reward     the reward.
     */
    public BNetNode(int[] npcs, int[] levels, double[] experience, Graphics[] graphics, Item reward) {
        this.npcs = npcs;
        this.levels = levels;
        this.experience = experience;
        this.graphics = graphics;
        this.reward = reward;
    }

    /**
     * Rewards the player.
     *
     * @param player the player.
     * @param npc    the npc.
     */
    public void reward(Player player, NPC npc) {
        if (!isBareHand(player)) {
            if (player.getInventory().remove(getJar())) {
                //Perks.addDouble(player, getReward());
                // TODO Double reward Perk
                player.getInventory().add(reward);
                player.getSkills().addExperience(Skills.HUNTER, getExperience(player));
            }
        } else {
            player.graphics(graphics[0]);
            player.getSkills().addExperience(Skills.HUNTER, getExperiences()[1]);
            player.getSkills().addExperience(Skills.AGILITY, getExperiences()[2]);
        }
    }

    /**
     * Handles a message event.
     *
     * @param type    the type.
     * @param success the success.
     */
    public void message(Player player, int type, boolean success) {
        if (!success) {
            return;
        }
        switch (type) {
            case 1:
                player.getActionSender().sendMessage("You manage to catch the butterfly.");
                if (isBareHand(player)) {
                    player.getActionSender().sendMessage("You release the " + NPCDefinition.forId(npcs[0]).getName().toLowerCase() + " butterfly.");
                }
                break;
        }
    }

    /**
     * Checks if the player has a jar.
     *
     * @param player the player.
     * @return <code>True</code> if so.
     */
    public boolean hasJar(Player player) {
        return player.getInventory().containsItem(getJar());
    }

    /**
     * Checks if the player has a weapon.
     *
     * @param player the player.
     * @return <code>True</code> if so.
     */
    public boolean hasWeapon(Player player) {
        Item item = player.getEquipment().get(Equipment.SLOT_WEAPON);
        return item != null && (item.getId() != 10010 && item.getId() != 11259);
    }

    /**
     * Checks if the player has a net.
     *
     * @param player the player.
     * @return <code>True</code> if so.
     */
    public boolean hasNet(Player player) {
        return player.getEquipment().contains(10010, 1) || player.getEquipment().contains(11259, 1);
    }

    /**
     * Checks if the player is bare handed.
     *
     * @param player the player.
     * @return {@code} True if so.
     */
    public boolean isBareHand(Player player) {
        return !hasNet(player) && player.getSkills().getStaticLevel(Skills.HUNTER) >= getBareHandLevel() && player.getSkills().getStaticLevel(Skills.AGILITY) >= getAgilityLevel();
    }

    /**
     * Gets the experience in hunter.
     *
     * @return the exp.
     */
    public double getExperience(Player player) {
        return experience[0];
    }

    /**
     * Gets the hunter level.
     *
     * @return the level.
     */
    public int getLevel() {
        return levels[0];
    }

    /**
     * Gets the agility level.
     *
     * @return the level.
     */
    public int getAgilityLevel() {
        return levels[2];
    }

    /**
     * Gets the bare hand level.
     *
     * @return the level.
     */
    public int getBareHandLevel() {
        return levels[1];
    }

    /**
     * Gets the npcs.
     *
     * @return The npcs.
     */
    public int[] getNpcs() {
        return npcs;
    }

    /**
     * Gets the levels.
     *
     * @return The levels.
     */
    public int[] getLevels() {
        return levels;
    }

    /**
     * Gets the experience.
     *
     * @return The experience.
     */
    public double[] getExperiences() {
        return experience;
    }

    /**
     * Gets the graphics.
     *
     * @return The graphics.
     */
    public Graphics[] getGraphics() {
        return graphics;
    }

    /**
     * Gets the reward.
     *
     * @return The reward.
     */
    public Item getReward() {
        return reward;
    }

    /**
     * Gets the jar.
     *
     * @return the jar.
     */
    public Item getJar() {
        return BUTTERFLY_JAR;
    }

}
