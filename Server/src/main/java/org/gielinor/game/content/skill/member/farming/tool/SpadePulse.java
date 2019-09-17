package org.gielinor.game.content.skill.member.farming.tool;

import java.security.SecureRandom;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.farming.FarmingConstant;
import org.gielinor.game.content.skill.member.farming.FarmingPatch;
import org.gielinor.game.content.skill.member.farming.patch.HerbNode;
import org.gielinor.game.content.skill.member.farming.patch.PickingNode;
import org.gielinor.game.content.skill.member.farming.patch.TreeNode;
import org.gielinor.game.content.skill.member.farming.patch.Trees;
import org.gielinor.game.content.skill.member.farming.wrapper.PatchWrapper;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.model.container.impl.Equipment;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the pulse used when spade.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class SpadePulse extends ToolAction {

    /**
     * Represents the spade animation to use.
     */
    private static final Animation ANIMATION = new Animation(830);

    /**
     * Represents the herb picking animation.
     */
    private static final Animation HERB_ANIMATION = new Animation(2282);

    /**
     * Represents the roots obtained when digging up a stump.
     */
    private static final Item[] ROOTS = new Item[]{ new Item(6043), new Item(6045), new Item(6047), new Item(6049), new Item(6051) };

    /**
     * Represents the forced command.
     */
    private String command;

    /**
     * The extra crops with Magic secateurs.
     */
    private int extra = -1;

    /**
     * Constructs a new {@code RakePulse} {@code Object}.
     */
    public SpadePulse() {
        super(null, null, null);
    }

    /**
     * Constructs a new {@code RakePulse} {@code Object}.
     *
     * @param player  The player.
     * @param wrapper The wrapper.
     */
    public SpadePulse(final Player player, final PatchWrapper wrapper) {
        super(player, wrapper, null);
    }

    @Override
    public ToolAction newInstance(Player player, PatchWrapper wrapper, Item item) {
        return new SpadePulse(player, wrapper);
    }

    @Override
    public boolean pulse() {
        sendMessage();
        if (!isReward(3)) {
            return false;
        }
        boolean tree = false;
        if (wrapper.getNode() != null && wrapper.getNode() instanceof TreeNode) {
            TreeNode t = ((TreeNode) wrapper.getNode());
            if (t.isStump(wrapper.getCycle())) {
                tree = true;
            }
        }
        animate();
        if ((wrapper.getPatch() == FarmingPatch.BUSHES &&
            !command.equals("pick")) || (wrapper.getPatch() ==
            FarmingPatch.FRUIT_TREE && !command.equals("pick")) ||
            wrapper.getCycle().getDeathHandler().isDead() || wrapper.getCycle().getDiseaseHandler().isDiseased() || tree) {
            return clearPatch();
        } else if (wrapper.getPatch() == FarmingPatch.BUSHES || wrapper.getPatch() == FarmingPatch.FRUIT_TREE) {
            return pickBush();
        } else if (wrapper.getCycle().getGrowthHandler().isFullGrown()) {
            if (player.getEquipment().getNew(Equipment.SLOT_WEAPON).getId() == 7409 && extra == -1) {
                if (wrapper.getNode() instanceof HerbNode || wrapper.getName().toLowerCase().contains("allotment")) {
                    extra = new SecureRandom().nextInt(3) + 1;
                }
            }
            return !harvestPatch();
        } else if (wrapper.hasScarecrow()) {
            return removeScarecrow();
        }
        return true;
    }

    @Override
    public void stop() {
        super.stop();
        player.getAnimator().reset();
    }

    /**
     * Method used to send the beginning message.
     */
    private void sendMessage() {
        if (ticks == 0) {
            if ((wrapper.getPatch() != FarmingPatch.BUSHES)) {
                if ((wrapper.getPatch() == FarmingPatch.TREE || wrapper.getPatch() == FarmingPatch.FRUIT_TREE) && !command.equals("pick")) {
                    if (wrapper.getNode().isStump(wrapper.getCycle())) {
                        player.getActionSender().sendMessage("You start digging up the tree stump.");
                    } else {
                        player.getActionSender().sendMessage("You start digging the farming patch...");
                    }
                } else if (wrapper.getCycle().getGrowthHandler().isFullGrown()) {
                    player.getActionSender().sendMessage("You begin to harvest the " + wrapper.getName() + ".");
                } else {
                    player.getActionSender().sendMessage("You start digging the farming patch...");
                }
            }
            animate();
        }
    }

    /**
     * Method used to animate the player.
     */
    public void animate() {
        if (command != null && command.equals("pick")) {
            player.animate(HERB_ANIMATION);
            return;
        }
        player.animate(ANIMATION);
    }

    /**
     * Method used to clear the patch.
     *
     * @return <code>True</code> if cleared.
     */
    private boolean clearPatch() {
        boolean success = RandomUtil.random(5) < 3;
        if (success) {
            if (wrapper.getPatch() != FarmingPatch.TREE && wrapper.getPatch() != FarmingPatch.FRUIT_TREE) {
                player.getActionSender().sendMessage("You have successfully cleared this patch for new crops.");
            } else {
                if (wrapper.getNode().isStump(wrapper.getCycle())) {
                    if (wrapper.getPatch() == FarmingPatch.TREE) {
                        player.getSkills().addExperience(Skills.FARMING, 6);
                        player.getInventory().add(ROOTS[Trees.forNode(wrapper.getNode()).ordinal()], player);
                    }
                    player.getActionSender().sendMessage("You dig up the tree stump.");
                } else {
                    player.getActionSender().sendMessage("You have successfully cleared this patch for new crops.");
                }
            }
            wrapper.getCycle().clear(player);
        }
        return success;
    }

    /**
     * Method used to remove the scarecrow.
     *
     * @return <code>True</code> if so.
     */
    private boolean removeScarecrow() {
        wrapper.getCycle().clear(player);
        player.getInventory().add(FarmingConstant.SCARECROW);
        player.getActionSender().sendMessage("You remove the scarecrow.");
        return true;
    }

    /**
     * Method used to harvest the patch.
     *
     * @return <code>True</code> if harvested.
     */
    private boolean harvestPatch() {
        player.getInventory().add(wrapper.getNode().getProduct());
        player.getSkills().addExperience(Skills.FARMING, wrapper.getNode().getExperiences()[1]);
        int harvestAmount = wrapper.getCycle().getHarvestAmount();
        wrapper.getCycle().setHarvestAmount(extra > 0 ? harvestAmount : (harvestAmount - 1));
        if (extra > 0) {
            extra--;
        }
        if (wrapper.getCycle().getHarvestAmount() < 1) {
            wrapper.getCycle().clear(player);
            player.getActionSender().sendMessage("The " + wrapper.getName() + " patch is now empty.");
        }
        return wrapper.getNode() == null ||
            player.getInventory().hasRoomFor(wrapper.getNode().getProduct()) &&
                wrapper.getCycle().getHarvestAmount() > 0;
    }

    /**
     * Method used to pick the bush.
     *
     * @return <code>True</code> if so.
     */
    public boolean pickBush() {
        return wrapper.getNode().pick(wrapper.getCycle());
    }

    @Override
    public boolean canInteract(String command) {
        final boolean needSpade = (wrapper.getPatch() != FarmingPatch.FRUIT_TREE && wrapper.getPatch() != FarmingPatch.FLOWER && wrapper.getPatch() != FarmingPatch.BUSHES);
        if (!player.getInventory().containsItem(FarmingConstant.SPADE) && needSpade) {
            player.getActionSender().sendMessage("You need a spade to do that.");
            return false;
        }
        if (wrapper.hasScarecrow()) {
            if (!player.getInventory().hasRoomFor(FarmingConstant.SCARECROW)) {
                player.getActionSender().sendMessage("You don't have enough inventory space.");
                return false;
            }
            return true;
        }
        if (wrapper.isWeedy() || wrapper.isEmpty()) {
            player.getActionSender().sendMessage("There aren't any crops in this patch to dig up.");
            return false;
        }
        boolean t = wrapper.getNode() != null && wrapper.getNode() instanceof TreeNode;
        assert ((PickingNode) wrapper.getNode()) != null;
        if (((wrapper.getPatch() == FarmingPatch.BUSHES && ((PickingNode) wrapper.getNode()).getProductAmount(wrapper.getState()) != 0) && command.equals("force") && !wrapper.getCycle().getDiseaseHandler().isDiseased() && !wrapper.getCycle().getDeathHandler().isDead()) || (wrapper.getCycle().getGrowthHandler().isGrowing() && !wrapper.getCycle().getGrowthHandler().isFullGrown()) && !wrapper.getCycle().getDiseaseHandler().isDiseased() && (t ? !((TreeNode) wrapper.getNode()).isStump(wrapper.getCycle()) : true)) {
            player.getDialogueInterpreter().sendDialogues(player, null, "Dig up these healthy plants? Why would I want to do", "that?");
            return false;
        }
        if ((wrapper.getPatch() == FarmingPatch.TREE || wrapper.getPatch() == FarmingPatch.FRUIT_TREE && !command.equals("pick")) && !wrapper.getCycle().getDeathHandler().isDead() && !wrapper.getCycle().getDiseaseHandler().isDiseased()) {
            if (!wrapper.getNode().isStump(wrapper.getCycle())) {
                player.getActionSender().sendMessage("You can only dig up tree stumps.");
                return false;
            }
        }
        if (wrapper.getCycle().getGrowthHandler().isFullGrown() && !player.getInventory().hasRoomFor(wrapper.getNode().getProduct())) {
            player.getActionSender().sendMessage("You don't have enough inventory space.");
            return false;
        }
        this.command = command;
        return true;
    }

}
