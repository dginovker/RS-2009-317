package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.world.map.zone.RegionZone;

public class OpenGrandExchangeCommand extends Command {

    @Override
    public Rights getRights() {
        return Rights.RUBY_MEMBER;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "ge", "grandexchange" };
    }

    @Override
    public void execute(Player player, String[] params) {
        for (RegionZone regionZone : player.getZoneMonitor().getZones()) {
            if (player.getZoneMonitor().isInZone(regionZone.getZone().getName())) {
                player.getActionSender().sendMessage("You cannot use that command here!");
                return;
            }
        }
        player.getGrandExchange().open();
    }
}
