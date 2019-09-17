package org.gielinor.game.system.command.impl;

import java.util.HashMap;
import java.util.Map;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * A command for degrading item tests
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ArmourCommand extends Command {

    /**
     * A mapping of items to give by name
     */
    private final static Map<String, Item[]> items = new HashMap<>();

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
        return new String[]{ "arm", "armor", "armour" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("armour", "Gives armour set from name", getRights(),
            "::armour <lt>set_name><br>Set Names:"
                + "<br>bronze<br>iron<br>steel<br>black<br>mithril<br>adamant<br>rune"));
    }

    @Override
    public void execute(Player p, String[] args) {
        items.put("bronze", new Item[]{ new Item(1155), new Item(1117), new Item(1075), new Item(1189) });
        items.put("iron", new Item[]{ new Item(1153), new Item(1115), new Item(1067), new Item(1191) });
        items.put("steel", new Item[]{ new Item(1157), new Item(1119), new Item(1069), new Item(1193) });
        items.put("black", new Item[]{ new Item(1165), new Item(1125), new Item(1077), new Item(1195) });
        items.put("mithril", new Item[]{ new Item(1159), new Item(1121), new Item(1071), new Item(1197) });
        items.put("adamant", new Item[]{ new Item(1161), new Item(1123), new Item(1073), new Item(1199) });
        items.put("rune", new Item[]{ new Item(1163), new Item(1127), new Item(1079), new Item(1201) });
        if (args.length >= 2) {
            if (items.containsKey(args[1])) {
                for (Item add : items.get(args[1])) {
                    p.getInventory().add(add);
                }
            } else {
                p.getActionSender().sendMessage("Use as ::arm <lt>armor name>");
            }
        }
    }
}
