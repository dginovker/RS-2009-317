package org.gielinor.game.system.command.impl;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * Gets an item ID by item name.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class GetIDCommand extends Command {

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
        return new String[]{ "geteid", "getid", "nameall" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("getid", "Gets the id of an item by the name", getRights(),
            "::getid <lt>item_name><br>Example:<br>::getid abyssal wh<br>::getid granite maul<br>::getid party"));
        CommandDescription.add(new CommandDescription("geteid", "Gets the id of an item by the exact name", getRights(),
            "::geteid <lt>item_name><br>Example:<br>::getid abyssal whip<br>::getid granite maul<br>::getid blue partyhat"));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length >= 2) {
            try {
                int results = 0;
                String res = "";
                String search = "";
                boolean exact = args[0].equalsIgnoreCase("geteid");
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    sb.append(args[i]);
                    if (i != args.length - 1) {
                        sb.append(" ");
                    }
                }
                if (sb.toString().isEmpty()) {
                    player.getActionSender().sendConsoleMessage("Use as ::" + args[0] + " <lt>item name>");
                    return;
                }
                search = sb.toString();
                for (ItemDefinition itemDef : ItemDefinition.getDefinitions().values()) {
                    if (itemDef == null) {
                        continue;
                    }
                    if (exact && !itemDef.getName().equalsIgnoreCase(search)) {
                        continue;
                    }
                    if (!exact && !itemDef.getName().toLowerCase().contains(search.toLowerCase())) {
                        continue;
                    }
                    String itemName = itemDef.getName();
                    res += ("<shad=1><col=FF9040>" + itemDef.getId() + " - " + itemName + " (" + itemDef.getValue()
                        + "gp)</col>\n");
                    results += 1;
                }
                if (results > 30) {
                    player.getActionSender().sendMessage("Too many results, please refine search.");
                }
                player.getActionSender()
                    .sendMessage(results + " items found for \"<col=306530>" + search + "</col>\"");
                if (results == 0) {
                    return;
                }
                String[] finalResults = res.split("\n");
                for (String message : finalResults) {
                    player.getActionSender().sendMessage(message);
                }
            } catch (Exception e) {
                player.getActionSender().sendMessage("Use as ::" + args[0] + " <lt>item name>");
            }
        } else {
            player.getActionSender().sendMessage("Use as ::" + args[0] + " <lt>item name>");
        }
    }

}
