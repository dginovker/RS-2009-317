package org.gielinor.game.content.skill.free.fishing;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents a fishing option.
 *
 * @author Emperor
 */
public enum FishingOption {

    SMALL_NET(new Item(20303), 1, Animation.create(621), null, "net", Fish.SHRIMP, Fish.ANCHOVIE),
    BAIT(new Item(307), 5, Animation.create(622), new Item(313), "bait", Fish.SARDINE, Fish.HERRING),
    LURE(new Item(309), 20, new Animation(622), new Item(314), "lure", Fish.TROUT, Fish.SALMON, Fish.RAINBOW_FISH),
    L_BAIT(new Item(307), 25, Animation.create(622), new Item(313), "bait", Fish.PIKE),
    CAGE(new Item(301), 40, Animation.create(619), null, "cage", Fish.LOBSTER),
    HARPOON(new Item(311), 35, Animation.create(618), null, "harpoon", Fish.TUNA, Fish.SWORDFISH),
    BIG_NET(new Item(305), 16, Animation.create(620), null, "net", Fish.MACKEREL, Fish.COD, Fish.BASS),
    M_NET(new Item(389), 91, Animation.create(621), null, "net", Fish.MANTA_RAY),
    N_HARPOON(new Item(311), 76, Animation.create(618), null, "harpoon", Fish.SHARK),
    H_NET(new Item(20303), 1, Animation.create(621), null, "net", Fish.MONKFISH);

    /**
     * The tool required.
     */
    private final Item tool;

    /**
     * The fishing level required.
     */
    private final int level;

    /**
     * The fishing animation.
     */
    private final Animation animation;

    /**
     * The bait.
     */
    private final Item bait;

    /**
     * The option name.
     */
    private final String name;

    /**
     * The fish to catch.
     */
    private final Fish[] fish;

    /**
     * Constructs a new {@code FishingOption} {@code Object}.
     *
     * @param tool      The tool needed.
     * @param level     The fishing level required.
     * @param animation The animation.
     * @param fish      The fish to catch.
     */
    private FishingOption(Item tool, int level, Animation animation, Item bait, String name, Fish... fish) {
        this.tool = tool;
        this.level = level;
        this.animation = animation;
        this.bait = bait;
        this.name = name;
        this.fish = fish;
    }

    /**
     * Method used to get a random {@link Fish}.
     *
     * @return the {@link Fish}.
     */
    public Fish getRandomFish(final Player player) {
        if (this == BIG_NET) {
            switch (RandomUtil.randomize(100)) {
                case 0:
                    return Fish.OYSTER;
                case 50:
                    return Fish.CASKET;
                case 90:
                    return Fish.SEAWEED;
            }
        }
        Fish reward = fish[RandomUtil.randomize(fish.length)];
        if (reward.getLevel() > player.getSkills().getLevel(Skills.FISHING)) {
            reward = fish[0];
        }
        return reward;
    }

    /**
     * Gets the start message.
     *
     * @return The start message.
     */
    public String getStartMessage() {
        if (name.equals("net")) {
            return "You cast out your net...";
        }
        return "You attempt to catch a fish.";
    }

    /**
     * Gets the tool.
     *
     * @return The tool.
     */
    public Item getTool() {
        return tool;
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
     * Gets the animation.
     *
     * @return The animation.
     */
    public Animation getAnimation() {
        return animation;
    }

    /**
     * Gets the bait.
     *
     * @return The bait.
     */
    public Item getBait() {
        return bait;
    }

    /**
     * Gets the name.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the fish.
     *
     * @return The fish.
     */
    public Fish[] getFish() {
        return fish;
    }

}