package org.gielinor.game.content.eco.grandexchange;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.database.DataSource;
import org.gielinor.game.content.eco.grandexchange.offer.GrandExchangeOffer;
import org.gielinor.game.content.eco.grandexchange.offer.OfferState;
import org.gielinor.game.content.global.consumable.Consumables;
import org.gielinor.game.content.global.consumable.Food;
import org.gielinor.game.content.skill.free.cooking.recipe.Recipe;
import org.gielinor.game.content.skill.free.crafting.GlassProduct;
import org.gielinor.game.content.skill.free.crafting.Hide;
import org.gielinor.game.content.skill.free.crafting.SilverProduct;
import org.gielinor.game.content.skill.free.crafting.armour.LeatherCrafting;
import org.gielinor.game.content.skill.free.crafting.armour.LeatherCrafting.DragonHide;
import org.gielinor.game.content.skill.free.crafting.armour.LeatherCrafting.SoftLeather;
import org.gielinor.game.content.skill.free.crafting.gem.Gems;
import org.gielinor.game.content.skill.free.crafting.jewellery.JewelleryCrafting;
import org.gielinor.game.content.skill.free.crafting.jewellery.JewelleryCrafting.JewelleryItem;
import org.gielinor.game.content.skill.free.crafting.pottery.PotteryItem;
import org.gielinor.game.content.skill.free.crafting.spinning.Spinnable;
import org.gielinor.game.content.skill.free.fishing.Fish;
import org.gielinor.game.content.skill.free.gather.SkillingResource;
import org.gielinor.game.content.skill.free.magic.Runes;
import org.gielinor.game.content.skill.free.runecrafting.Talisman;
import org.gielinor.game.content.skill.free.runecrafting.Tiara;
import org.gielinor.game.content.skill.free.smithing.smelting.Bar;
import org.gielinor.game.content.skill.member.farming.patch.Allotments;
import org.gielinor.game.content.skill.member.fletching.FletchItem;
import org.gielinor.game.content.skill.member.fletching.items.arrow.ArrowHead;
import org.gielinor.game.content.skill.member.fletching.items.bolts.Bolt;
import org.gielinor.game.content.skill.member.fletching.items.bow.StringBow;
import org.gielinor.game.content.skill.member.fletching.items.crossbow.Limb;
import org.gielinor.game.content.skill.member.fletching.items.darts.Dart;
import org.gielinor.game.content.skill.member.fletching.items.gem.Gem;
import org.gielinor.game.content.skill.member.herblore.FinishedPotion;
import org.gielinor.game.content.skill.member.herblore.GrindingItem;
import org.gielinor.game.content.skill.member.herblore.Herbs;
import org.gielinor.game.content.skill.member.herblore.UnfinishedPotion;
import org.gielinor.game.node.item.Item;
import org.gielinor.parser.item.ItemConfiguration;
import org.gielinor.utilities.misc.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages several resources being "pumped" into the game by creating offers in
 * the grand exchange.
 *
 * @author Emperor
 */
public final class ResourceManager {

    private static final Logger log = LoggerFactory.getLogger(ResourceManager.class);

    /**
     * The resources used for "kick-starting" the eco.
     */
    private static final int[] RESOURCES = { 3122, 4153, 6809, 10564, 10589, 1215, 4587, 1305, 1434, 7158, 3204, 1377,
        1249, 11212, 11230, 6523, 6527, 6528, 6525, 6524, 6128, 6129, 6130, 6131, 6133, 6135, 6137, 6139, 6141,
        6143, 6145, 6147, 6149, 6151, 6153 };

    /**
     * The current stock of resources.
     */
    private static final List<GrandExchangeOffer> STOCK = new ArrayList<>();

    /**
     * Loads the resources stock.
     */
    public static void init() {
        log.info("Requesting Grand Exchange resources...");
        try (Connection databaseConnection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = databaseConnection
                .prepareStatement("SELECT * FROM grand_exchange_resource")) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int itemId = resultSet.getInt("item_id");
                        boolean sale = resultSet.getBoolean("sale");
                        GrandExchangeOffer grandExchangeOffer = new GrandExchangeOffer(itemId, sale);
                        grandExchangeOffer.setAmount(resultSet.getInt("amount"));
                        grandExchangeOffer.setCompletedAmount(resultSet.getInt("completed_amount"));
                        grandExchangeOffer.setOfferedValue(resultSet.getInt("offered_value"));
                        int value = grandExchangeOffer.getOfferedValue();
                        int shopValue = ItemDefinition.forId(itemId).getValue();
                        if (value < (shopValue * 1.05)) {
                            value = (int) (shopValue * 1.05);
                        }
                        grandExchangeOffer.setOfferedValue(value);
                        grandExchangeOffer.setTimeStamp(resultSet.getLong("timestamp"));
                        grandExchangeOffer.setState(OfferState.values()[resultSet.getByte("state")]);
                        grandExchangeOffer.setTotalCoinExchange(resultSet.getInt("total_coin_exchange"));
                        grandExchangeOffer.setPidn(-1);
                        grandExchangeOffer.setUid(STOCK.size() + 1);
                        STOCK.add(grandExchangeOffer);
                    }
                }
            }
        } catch (SQLException | IOException ex) {
            log.error("Exception whilst attempting to request Grand Exchange resources.", ex);
        }
        log.info("Received and parsed {} Grand Exchange resources.", STOCK.size());
    }

    /**
     * Dumps the current resources.
     */
    public static void dump() {
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement prepareStatement = connection
                .prepareStatement("DELETE FROM grand_exchange_resource")) {
                prepareStatement.executeUpdate();
            }
            if (STOCK.size() == 0) {
                return;
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO grand_exchange_resource VALUES(?, ?, ?, ?, ?, ?, ?, ?)")) {
                for (GrandExchangeOffer grandExchangeOffer : STOCK) {
                    if (grandExchangeOffer == null || grandExchangeOffer.getState() == OfferState.COMPLETED) {
                        continue;
                    }
                    preparedStatement.setInt(1, grandExchangeOffer.getItemId());
                    preparedStatement.setBoolean(2, grandExchangeOffer.isSell());
                    preparedStatement.setInt(3, grandExchangeOffer.getAmount());
                    preparedStatement.setInt(4, grandExchangeOffer.getCompletedAmount());
                    preparedStatement.setInt(5, grandExchangeOffer.getOfferedValue());
                    preparedStatement.setLong(6, grandExchangeOffer.getTimeStamp());
                    preparedStatement.setInt(7, grandExchangeOffer.getState().ordinal());
                    preparedStatement.setInt(8, grandExchangeOffer.getTotalCoinExchange());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
        } catch (SQLException | IOException ex) {
            log.error("Exception whilst dumping Grand Exchange resources.", ex);
        }
    }

    /**
     * Clears the resource offer.
     *
     * @param itemId
     *            The item id to clear.
     */
    public static void clearResource(int itemId) {
        for (Iterator<GrandExchangeOffer> it = STOCK.iterator(); it.hasNext(); ) {
            GrandExchangeOffer offer = it.next();
            if (offer.getItemId() == itemId) {
                offer.setCompletedAmount(offer.getAmount());
                offer.setState(OfferState.COMPLETED);
                it.remove();
            }
        }
    }

    /**
     * Adds a new resource offer.
     *
     * @param itemId
     *            The item id.
     * @param amount
     *            The amount.
     * @param sell
     *            If the G.E should sell the resource.
     */
    public static void addResource(int itemId, int amount, boolean sell) {
        GrandExchangeOffer grandExchangeOffer = new GrandExchangeOffer(itemId, sell);
        if (grandExchangeOffer.getEntry() == null) {
            log.warn("Found no Grand Exchange entry for item: {}.", itemId);
            return;
        }
        grandExchangeOffer.setState(OfferState.REGISTERED);
        grandExchangeOffer.setAmount(amount);
        grandExchangeOffer.setOfferedValue((int) (new Item(itemId).getValue() * 1.05));
        grandExchangeOffer.setPidn(-1);
        grandExchangeOffer.setUid(STOCK.size() + 1);
        grandExchangeOffer.setTimeStamp(System.currentTimeMillis());
        STOCK.add(grandExchangeOffer);
    }

    /**
     * "Kick starts" the economy by adding resources to the Grand Exchange.
     */
    public static void kickStartEconomy() {
        List<Integer> handledResources = new ArrayList<>();
        int id;
        for (int itemId : RESOURCES) {
            handledResources.add(itemId);
        }
        for (SkillingResource r : SkillingResource.values()) {
            if (!handledResources.contains(id = r.getReward())) {
                if (id == -1) {
                    continue;
                }
                handledResources.add(id);
            }
        }
        for (Consumables c : Consumables.values()) {
            if (c.getConsumable() instanceof Food) {
                Food f = c.getConsumable().asFood();
                if (f.getRaw() != null && !handledResources.contains(id = f.getRaw().getId())) {
                    handledResources.add(id);
                }
            }
            if (!handledResources.contains(id = c.getConsumable().getItem().getId())) {
                handledResources.add(id);
            }
        }
        for (Recipe r : Recipe.RECIPES) {
            for (Item item : r.getIngredients()) {
                if (!handledResources.contains(id = item.getId())) {
                    handledResources.add(id);
                }
            }
            if (!handledResources.contains(id = r.getBase().getId())) {
                handledResources.add(id);
            }
        }
        for (DragonHide d : LeatherCrafting.DragonHide.values()) {
            if (!handledResources.contains(id = d.getLeather())) {
                handledResources.add(id);
            }
            if (!handledResources.contains(id = d.getProduct())) {
                handledResources.add(id);
            }
        }
        for (SoftLeather s : LeatherCrafting.SoftLeather.values()) {
            if (!handledResources.contains(id = s.getProduct().getId())) {
                handledResources.add(id);
            }
        }
        for (Gems g : Gems.values()) {
            if (!handledResources.contains(id = g.getGem().getId())) {
                handledResources.add(id);
            }
            if (!handledResources.contains(id = g.getUncut().getId())) {
                handledResources.add(id);
            }
        }
        for (JewelleryItem d : JewelleryCrafting.JewelleryItem.values()) {
            if (!handledResources.contains(id = d.getSendItem())) {
                handledResources.add(id);
            }
            for (int item : d.getItems()) {
                if (!handledResources.contains(id = item)) {
                    handledResources.add(id);
                }
            }
        }
        for (PotteryItem item : PotteryItem.values()) {
            if (!handledResources.contains(id = item.getUnfinished().getId())) {
                handledResources.add(id);
            }
            if (!handledResources.contains(id = item.getProduct().getId())) {
                handledResources.add(id);
            }
        }
        for (Spinnable spinnable : Spinnable.values()) {
            if (!handledResources.contains(id = spinnable.getItem().getId())) {
                handledResources.add(id);
            }
            if (!handledResources.contains(id = spinnable.getProduct().getId())) {
                handledResources.add(id);
            }
        }
        for (GlassProduct s : GlassProduct.values()) {
            if (!handledResources.contains(id = s.getProduct())) {
                handledResources.add(id);
            }
        }
        for (SilverProduct s : SilverProduct.values()) {
            if (!handledResources.contains(id = s.getNeeded())) {
                handledResources.add(id);
            }
            if (!handledResources.contains(id = s.getProduct())) {
                handledResources.add(id);
            }
            if (!handledResources.contains(id = s.getStrung())) {
                handledResources.add(id);
            }
        }
        for (Hide hide : Hide.values()) {
            if (!handledResources.contains(id = hide.getRaw().getId())) {
                handledResources.add(id);
            }
            if (!handledResources.contains(id = hide.getProduct().getId())) {
                handledResources.add(id);
            }
        }
        for (Fish f : Fish.values()) {
            if (!handledResources.contains(id = f.getItem().getId())) {
                handledResources.add(id);
            }
        }
        for (Runes r : Runes.values()) {
            if (!handledResources.contains(id = r.getId())) {
                handledResources.add(id);
            }
        }
        for (Talisman t : Talisman.values()) {
            if (!handledResources.contains(id = t.getTalisman().getId())) {
                handledResources.add(id);
            }
        }
        for (Tiara t : Tiara.values()) {
            if (!handledResources.contains(id = t.getTiara().getId())) {
                handledResources.add(id);
            }
        }
        for (Bar bar : Bar.values()) {
            if (!handledResources.contains(id = bar.getProduct().getId())) {
                handledResources.add(id);
            }
        }
        for (Allotments a : Allotments.values()) {
            if (!handledResources.contains(id = a.getFarmingNode().getSeed().getId())) {
                handledResources.add(id);
            }
            if (!handledResources.contains(id = a.getFarmingNode().getProduct().getId())) {
                handledResources.add(id);
            }
        }
        for (ArrowHead h : ArrowHead.values()) {
            if (!handledResources.contains(id = h.getProduct().getId())) {
                handledResources.add(id);
            }
        }
        for (Dart d : Dart.values()) {
            if (!handledResources.contains(id = d.getProduct().getId())) {
                handledResources.add(id);
            }
        }
        for (Bolt b : Bolt.values()) {
            if (!handledResources.contains(id = b.getProduct().getId())) {
                handledResources.add(id);
            }
        }
        for (StringBow b : StringBow.values()) {
            if (!handledResources.contains(id = b.getItem().getId())) {
                handledResources.add(id);
            }
            if (!handledResources.contains(id = b.getProduct().getId())) {
                handledResources.add(id);
            }
        }
        for (Limb l : Limb.values()) {
            if (!handledResources.contains(id = l.getLimb().getId())) {
                handledResources.add(id);
            }
            if (!handledResources.contains(id = l.getStock().getId())) {
                handledResources.add(id);
            }
            if (!handledResources.contains(id = l.getProduct().getId())) {
                handledResources.add(id);
            }
        }
        for (Gem g : Gem.values()) {
            if (!handledResources.contains(id = g.getBoltTip().getId())) {
                handledResources.add(id);
            }
        }
        for (FletchItem f : FletchItem.values()) {
            if (!handledResources.contains(id = f.getProduct().getId())) {
                handledResources.add(id);
            }
        }
        for (Herbs h : Herbs.values()) {
            if (!handledResources.contains(id = h.getHerb().getId())) {
                handledResources.add(id);
            }
            if (!handledResources.contains(id = h.getProduct().getId())) {
                handledResources.add(id);
            }
        }
        for (GrindingItem g : GrindingItem.values()) {
            if (!handledResources.contains(id = g.getProduct().getId())) {
                handledResources.add(id);
            }
            for (Item i : g.getItems()) {
                if (!handledResources.contains(id = i.getId())) {
                    handledResources.add(id);
                }
            }
        }
        for (FinishedPotion f : FinishedPotion.values()) {
            if (!handledResources.contains(id = f.getIngredient().getId())) {
                handledResources.add(id);
            }
            if (!handledResources.contains(id = f.getPotion().getId())) {
                handledResources.add(id);
            }
        }
        for (UnfinishedPotion f : UnfinishedPotion.values()) {
            if (!handledResources.contains(id = f.getIngredient().getId())) {
                handledResources.add(id);
            }
            if (!handledResources.contains(id = f.getPotion().getId())) {
                handledResources.add(id);
            }
            if (!handledResources.contains(id = f.getBase().getId())) {
                handledResources.add(id);
            }
        }
        // for (SummoningPouch s : SummoningPouch.values()) {
        // for (Item item : s.getItems()) {
        // if (!handledResources.contains(id = item.getId())) {
        // handledResources.add(id);
        // }
        // }
        // }
        for (int itemId : handledResources) {
            ItemDefinition itemDefinition = ItemDefinition.forId(itemId);
            if (itemDefinition == null) {
                log.warn("Missing item definition for item: {}.", itemId);
                continue;
            }
            int amount = itemDefinition.getConfiguration(ItemConfiguration.GE_LIMIT, 500) * RandomUtil.random(1, 7);
            addResource(itemId, amount, true);
        }
    }

    /**
     * Gets the stock.
     *
     * @return The stock.
     */
    public static List<GrandExchangeOffer> getStock() {
        return STOCK;
    }

}
