package org.gielinor.game.content.global.shop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Ironman;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.callback.CallBack;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.model.container.ContainerType;
import org.gielinor.rs2.pulse.Pulse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class representing a shop.
 *
 * @author 'Vexia
 */
public final class Shop {
    
    private static final Logger log = LoggerFactory.getLogger(Shop.class);
    
    public enum ShopAccessibility {
        PLAYERS_ONLY,
        IRONMEN_ONLY,
        ALL_PLAYERS;
        
        public static ShopAccessibility forOrdinal(int ordinal) {
            switch (ordinal) {
                case 0:
                    return PLAYERS_ONLY;
                case 1:
                    return IRONMEN_ONLY;
                case 2:
                    return ALL_PLAYERS;
                default:
                    return PLAYERS_ONLY;
            }
        }
    }
    
    /**
     * Represents the general store items.
     */
    public final static Item[] GENERAL_STORE_ITEMS = {new Item(1931, 30), new Item(1935, 30), new Item(1735, 10), new Item(1925, 10), new Item(1923, 10), new Item(1887, 10), new Item(20590, 10), new Item(1755, 10), new Item(Item.HAMMER, 10), new Item(550, 10), new Item(9003, 10)};
    
    /**
     * Represents the shop containers.
     */
    private final Container[] containers = new Container[]{
        new Container(40, ContainerType.SHOP),
        new Container(40, ContainerType.SHOP), // Default stock
        new Container(40, ContainerType.SHOP) // Iron man
    };
    
    /**
     * Represents the list of shop viewers.
     */
    private final List<ShopViewer> viewers = new ArrayList<>();
    
    /**
     * Represents the title of the shop.
     */
    private String title;
    
    /**
     * Represents the items in the store.
     */
    public Item[] items;
    
    /**
     * Represents if it's a general store.
     */
    private boolean general;
    
    /**
     * Represents the accessibility capability of using this shop.
     * 0 = Players only, 1 = Ironmen only, 2 = Both
     */
    private ShopAccessibility accessibility;
    
    /**
     * Represents the currency the shop allows.
     */
    private int currency;
    
    /**
     * Represents the owners of the shop.
     */
    private int[] npcs;
    
    /**
     * How many times we've skipped reducing.
     */
    private int skipped = 0;
    
    /**
     * Constructs a new {@code Shop} {@code Object}.
     */
    public Shop(Shops shop) {
        try {
            ShopParser parser = new ShopParser().get(shop.name());
            this.title = parser.getTitle();
            this.items = parser.items;
            this.general = parser.getGeneral();
            this.currency = parser.getCurrency();
            this.accessibility = parser.getAccessibility();
            this.npcs = parser.getNpcs();
            
            this.getContainer(null, 0).add(this.items);
            this.getContainer(null, 1).add(this.items);
            this.getContainer(null, 2).add(this.items);
        } catch (NoSuchFieldException e) {
            log.error(e.getMessage());
        }
    }
    
    /**
     * Method used to open the shop.
     *
     * @param player the shop.
     */
    public void open(final Player player) {
        if (!player.getRights().isAdministrator()) {
            if (Ironman.isIronman(player) && getAccessibility() == ShopAccessibility.PLAYERS_ONLY) {
                player.getActionSender().sendMessage("This shop is unavailable in accessibility mode.");
                player.getInterfaceState().close();
                return;
            }
            if (!Ironman.isIronman(player) && getAccessibility() == ShopAccessibility.IRONMEN_ONLY) {
                player.getActionSender().sendMessage("This shop is only available in accessibility mode.");
                player.getInterfaceState().close();
                return;
            }
        }
        ShopViewer.extend(player, this).open();
    }
    
    /**
     * Method used to update the viewers.
     */
    public void update() {
        viewers.forEach(org.gielinor.game.content.global.shop.ShopViewer::update);
    }
    
    public void restock() {
        Container container = getContainer(null, 0);
        Container original = getContainer(null, 1);
        for (int i = 0; i < container.toArray().length; i++) {
            final Item item = container.toArray()[i];
            if (item == null) {
                continue;
            }
            boolean reduce = false;
            if (original.get(i) == null || original.toArray().length < i) {
                reduce = true;
            }
            if (!reduce) {
                if (item.getCount() < original.get(i).getCount()) {
                    item.setCount(item.getCount() + 1);
                }
                reduce = item.getCount() > original.get(i).getCount();
            }
            if (reduce && skipped > 3) {
                skipped = 0;
                int count = item.getCount() - 1;
                if (count < 1) {
                    container.remove(item);
                } else {
                    item.setCount(count);
                }
                container.shift();
            }
            skipped++;
        }
        update();
    }
    
    public void restockIronMan() {
        Container container = getContainer(null, 2);
        Container original = getContainer(null, 1);
        for (int i = 0; i < container.toArray().length; i++) {
            final Item item = container.toArray()[i];
            if (item == null) {
                continue;
            }
            boolean reduce = false;
            if (original.get(i) == null || original.toArray().length < i) {
                reduce = true;
            }
            if (!reduce) {
                if (item.getCount() < original.get(i).getCount()) {
                    item.setCount(item.getCount() + 1);
                }
                reduce = item.getCount() > original.get(i).getCount();
            }
            if (reduce && skipped > 3) {
                skipped = 0;
                int count = item.getCount() - 1;
                if (count < 1) {
                    container.remove(item);
                } else {
                    item.setCount(count);
                }
                container.shift();
            }
            skipped++;
        }
        update();
    }
    
    
    /**
     * Gets the value gained for selling this item to a certain shop.
     *
     * @param item The item to sell.
     * @return The value.
     */
    public int getSellingValue(Player player, Item item) {
        if (!item.getDefinition().isUnnoted()) {
            item = new Item(item.getNoteChange(), item.getCount());
        }
        double diff = item.getDefinition().isStackable() ? 0.005 : 0.05;
        double maxMod = 1.0 - (getContainer(player, 0).getCount(item) * diff);
        if (maxMod < 0.25) {
            maxMod = 0.25;
        }
        double minMod = maxMod - ((item.getCount() - 1) * diff);
        if (minMod < 0.25) {
            minMod = 0.25;
        }
        double mod = (maxMod + minMod) / 2;
        return (int) (item.getDefinition().getMinValue() * mod * item.getCount());
    }
    
    /**
     * Checks if the item is allowed to be sold to the shop.
     *
     * @param itemId the item id.
     * @return <code>True</code> if so.
     */
    public boolean itemAllowed(Player player, int itemId) {
        if (general && ItemDefinition.forId(itemId).isTradeable()) {
            return true;
        }
        for (Item item : getContainer(player, 0).toArray()) {
            if (item == null) {
                continue;
            }
            if (itemId == item.getId()) {
                return true;
            }
            if (ItemDefinition.forId(item.getId()).getNoteId() > 0) {
                if (ItemDefinition.forId(itemId).getNoteId() > 0 && ItemDefinition.forId(itemId).getNoteId() ==
                                                                        ItemDefinition.forId(item.getId()).getNoteId()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Gets the container the item should go to.
     *
     * @param player The player.
     * @return The container.
     */
    public Container getContainer(Player player) {
        return getContainer(player, 0);
    }
    
    /**
     * Gets the container on the slot.
     *
     * @param tabIndex the tab index.
     * @return the container.
     */
    public Container getContainer(Player player, int tabIndex) {
        if (tabIndex > containers.length) {
            throw new IndexOutOfBoundsException("Error! Shop tab index out of bounds.");
        }
        if (player != null && tabIndex == 0 && Ironman.isIronman(player)) {
            tabIndex = 2;
        }
        return containers[tabIndex];
    }
    
    /**
     * Gets the viewers.
     *
     * @return The viewers.
     */
    public List<ShopViewer> getViewers() {
        return viewers;
    }
    
    /**
     * Gets the title.
     *
     * @return The title.
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Gets the items.
     *
     * @param player The player.
     * @return The items.
     */
    public Item[] getItems(Player player) {
        return getContainer(player, 0).toArray();
    }
    
    /**
     * Gets the general.
     *
     * @return The general.
     */
    public boolean isGeneral() {
        return general;
    }
    
    /**
     *
     */
    public ShopAccessibility getAccessibility() {
        return accessibility;
    }
    
    /**
     * Gets the currency.
     *
     * @return The currency.
     */
    public int getCurrency() {
        return currency;
    }
    
    /**
     * Gets the npcs.
     *
     * @return The npcs.
     */
    public int[] getNpcs() {
        return npcs;
    }
    
    /**
     * Gets the containers.
     *
     * @return The containers.
     */
    public Container[] getContainers() {
        return containers;
    }
    
    /**
     * Represents the pulse used to restock shops.
     *
     * @author 'Vexia
     */
    public static final class RestockPulse extends Pulse implements CallBack {
        
        /**
         * Constructs a new {@code RestockPulse} {@code Object}.
         */
        public RestockPulse() {
            super(30);
        }
        
        @Override
        public boolean pulse() {
            for (Shops shop : Shops.values()) {
                shop.getShop().restock();
                if (shop.getShop().getAccessibility() == ShopAccessibility.ALL_PLAYERS) {
                    shop.getShop().restockIronMan();
                }
            }
            return false;
        }
        
        @Override
        public boolean call() {
            World.submit(this);
            return true;
        }
        
    }
    
}
