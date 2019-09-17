package org.gielinor.game.system.command.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.content.eco.grandexchange.GEItemSet;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * Uses the input ::<code>set</code> to give the player items.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ItemSetCommand extends Command {

    /**
     * A mapping of items to give by name
     */
    private final static Map<String, int[]> items = new HashMap<>();

    static {
        items.put("dharoks", GEItemSet.DHAROKS.getComponents());
        items.put("ahrims", GEItemSet.AHRIMS.getComponents());
        items.put("guthans", GEItemSet.GUTHANS.getComponents());
        items.put("karils", GEItemSet.KARILS.getComponents());
        items.put("veracs", GEItemSet.VERACS.getComponents());
        items.put("torags", GEItemSet.TORAGS.getComponents());
        items.put("ranged", new int[]{ 892, 861, 6570, 2581, 2577, 2503, 2491, 2497, 1478, 6733 });
        items.put("melee", new int[]{ 7462, // barrow gloves
            6737, // berserker ring
            26585, // fury
            6570, // fire cape
            3751, // helm
            1127, // body
            1079, // legs
            1201, // kite
            4131, // boots
            4151, // whip
            4587 // scim
        });
        items.put("magic", new int[]{ 6918, // hat
            6916, // top
            6924, // bottoms
            6922, // gloves
            6920, // boots
            6889, // book
            6914, // wand
            4675, // staff
            2412 // cape
        });
        items.put("runes", new int[]{ 554, 555, 556, 557, 558, 559, 560, 561, 562, 563, 564, 565, 566, 9075 });
        items.put("potions", new int[]{ 14728, 2436, 2440, 2442, 2434, 3040, 2444 });
        items.put("food",
            new int[]{ 385, 385, 385, 385, 385, 385, 385, 385, 385, 385, 385, 385, 385, 385, 385, 385, 385, 385 });
        items.put("thirdmelee", GEItemSet.THIRD_AGE_MELEE.getComponents());
        items.put("thirdrange", GEItemSet.THIRD_AGE_RANGE.getComponents());
        items.put("thirdmage", GEItemSet.THIRD_AGE_MAGE.getComponents());
        items.put("skeletal", GEItemSet.SKELETAL.getComponents());
        items.put("spined", GEItemSet.SPINED.getComponents());
        items.put("bandos", new int[]{ 11696, 11724, 11726, 11728 });
        items.put("armadyl", new int[]{ 11694, 11718, 11720, 11722 });
        items.put("arma", new int[]{ 11694, 11718, 11720, 11722 });

    }

    @Override
    public Rights getRights() {
        return Rights.GIELINOR_MODERATOR;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public void init() {
        for (String command : items.keySet()) {
            String givenItems = "Gives:<br>";
            for (int itemId : items.get(command)) {
                ItemDefinition itemDefinition = ItemDefinition.forId(itemId);
                if (itemDefinition == null) {
                    continue;
                }
                givenItems += itemDefinition.getName() + "<br>";
            }
            if (command.equalsIgnoreCase("karils")) {
                givenItems += Integer.MAX_VALUE + "x Bolt rack";
            }
            CommandDescription
                .add(new CommandDescription(command, "Gives the command typed item set", getRights(), givenItems));
        }
    }

    @Override
    public String[] getCommands() {
        Set<String> commands = items.keySet();
        return commands.toArray(new String[commands.size()]);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!items.containsKey(args[0].toLowerCase())) {
            return;
        }
        for (int add : items.get(args[0].toLowerCase())) {
            Item item = new Item(add, 1);
            if (item.getDefinition() == null) {
                continue;
            }
            if (item.getDefinition().isStackable()) {
                item.setCount(Integer.MAX_VALUE);
            }
            player.getInventory().add(item);
        }
        if (args[0].equalsIgnoreCase("karils")) {
            player.getInventory().add(new Item(4740, Integer.MAX_VALUE));
        }
    }
}
