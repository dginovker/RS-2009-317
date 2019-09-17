package org.gielinor.game.system.command.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * Empties the player's inventory.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class EmptyCommand extends Command {

    @Override
    public Rights getRights() {
        return Rights.GIELINOR_MODERATOR;
    }

    @Override
    public boolean isBeta() {
        return true;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "empty" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("empty", "Empties items from inventory", getRights(),
            "::empty <lt>[ items...]><br>Can be used as:<br>::empty 4151 - Keeps first item with 4151<br>::empty 1038 1040 - Keeps first items with ids of 1038 and 1040"));
    }

    @Override
    public void execute(Player player, String[] args) {
        Map<Integer, Item> keepItems = new HashMap<>();
        String[] keep = toString(args, 1).split(" ");
        if (player.getInventory().contains(5733)) {
            int slot = player.getInventory().getSlotById(5733);
            Item item = player.getInventory().get(slot);
            if (item != null) {
                keepItems.put(slot, item);
            }
        }
        for (String k : keep) {
            if (!StringUtils.isNumeric(k)) {
                continue;
            }
            if (!player.getInventory().contains(Integer.parseInt(k))) {
                continue;
            }
            int slot = player.getInventory().getSlotById(Integer.parseInt(k));
            Item item = player.getInventory().get(slot);
            if (item == null) {
                continue;
            }
            keepItems.put(slot, item);
        }
        player.getInventory().clear();
        if (keepItems.size() != 0) {
            for (int slot : keepItems.keySet()) {
                Item item = keepItems.get(slot);
                player.getInventory().add(item, true, slot);
            }
        }
    }
}
