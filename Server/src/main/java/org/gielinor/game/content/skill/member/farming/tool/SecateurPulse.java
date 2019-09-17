package org.gielinor.game.content.skill.member.farming.tool;

import org.gielinor.game.content.skill.member.farming.patch.PickingNode;
import org.gielinor.game.content.skill.member.farming.patch.TreeNode;
import org.gielinor.game.content.skill.member.farming.wrapper.PatchWrapper;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;

/**
 * Represents the pulse used when using secateurs.
 *
 * @author 'Vexia
 */
public final class SecateurPulse extends ToolAction {

    /**
     * Represents the watering animation to use.
     */
    private static final Animation ANIMATION = new Animation(2275);

    /**
     * Constructs a new {@code SecateurPulse} {@code Object}.
     */
    public SecateurPulse() {
        super(null, null, null);
    }

    /**
     * Constructs a new {@code SecateurPulse} {@code Object}.
     *
     * @param player  The player.
     * @param wrapper The wrapper.
     * @param item    The item.
     */
    public SecateurPulse(final Player player, final PatchWrapper wrapper, final Item item) {
        super(player, wrapper, item);
    }

    @Override
    public ToolAction newInstance(Player player, PatchWrapper wrapper, Item item) {
        return new SecateurPulse(player, wrapper, item);
    }

    @Override
    public boolean pulse() {
        if (ticks == 0) {
            player.animate(ANIMATION);
        }
        if (++ticks % 4 != 0) {
            return false;
        }
        if (!isReward(3)) {
            return false;
        }
        wrapper.getCycle().getDiseaseHandler().cure(player, false);
        return true;
    }

    @Override
    public void stop() {
        super.stop();
        player.getAnimator().reset();
    }

    @Override
    public boolean canInteract(String command) {
        if (wrapper.getNode() != null && !(wrapper.getNode() instanceof PickingNode || wrapper.getNode() instanceof TreeNode) || wrapper.isWeedy() || wrapper.isEmpty() || !wrapper.getCycle().getDiseaseHandler().isDiseased()) {
            player.getActionSender().sendMessage("Nothing interesting happens.");
            return false;
        }
        return true;
    }

}
