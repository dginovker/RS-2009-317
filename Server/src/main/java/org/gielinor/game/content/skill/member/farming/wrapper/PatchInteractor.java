package org.gielinor.game.content.skill.member.farming.wrapper;

import org.gielinor.game.content.skill.SkillMenu;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.farming.FarmingConstant;
import org.gielinor.game.content.skill.member.farming.FarmingNode;
import org.gielinor.game.content.skill.member.farming.FarmingPatch;
import org.gielinor.game.content.skill.member.farming.patch.Allotments;
import org.gielinor.game.content.skill.member.farming.patch.Hops;
import org.gielinor.game.content.skill.member.farming.tool.PatchTool;
import org.gielinor.game.content.skill.member.farming.tool.ToolAction;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Perk;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the class of handling patch interactions.
 *
 * @author 'Vexia
 */
public final class PatchInteractor {

    /**
     * Represents the compost animation.
     */
    private static final Animation COMPOST_ANIMATION = new Animation(2283);

    /**
     * Represents the dibber animation.
     */
    private static final Animation DIBBER_ANIMATION = new Animation(2291);

    /**
     * Represents the picking animation (flower, etc).
     */
    private static final Animation PICK_ANIMATION = new Animation(2292);

    /**
     * Represents the spade animation.
     */
    private static final Animation SPADE_ANIMATION = new Animation(830);

    /**
     * Represents the patch wrapper.
     */
    private final PatchWrapper wrapper;

    /**
     * Represents the player instance.
     */
    private final Player player;

    /**
     * Constructs a new {@code PatchInteractor} {@code Object}.
     *
     * @param wrapper the wrapper.
     */
    public PatchInteractor(final PatchWrapper wrapper) {
        this.wrapper = wrapper;
        this.player = wrapper.getPlayer();
    }

    /**
     * Method used to handle the inspecting of a patch.
     */
    public void inspect() {
        final String[] messages = getInspectMessage(player);
        if (messages == null) {
            return;
        }
        messages[0] = messages[0].startsWith(" ") ? messages[0].substring(1) : messages[0];
        for (String message : messages) {
            if (message != null && message.length() != 0) {
                player.getActionSender().sendMessage(message);
            }
        }
        if (wrapper.getNode() != null) {
            if (wrapper.getCycle().isProtected()) {
                player.getActionSender().sendMessage("A nearby gardener is looking after this patch for you.");
            } else if (wrapper.getCycle().getNode().isFlowerProtected(wrapper.getCycle())) {
                if (wrapper.getCycle().getNode() != null && wrapper.getCycle().getNode() == Allotments.SWEETCORN.getFarmingNode()) {
                    player.getActionSender().sendMessage("A nearby scarecrow is giving protection for this patch.");
                } else {
                    player.getActionSender().sendMessage("A nearby flower is giving protection for this patch.");
                }
            }
        }
    }

    /**
     * Method used to open the guide of a patch.
     */
    public void openGuide() {
        if (wrapper.getPatch() != null) {
            SkillMenu.forId(Skills.FARMING).open(player, wrapper.getPatch().ordinal(), true);
        }
    }

    /**
     * Method used to handle the tool interaction with a patch.
     *
     * @param command the command.
     */
    public void handleToolInteraction(final Item item, String command) {
        if (item.getId() == FarmingConstant.COMPOST.getId() || item.getId() == FarmingConstant.SUPERCOMPOST.getId()) {
            applyCompost(item);
            return;
        }
        final ToolAction action = PatchTool.forItem(item).getAction().newInstance(player, wrapper, item);
        if (!action.canInteract(command)) {
            return;
        }
        player.getPulseManager().run(action);
    }

    /**
     * Method used to apply compost to a patch.
     *
     * @param item the item.
     */
    public void applyCompost(final Item item) {
        if (wrapper.isWeedy()) {
            player.getActionSender().sendMessage("This patch needs weeding first.");
            return;
        }
        if (!wrapper.isEmpty()) {
            player.getActionSender().sendMessage("This patch needs to be empty and weeded to do that.");
            return;
        }
        if ((wrapper.getCycle().getCompostThreshold() == 3) || wrapper.getCycle().isComposted() && ((item.getId() == FarmingConstant.COMPOST.getId() && wrapper.getCycle().getCompostThreshold() == 1) || (item.getId() == FarmingConstant.SUPERCOMPOST.getId() && wrapper.getCycle().getCompostThreshold() == 2))) {
            player.getActionSender().sendMessage("The " + wrapper.getName() + " patch has already been treated with " + wrapper.getCycle().getCompostName() + ".");
            return;
        }
        final boolean regular = item.getId() == FarmingConstant.COMPOST.getId();
        final int compostThreshold = wrapper.getCycle().getCompostThreshold();
        player.animate(COMPOST_ANIMATION);
        player.getInventory().replace(FarmingConstant.BUCKET, item.getSlot());
        player.getSkills().addExperience(Skills.FARMING, 18);
        wrapper.getCycle().setCompostThreshold((regular && (compostThreshold == 2) || !regular && compostThreshold == 1) ? 3 : (regular ? 1 : 2));
        if (wrapper.getPatch() == FarmingPatch.TREE) {
            player.getActionSender().sendMessage("You treat the tree patch with " + wrapper.getCycle().getCompostName() + ".");
        }
    }

    /**
     * Method used to prepare a patch.
     *
     * @param item the item.
     */
    public void plant(final Item item) {
        final FarmingNode node = wrapper.getPatch().forSeed(item);
        final boolean tree = (wrapper.getPatch() == FarmingPatch.TREE || wrapper.getPatch() == FarmingPatch.FRUIT_TREE);
        if (node == null) {
            player.getActionSender().sendMessage("You can't plant a " + item.getName().toLowerCase() + " in this patch.");
            return;
        }
        if (player.getSkills().getLevel(Skills.FARMING) < node.getLevel()) {
            player.getDialogueInterpreter().sendPlaneMessage("You must be a Level " + node.getLevel() + " Farmer to plant those.");
            return;
        }
        if (!player.getInventory().containsItem(FarmingConstant.SEED_DIBBER) && !tree) {
            player.getDialogueInterpreter().sendPlaneMessage("You need a seed dibber to plant seeds.");
            return;
        }
        if (tree && !player.getInventory().containsItem(FarmingConstant.SPADE)) {
            player.getActionSender().sendMessage("You need a spade to plant a sapling.");
            return;
        }
        final int seedRequirement = node == Hops.JUTE.getFarmingNode() ? 3 : wrapper.getPatch().getSeedRequirement();
        if (player.getInventory().getCount(item) < seedRequirement) {
            player.getActionSender().sendMessage("You need " + seedRequirement + " " + item.getName().toLowerCase() + "" + (wrapper.getPatch().getSeedRequirement() > 1 ? "s" : "") + " to grow those.");
            return;
        }
        if (wrapper.isWeedy()) {
            player.getActionSender().sendMessage("This patch needs weeding first.");
            return;
        }
        if (!wrapper.isEmpty()) {
            player.getDialogueInterpreter().sendPlaneMessage("You can only plant " + item.getName().toLowerCase() + " in an empty patch.");
            return;
        }
        boolean removeItems = true;
        if (player.getPerkManager().isTriggered(Perk.SEED_SAVIOR)) {
            removeItems = false;
        }
        final Item remove = tree ? item : new Item(item.getId(), seedRequirement);
        if (tree) {
            player.getInventory().replace(FarmingConstant.PLANT_POT, item.getSlot());
        }
        if (tree || player.getInventory().remove(remove)) {
            player.lock(3);
            wrapper.setNode(node);
            player.animate(tree ? SPADE_ANIMATION : DIBBER_ANIMATION);
            final boolean finalRemoveItems = removeItems;
            World.submit(new Pulse(1, player) {

                @Override
                public boolean pulse() {
                    player.getSkills().addExperience(Skills.FARMING, node.getExperiences()[0]);
                    if (!tree) {
                        String seeds = seedRequirement == 1 ? "a" : String.valueOf(seedRequirement);
                        player.getActionSender().sendMessage("You plant " + seeds + " " + item.getName().toLowerCase()
                            + (wrapper.getPatch().getSeedRequirement() > 1 ? "s" : "") + " in the " + wrapper.getName() + " patch.");
                    } else {
                        player.getActionSender().sendMessage("You plant the " + item.getName().toLowerCase() + " in the tree patch.");
                    }
                    if (!finalRemoveItems) {
                        if (tree) {
                            player.getInventory().remove(FarmingConstant.PLANT_POT);
                        }
                        player.getInventory().add(remove);
                        player.getActionSender().sendMessage("Your Seed savior perk saves the " + (tree ? "sapling " : "seed ") + "from being used!");
                    }
                    wrapper.addConfigValue(node.getBase());
                    wrapper.getCycle().getGrowthHandler().setGrowthUpdate();
                    return true;
                }
            });
        }
    }

    /**
     * Method used to harvest a patch with a spade.
     */
    public void harvest() {
        handleToolInteraction(FarmingConstant.SPADE, "force");
    }

    /**
     * Method used to pick a patch.
     */
    public void pick() {
        if (wrapper.getPatch() == FarmingPatch.FLOWER) {
            player.animate(PICK_ANIMATION);
            player.getPulseManager().run(new Pulse(3, player) {

                @Override
                public boolean pulse() {
                    player.getInventory().add(wrapper.getNode().getProduct(), player);
                    player.getSkills().addExperience(Skills.FARMING, wrapper.getNode().getExperiences()[1]);
                    wrapper.getCycle().clear(player);
                    return true;
                }
            });
        } else {
            handleToolInteraction(FarmingConstant.SPADE, "pick");
        }
    }

    /**
     * Method used to add a scarecrow.
     */
    public void addScarecrow() {
        if (wrapper.getPatch() != FarmingPatch.FLOWER) {
            player.getActionSender().sendMessage("You can only place a scarecrow in a flower patch.");
            return;
        }
        if (!wrapper.isEmpty() || wrapper.hasScarecrow()) {
            player.getActionSender().sendMessage("The patch needs to be empty in order to do that.");
            return;
        }
        if (player.getSkills().getLevel(Skills.FARMING) < 23) {
            player.getActionSender().sendMessage("You need a Farming level of at least 23 to do this.");
            return;
        }
        if (player.getInventory().remove(FarmingConstant.SCARECROW)) {
            wrapper.addConfigValue(36);
            wrapper.getCycle().getGrowthHandler().setGrowthUpdate();
            player.getActionSender().sendMessage("You place the scarecrow in the flower patch.");
        }
    }

    /**
     * Method used to check the health of the patch.
     */
    public void checkHealth() {
        wrapper.getNode().checkHealth(wrapper.getCycle());
    }

    /**
     * Gets the message displayed when the patch is inspected.
     *
     * @param player the player.
     * @return the messages.
     */
    private String[] getInspectMessage(final Player player) {
        if (wrapper.getName().equals("invalid")) {
            return null;
        }
        String name = "This is " + (TextUtils.isPlusN(wrapper.getName()) && wrapper.getPatch()
            != FarmingPatch.HOP ? "an" : "a") + " " + wrapper.getName() + " patch.";
        String soil = "The soil has not been treated.";
        String patchState = "The patch needs weeding.";
        if (wrapper.isWeedy()) {
            patchState = "The patch needs weeding.";
        } else if (wrapper.isEmpty()) {
            patchState = "The patch is empty and weeded.";
        }
        if (wrapper.getCycle().isComposted()) {
            soil = "The soil has been treated with " + wrapper.getCycle().getCompostName() + ".";
        }

        if (wrapper.getNode() != null) {
            if (wrapper.getCycle().getGrowthHandler().isGrowing()) {
                patchState = "The patch has something growing in it.";
            }
            if (wrapper.getCycle().getDiseaseHandler().isDiseased()) {
                patchState = "The patch is diseased and needs attending to before it dies.";
            }
            if (wrapper.getCycle().getDeathHandler().isDead()) {
                patchState = "The patch has become infected by disease and has died.";
            }
            if (wrapper.getCycle().getGrowthHandler().isFullGrown()) {
                patchState = "The patch is fully grown.";
            }
            if (wrapper.getNode().isStump(wrapper.getCycle())) {
                patchState = "The patch has the remains of a tree stump in it.";
            }
        }
        return spliceMessage(name + " " + soil + " " + patchState);
    }

    /**
     * Method used to splice the message into two.
     *
     * @param line the line.
     * @return the messages.
     */
    private String[] spliceMessage(String line) {
        final StringBuilder builder = new StringBuilder();
        final StringBuilder second = new StringBuilder();
        boolean useSecond = false;
        if (line.length() > 76) {
            String[] tokens = line.split(" ");
            for (String s : tokens) {
                s = " " + s;
                if (useSecond || builder.length() + s.length() > 86) {
                    second.append(s);
                    useSecond = true;
                } else {
                    builder.append(s);
                }
            }
        }
        if (builder.length() == 0) {
            builder.append(line);
        }
        return new String[]{ builder.toString(), second.toString() };
    }

    /**
     * Gets the wrapper.
     *
     * @return The wrapper.
     */
    public PatchWrapper getWrapper() {
        return wrapper;
    }

}
