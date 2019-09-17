package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;

/**
 * Performs an animation and graphic for the player.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class AnimationGraphicsCommand extends Command {

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
        return new String[]{ "ag", "animgfx" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("animgfx", "Plays an animation along with a graphic", getRights(),
            "::ag <lt>animation id> <lt>graphic id>"));
    }

    @Override
    public void execute(final Player player, String[] args) {
        if (args.length != 3) {
            player.getActionSender().sendMessage("Use as ::" + args[0] + " <lt>animation id> <lt>graphics id>");
            return;
        }
        player.visualize(Animation.create(Integer.parseInt(args[1])), Graphics.create(Integer.parseInt(args[2])));
    }
}
