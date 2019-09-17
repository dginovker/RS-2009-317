package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles the search option.
 *
 * @author 'Vexia
 */
public class SearchOptionPlugin extends OptionHandler {

    /**
     * Represents an object to search.
     *
     * @author 'Vexia
     */
    public enum Search {
        /**
         * Represents a search.
         */
        DEFAULT(-1, new Item(1059, 1));

        public static Search forId(int id) {
            for (Search search : Search.values()) {
                if (search.getObject() == id) {
                    return search;
                }
            }
            return null;
        }

        /**
         * The object id.
         */
        private int object;

        /**
         * The item rewarded.
         */
        private Item item;

        /**
         * Constructs a new {@code SearchOptionPlugin.java} {@Code
         * Object}
         *
         * @param object
         * @param item
         */
        Search(int object, Item item) {
            this.object = object;
            this.item = item;
        }

        /**
         * @return the item.
         */
        public Item getItem() {
            return item;
        }

        /**
         * @return the object.
         */
        public int getObject() {
            return object;
        }
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (node.getName().equals("Bookcase")) {
            player.getActionSender().sendMessage("You search the books...");
            player.getActionSender().sendMessage("You find nothing of interest to you.");
            return true;
        }
    
        // motherload mine hammer crate
        if (node.getName().equals("Crate") && node.getLocation().equals(3752, 5674, 0)) {
            if (!player.getInventory().contains(Item.HAMMER)) {
                if (player.getInventory().add(Item.HAMMER)) {
                    player.getActionSender().sendMessage("You search the crate and find a hammer. How useful!");
                    return true;
                }
            }
        }
        
        player.getActionSender().sendMessage("You search the " + node.getName().toLowerCase() + " but find nothing.");
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.setOptionHandler("search", this);
        return this;
    }
}
