package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.ai.AIPBuilder;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.parser.item.ItemConfiguration;

/**
 * Sets the player's gear to random items.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class RandomGearCommand extends Command {

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
        return new String[]{ "randomgear", "rg" };
    }

    @Override
    public void init() {
        CommandDescription
            .add(new CommandDescription("randomgear", "Sets your gear to random items.", getRights(), null));
        CommandDescription
            .add(new CommandDescription("rg", "Sets your gear to random items.", getRights(), null));
    }

    @Override
    public void execute(Player player, String[] args) {
        player.getEquipment().clear();
        player.getEquipment().refresh();
        player.getAppearance().sync();
        int slot = 0;
        for (slot = 0; slot < 13; slot++) {
            if (slot == 6 || slot == 8 || slot == 11) {
                continue;
            }
            Item item = new Item(AIPBuilder.getRandom(slot).getId());
            if (slot == 5) {
                if (player.getEquipment().get(3) != null && player.getEquipment().get(3).getDefinition()
                    .getConfiguration(ItemConfiguration.TWO_HANDED, false)) {
                    continue;
                }
            }
            player.getEquipment().add(item, true, slot);
        }
        player.getEquipment().refresh();
        player.getAppearance().sync();
    }
}
