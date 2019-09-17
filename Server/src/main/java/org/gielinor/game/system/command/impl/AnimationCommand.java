package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.update.flag.context.Animation;

/**
 * Plays an animation.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class AnimationCommand extends Command {

    @Override
    public Rights getRights() {
        return Rights.GIELINOR_MODERATOR;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "anim", "animation", "oanim", "oa", "o" };
    }

    @Override
    public void init() {
        CommandDescription.add(
            new CommandDescription("oanim", "Plays an OSRS animation", getRights(), "::oanim <lt>animation id>"));
        CommandDescription
            .add(new CommandDescription("anim", "Plays an animation", getRights(), "::anim <lt>animation id>"));
    }

    // 3288
    @Override
    public void execute(Player player, String[] args) {
        if (args[0].equalsIgnoreCase("o")) {
            int animId = player.getAttribute("oa_a", 6000);
            // player.getAnimator().reset();
            player.getAnimator().forceAnimation(new Animation(animId + 16147, 0));
            player.getActionSender().sendMessage("Animation ID: " + animId);
            player.setAttribute("oa_a", animId + 1);
            return;
        }
        if (args.length < 2) {
            player.getActionSender().sendMessage("Use as ::anim <lt>animation id>");
            return;
        }
        int id = Integer.parseInt(args[1])
            + ((args[0].toLowerCase().startsWith("oanim") || args[0].toLowerCase().startsWith("oa")) ? 16147 : 0);
        int delay = args.length == 3 ? Integer.parseInt(args[2]) : 0;
        player.playAnimation(new Animation(id, delay));
    }
}
