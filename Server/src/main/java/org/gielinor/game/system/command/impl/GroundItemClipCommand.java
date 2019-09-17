package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.item.GroundItem;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionChunk;
import org.gielinor.game.world.map.RegionManager;

/**
 * Sends an Abyssal Whip item on each square that is clipped within the player's
 * region.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class GroundItemClipCommand extends Command {

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
        return new String[]{ "tgu", "groundclip", "clipground" };
    }

    @Override
    public void execute(final Player player, String[] args) {
        for (RegionChunk[] chunks : player.getViewport().getChunks()) {
            for (RegionChunk chunk : chunks) {
                for (int x = 0; x < RegionChunk.SIZE; x++) {
                    for (int y = 0; y < RegionChunk.SIZE; y++) {
                        if (chunk == null) {
                            System.out.println("Chunk null.");
                            continue;
                        }
                        if (chunk.getCurrentBase() == null) {
                            System.out.println("Current base null.");
                            continue;
                        }
                        Location l = chunk.getCurrentBase().transform(x, y, 0);
                        if (RegionManager.getClippingFlag(l.getZ(), l.getX(), l.getY()) == 0) {
                            continue;
                        }
                        GroundItemManager.create(new GroundItem(new Item(4151), l, player));
                    }
                }
            }
        }
    }
}
