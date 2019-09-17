package org.gielinor.game.system.command.impl;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.World;
import org.gielinor.rs2.pulse.Pulse;

public class GodmodeCommand extends Command {

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
        return new String[]{ "godmode", "god" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("god", "Toggles god mode on or off", getRights(), null));
    }

    @Override
    public void execute(final Player player, String[] args) {
        Pulse godModePulse = player.getAttribute("GOD_MODE_PULSE");
        if (godModePulse == null) {
            godModePulse = (new Pulse(1, player) {

                @Override
                public boolean pulse() {
                    player.getSkills().heal(player.getSkills().getStaticLevel(Skills.HITPOINTS));
                    player.getSkills().setLevel(Skills.PRAYER, player.getSkills().getStaticLevel(Skills.PRAYER));
                    return false;
                }

            });
            player.setAttribute("GOD_MODE_PULSE", godModePulse);
            World.submit(godModePulse);
            player.getActionSender().sendMessage("God mode has been enabled.");
        } else {
            godModePulse.stop();
            player.setAttribute("GOD_MODE_PULSE", null);
            player.removeAttribute("GOD_MODE_PULSE");
            player.getActionSender().sendMessage("God mode has been disabled.");
        }
    }

}
