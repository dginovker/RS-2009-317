package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.Region;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.map.build.DynamicRegion;

/**
 * Sends the player's position in chat.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class PositionCommand extends Command {

    @Override
    public Rights getRights() {
        return Rights.PLAYER_MODERATOR;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "pos", "mypos", "coords", "loc" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("pos", "Displays current position information on the<br>world map", getRights(), null));
    }

    @Override
    public void execute(Player player, String[] args) {
        final Location l = player.getLocation();
        final Region r = player.getViewport().getRegion();
        player.getActionSender().sendMessage("Absolute: " + l + ", regional: [" + l.getLocalX() + ", " + l.getLocalY() + "], chunk: [" + l.getChunkOffsetX() + ", " + l.getChunkOffsetY() + "], flag: [" + RegionManager.isTeleportPermitted(l) + ", " + RegionManager.getClippingFlag(l) + ", " + RegionManager.isLandscape(l) + "].");
        player.getActionSender().sendMessage("Region: [id=" + l.getRegionId() + ", active=" + r.isActive() + ", instanced=" + ((r instanceof DynamicRegion)) + "], obj=" + RegionManager.getObject(l) + ".");
        player.getActionSender().sendMessage("Object: " + RegionManager.getObject(l) + ".");
    }

}
