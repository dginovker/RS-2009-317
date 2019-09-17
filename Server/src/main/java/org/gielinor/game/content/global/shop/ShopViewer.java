package org.gielinor.game.content.global.shop;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.component.CloseEvent;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.impl.AchievementTask;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Ironman;
import org.gielinor.game.node.item.Item;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.ContainerContext;
import org.gielinor.net.packet.context.InventoryInterfaceContext;
import org.gielinor.net.packet.out.ContainerPacket;
import org.gielinor.net.packet.out.InventoryInterface;
import org.gielinor.rs2.model.container.Container;

import plugin.interaction.inter.FurClothingInterface;
import plugin.interaction.inter.ShoppingPlugin;

/**
 * Represents the viewing of a shop.
 *
 * @author 'Vexia
 */
public final class ShopViewer {

    /**
     * Represents the main component.
     */
    private static final Component COMPONENT = new Component(3823).setCloseEvent(new ShopCloseEvent());

    /**
     * Represents the single tab component.
     */
    private static final Component SINGLE_TAB = new Component(3900);

    /**
     * Represents the player.
     */
    private final Player player;

    /**
     * Represents the shop being viewed.
     */
    private final Shop shop;

    /**
     *
     */

    /**
     * Constructs a new {@code ShopViewer} {@code Object}.
     *
     * @param player the player.
     * @param shop   the shop.
     */
    public ShopViewer(Player player, Shop shop) {
        this.player = player;
        this.shop = shop;
    }

    /**
     * Extends a shop a shop viewer to the player instance.
     *
     * @param player the player.
     * @param shop   the shop.
     * @return the viewer.
     */
    public static ShopViewer extend(final Player player, Shop shop) {
        ShopViewer viewer = player.getExtension(ShopViewer.class);
        if (viewer != null) {
            return viewer;
        }
        player.addExtension(ShopViewer.class, (viewer = new ShopViewer(player, shop)));
        return viewer;
    }

    /**
     * Method used to open the shop visual.
     */
    public void open() {
        if (!player.getRights().isAdministrator()) {
            if (Ironman.isIronman(player) && shop.getAccessibility() == Shop.ShopAccessibility.PLAYERS_ONLY) {
                player.getActionSender().sendMessage("You cannot use this shop.");
                player.getInterfaceState().close();
                return;
            }
            if (!Ironman.isIronman(player) && shop.getAccessibility() == Shop.ShopAccessibility.IRONMEN_ONLY) {
                player.getActionSender().sendMessage("You cannot use this shop.");
                player.getInterfaceState().close();
                return;
            }
        }
        update();
        shop.getViewers().add(this);
        player.getInterfaceState().setOpened(COMPONENT);
        player.getInterfaceState().setOverlay(SINGLE_TAB);
        PacketRepository.send(InventoryInterface.class, new InventoryInterfaceContext(player,
            new Component(3824), new Component(3822).setCloseEvent(new ShopCloseEvent())));
        Shops shops = Shops.forId(shop.getNpcs()[0]);
        String shopTitle = shop.getTitle();
        if (shops != null) {
            shopTitle = shops.getTitle(player);
        }
        if (shop == Shops.LOYALTY_POINT_SHOP.getShop()) {
            shopTitle = "Loyalty Point Shop (" + player.getSavedData().getGlobalData().getLoyaltyPoints() + " Point" + (player.getSavedData().getGlobalData().getLoyaltyPoints() == 1 ? "" : "s") + ")";
        }
        player.getActionSender().sendString(3901, shopTitle);
    }

    /**
     * Method used to update the contents on the component.
     */
    public void update() {
        PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 3823, 0, 0, player.getInventory().toArray(), 28, false));
        PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 3900, 0, 0, shop.getContainer(player, 0).toArray(), 40, false));
    }

    /**
     * Method used to buy an item from the shop.
     *
     * @param slot   the slot.
     * @param amount the amount.
     */
    public void buy(final int slot, int amount) {
        Shops shops = Shops.forId(shop.getNpcs()[0]);
        if (shops != null && shops.buy(player, slot, amount, shop)) {
            return;
        }
        if (player.getRights().isAdministrator() && !player.ignoreRestrictions()) {
            if (shop != Shops.CUSTOM_FUR_CLOTHING.getShop() && shop != Shops.LOYALTY_POINT_SHOP.getShop()) {
                player.getActionSender().sendMessage("You cannot buy items.");
                return;
            }
        }
        if (amount < 0) {
            return;
        }
        final Container container = shop.getContainer(player, 0);
        final Item item = container.get(slot);
        if (item == null) {
            return;
        }
        if (shop == Shops.CUSTOM_FUR_CLOTHING.getShop()) {
            FurClothingInterface.FurClothing furClothing = FurClothingInterface.FurClothing.forId(item.getId());
            if (furClothing == null) {
                player.getActionSender().sendMessage("Something went wrong... Please report this on forums (" + item.getId() + ").");
                return;
            }
            int index = furClothing.getIndex(item.getId());
            if (index == -1) {
                player.getActionSender().sendMessage("Something went wrong... Please report this on forums (" + item.getId() + ").");
                return;
            }
            Item fur = !furClothing.isAnyFur(index) ? furClothing.getFur(index) :
                !player.getInventory().containsItem(furClothing.getFur(index)) ?
                    furClothing.getFur(index, true) : furClothing.getFur(index);
            int requiredFurs = fur.getCount() * amount;
            int playerFur = player.getInventory().getCount(new Item(fur.getId()));
            if (playerFur < requiredFurs) {
                amount = 1;
                if (playerFur != 0) {
                    amount = playerFur / fur.getCount();
                }
            }
            if (amount < 1) {
                amount = 0;
            }
            furClothing.buy(player, index, amount);
            return;
        }
        if (item.getCount() < 1) {
            player.getActionSender().sendMessage("There is no stock of that item at the moment.");
            return;
        }
        if (amount > item.getCount()) {
            amount = item.getCount();
        }
        final Item add = new Item(item.getId(), amount);
        if (player.getInventory().getMaximumAdd(add) < amount) {
            add.setCount(player.getInventory().getMaximumAdd(add));
        }
        if (add.getCount() < 1 || !player.getInventory().hasRoomFor(add)) {
            player.getActionSender().sendMessage("Not enough space in inventory.");
            return;
        }
        if (shop == Shops.LOYALTY_POINT_SHOP.getShop()) {
            int cost = add.getCount() * ShoppingPlugin.getPriceCustomItem(add.getId());
            if (add.getDefinition().isStackable()) {
                cost = amount * ShoppingPlugin.getPriceCustomItem(add.getId());
                add.setCount(item.getCount() * amount);
            }
            if (player.getSavedData().getGlobalData().getLoyaltyPoints() < cost) {
                player.getActionSender().sendMessage("You don't have enough Loyalty points.");
                return;
            }
            if (player.getSavedData().getGlobalData().getLoyaltyPoints() >= cost) {
                player.getSavedData().getGlobalData().decreaseLoyaltyPoints(cost);
                player.getActionSender().sendString("Loyalty Point Shop (" + player.getSavedData().getGlobalData().getLoyaltyPoints() + " Point" + (player.getSavedData().getGlobalData().getLoyaltyPoints() == 1 ? "" : "s") + ")", 3901);
                player.getActionSender().sendMessage("You have " + player.getSavedData().getGlobalData().getLoyaltyPoints() + " Point" + (player.getSavedData().getGlobalData().getLoyaltyPoints() == 1 ? "" : "s") + " left.");
                player.getInventory().add(add);
                shop.update();
            }
            return;
        }
        // Regular shop
        final Item currency = new Item(shop.getCurrency(), add.getCount() * add.getDefinition().getMaxValue());
        if (!player.getInventory().contains(currency)) {
            player.getActionSender().sendMessage("You don't have enough " + ItemDefinition.forId(shop.getCurrency()).getName().toLowerCase() + ".");
            return;
        }
        if (player.getInventory().remove(currency)) {
            if (getShop().isGeneral() && !getShop().getContainer(null, 1).contains(add.getId())) {
                container.remove(add);
                container.shift();
            } else {
                container.removeOrZero(new Item(item.getId(), amount));
            }
            player.getInventory().add(add);
            AchievementDiary.finalize(player, AchievementTask.BUY_SHOP);
            shop.update();
        }
    }

    /**
     * Method used to sell an item to the shop.
     *
     * @param slot   the slot.
     * @param amount the amount.
     */
    public void sell(final int slot, int amount) {
        Shops shops = Shops.forId(shop.getNpcs()[0]);
        if (shops != null && shops.sell(player, slot, amount, shop)) {
            return;
        }
        if (shop == Shops.CUSTOM_FUR_CLOTHING.getShop()) {
            player.getActionSender().sendMessage("You can't sell items to this shop.");
            return;
        }
        if (player.getRights().isAdministrator() && !player.ignoreRestrictions()) {
            player.getActionSender().sendMessage("You cannot sell items.");
            return;
        }
        if (getShop() == Shops.LUNAR_EQUIPMENT.getShop() || getShop() == Shops.MOON_CLAN_FINE_CLOTHES.getShop() || getShop() == Shops.LOYALTY_POINT_SHOP.getShop()) {
            player.getActionSender().sendMessage("You cannot sell this item.");
            return;
        }
        if (amount < 0) {
            return;
        }
        final Item item = player.getInventory().get(slot);
        if (item == null) {
            return;
        }
        final ItemDefinition def = ItemDefinition.forId(item.getId());
        if (item.getDefinition().hasDestroyAction() || !def.isTradeable() || !shop.itemAllowed(player, item.getId()) && !shop.isGeneral() || shop.getCurrency() == 6529) {
            player.getActionSender().sendMessage("You can't sell this item to this shop.");
            return;
        }
        if (item.getId() == shop.getCurrency()) {
            player.getActionSender().sendMessage("You can't sell " + item.getName().toLowerCase() + " to a shop.");
            return;
        }
        final Container container = shop.getContainer(player);
        if (amount > player.getInventory().getCount(item)) {
            amount = player.getInventory().getCount(item);
        }
        Item add = new Item(item.getId(), amount);
        if (add.getCount() > container.getMaximumAdd(add)) {
            add.setCount(container.getMaximumAdd(add));
        }
        if (!container.hasRoomFor(add) || add.getCount() < 1) {
            player.getActionSender().sendMessage("The shop has ran out of space.");
            return;
        }
        final Item currency = new Item(shop.getCurrency(), shop.getSellingValue(player, add));
        if (!player.getInventory().hasRoomFor(currency)) {
            player.getActionSender().sendMessage("You don't have enough space for that many " + currency.getName().toLowerCase() + ".");
            return;
        }
        if (currency.getCount() > player.getInventory().getMaximumAdd(currency)) {
            currency.setCount(player.getInventory().getMaximumAdd(currency));
        }
        if (player.getInventory().remove(add, slot, true, false)) {
            if (!add.getDefinition().isUnnoted()) {
                add = new Item(add.getNoteChange(), add.getCount());
            }
            if (container.add(add)) {
                if (currency.getCount() > 0) {
                    player.getInventory().add(currency);
                }
                AchievementDiary.finalize(player, AchievementTask.SELL_SHOP);
                shop.update();
            }
        }
    }

    /**
     * Gets the player.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the shop.
     *
     * @return The shop.
     */
    public Shop getShop() {
        return shop;
    }

    /**
     * Represents the close event when a shop is closed.
     *
     * @author 'Vexia
     */
    public static final class ShopCloseEvent implements CloseEvent {

        @Override
        public void close(Player player, Component c) {
            final ShopViewer viewer = player.getExtension(ShopViewer.class);
            if (viewer == null) {
                return;
            }
            player.removeExtension(ShopViewer.class);
            viewer.getShop().getViewers().remove(viewer);
            player.getInterfaceState().closeSingleTab();
        }

        @Override
        public boolean canClose(Player player, Component component) {
            return true;
        }

    }
}
