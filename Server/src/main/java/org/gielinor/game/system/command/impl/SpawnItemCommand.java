package org.gielinor.game.system.command.impl;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Command used to spawn inventory items.
 *
 * @author Logan
 * @author Erik
 */
public class SpawnItemCommand extends Command {

    private static final Logger log = LoggerFactory.getLogger(SpawnItemCommand.class);

    /** Items that are Gielinor moderator exclusive in beta-mode worlds */
    private static final List<Integer> BETA_BLACKLISTED_ITEMS = Collections.unmodifiableList(Arrays.asList(
        Item.BAN_HAMMER,         // cannot be used by non-staff
        Item.ROTTEN_POTATO       // cannot be used by non-staff
    ));

    @Override
    public Rights getRights() {
        return Rights.GIELINOR_MODERATOR;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public boolean isBeta() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "item", "give", "pickup" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("item", "Spawns an item in inventory", getRights(),
            "::item [<lt>name> or <lt>item_id>] <lt>[ amount]><br>Examples:<br>::item abyssal_whip 3<br>::item armadyl_godsword<br>::item 4153<br>::item 1038 1"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.getActionSender().sendConsoleMessage(
                "Please use as ::item <lt><col=1b5cc4>item_id</col> OR <col=1b5cc4>item_name</col>> <lt><col=1b5cc4>amount</col>>");
        }

        ItemDefinition itemDefinition;
        int amount = 1;

        // Parse what item they are interested in.
        try {
            int id = Integer.parseInt(args[1]);
            itemDefinition = ItemDefinition.forId(id, false);
        } catch (NumberFormatException ignored) {
            String name = args[1].toLowerCase();
            boolean note = name.endsWith("_note");
            // The "_note" tag is not part of the item name!
            if (note)
                name = name.substring(0, name.length() - "_note".length());
            itemDefinition = ItemDefinition.forName(name.replaceAll("_", " "), false);
            // Find the noted version of the one we found, if it isn't noted already, if that's
            // what the user requested.
            if (note && itemDefinition != null && itemDefinition.isUnnoted()) {
                ItemDefinition noteDefinition = ItemDefinition.forId(itemDefinition.getNoteId(), false);
                if (noteDefinition == null)
                    player.getActionSender().sendConsoleMessage(itemDefinition.getName() + " cannot be noted.");
                else
                    itemDefinition = noteDefinition;
            }
        }

        // The requested item may not exist.
        if (itemDefinition == null) {
            player.getActionSender().sendConsoleMessage("No such item: <col=800000>" + args[1] + "</col>.");
            return;
        }

        // Blacklist certain items in beta mode (to non administrative users).
        if (World.getConfiguration().isBetaEnabled() && player.getRights().ordinal() < Rights.GIELINOR_MODERATOR.ordinal()) {
            if (BETA_BLACKLISTED_ITEMS.contains(itemDefinition.getId())) {
                player.getActionSender().sendConsoleMessage(itemDefinition.getName() + " is only available to Gielinor moderators.");
                return;
            }
        }

        // Perhaps they are picky about how many they would like
        if (args.length > 2) {
            String value = args[2].toLowerCase();
            try {
                // Do some pre-parsing to allow inputs such as "50k" and "15m" for
                // 50,000 and 15,000,000, respectively.
                if (value.endsWith("k")) {
                    value = value.substring(0, value.length() - 1) + "000";
                } else if (value.endsWith("m")) {
                    value = value.substring(0, value.length() - 1) + "000000";
                } else if (value.endsWith("b")) {
                    value = value.substring(0, value.length() - 1) + "000000000";
                }
                int parsedAmount = Integer.parseInt(value); // throws NumberFormatException if invalid input
                if (parsedAmount < 1) {
                    player.getActionSender().sendConsoleMessage(
                        "You'd want less than one? Weird! I'll give you one instead!");
                } else {
                    amount = parsedAmount;
                }
            } catch (NumberFormatException ignored) {
                if ("max".equals(value)) {
                    amount = Integer.MAX_VALUE;
                } else {
                    player.getActionSender().sendConsoleMessage(
                        "Failed to parse [<col=800000>" + value + "</col>] as amount. Giving you one instead.");
                }
            }
        }

        int currentCount = player.getInventory().getCount(itemDefinition.getId());
        player.getInventory().add(new Item(itemDefinition.getId(), amount));
        int addedCount = player.getInventory().getCount(itemDefinition.getId()) - currentCount;

        if (addedCount == 0)
            player.getActionSender().sendConsoleMessage("You cannot hold any more!");
        else if (addedCount == 1)
            player.getActionSender().sendConsoleMessage("Added item to inventory: " + itemDefinition.getName() + ".");
        else {
            NumberFormat formatter = NumberFormat.getInstance(Locale.US);
            player.getActionSender().sendConsoleMessage(
                "Added item to inventory: " + formatter.format(addedCount) + " x " + itemDefinition.getName() + ".");
        }

        log.debug("{} spawned {} x {} (id: {}).",
            player.getName(), addedCount, itemDefinition.getName(), itemDefinition.getId());
    }

}
