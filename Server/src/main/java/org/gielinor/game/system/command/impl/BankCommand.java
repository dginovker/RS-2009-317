package org.gielinor.game.system.command.impl;

import org.apache.commons.lang3.StringUtils;
import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * Opens a player's bank, or with arguments, spawns an item in the player's
 * bank.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class BankCommand extends Command {

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
        return new String[]{ "bank" };
    }

    @Override
    public boolean isBeta() {
        return true;
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("bank", "Opens bank, or spawns and item into it", getRights(),
            "::bank - Opens bank<br>::bank 4151 - Spawns an Abyssal whip in bank<br>::bank 11695 1000 - Spawns 1000 noted Armadyl Godswords into bank"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.getBank().open();
            return;
        }
        ItemDefinition itemDefinition = null;
        boolean noted = false;
        if (!StringUtils.isNumeric(args[1])) {
            if (args[1].endsWith("_noted")) {
                noted = true;
            }
            if (args.length >= 3 && !StringUtils.isNumeric(args[2])) {
                player.getActionSender().sendMessage("Use as ::item <lt>item_name> <lt>amount>");
                return;
            }
            itemDefinition = ItemDefinition.forName(args[1].replaceAll("_", " "));
        } else {
            itemDefinition = ItemDefinition.forId(Integer.parseInt(args[1]));
        }
        if (itemDefinition == null && !StringUtils.isNumeric(args[1])) {
            player.getActionSender().sendMessage("No item definition available for " + args[1]);
            return;
        }
        int id = itemDefinition.getId();
        if (noted) {
            id = itemDefinition.getNoteId();
        }
        int count = 1;
        if (args.length == 3) {
            String value = args[2];
            if (value.toLowerCase().contains("k")) {
                value = value.replaceAll("k", "000");
            } else if (value.toLowerCase().contains("m")) {
                value = value.replaceAll("m", "000000");
            } else if (value.toLowerCase().contains("b")) {
                value = value.replaceAll("b", "000000000");
            }
            count = Integer.parseInt(value);
        }
        // TODO FIX
        if (id == 0 && StringUtils.isNumeric(args[1]) && Integer.parseInt(args[1]) != 0) {
            id = Integer.parseInt(args[1]);
        }
        player.getBank().add(new Item(id, count));

    }
}
