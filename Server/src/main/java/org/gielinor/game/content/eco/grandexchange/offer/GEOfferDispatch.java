package org.gielinor.game.content.eco.grandexchange.offer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.gielinor.database.DataSource;
import org.gielinor.game.content.eco.EcoStatus;
import org.gielinor.game.content.eco.EconomyManagement;
import org.gielinor.game.content.eco.grandexchange.BuyingLimitation;
import org.gielinor.game.content.eco.grandexchange.GrandExchangeDatabase;
import org.gielinor.game.content.eco.grandexchange.ResourceManager;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.callback.CallBack;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.rs2.task.TaskExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the Grand Exchange offers.
 *
 * @author Emperor
 * @author Logan G. <logan@Gielinor.org>
 */
public final class GEOfferDispatch extends Pulse implements CallBack {

    private static final Logger log = LoggerFactory.getLogger(GEOfferDispatch.class);

    /**
     * The update notification.
     */
    public static final String UPDATE_NOTIFICATION = "One or more of your grand exchange offers have been updated.";

    /**
     * The offset of the offer UIDs.
     */
    private static long offsetUID = 1;

    /**
     * The mapping of all current offers.
     */
    private static final Map<Long, GrandExchangeOffer> OFFER_MAPPING = new HashMap<>();

    /**
     * If the database should be dumped.
     */
    private static boolean dumpDatabase;

    /**
     * Initializes the Grand Exchange.
     */
    public static void init() {
        log.info("Requesting Grand Exchange data...");
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM grand_exchange")) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        long uid = resultSet.getLong("offer_uid");
                        int itemId = resultSet.getInt("item_id");
                        boolean sale = resultSet.getBoolean("sale");
                        GrandExchangeOffer grandExchangeOffer = new GrandExchangeOffer(itemId, sale);
                        grandExchangeOffer.setUid(uid);
                        grandExchangeOffer.setAmount(resultSet.getInt("amount"));
                        grandExchangeOffer.setCompletedAmount(resultSet.getInt("completed_amount"));
                        grandExchangeOffer.setOfferedValue(resultSet.getInt("offered_value"));
                        grandExchangeOffer.setTimeStamp(resultSet.getLong("timestamp"));
                        grandExchangeOffer.setState(OfferState.values()[resultSet.getByte("state")]);
                        grandExchangeOffer.setTotalCoinExchange(resultSet.getInt("total_coin_exchange"));
                        grandExchangeOffer.setPidn(resultSet.getInt("offer_pidn"));
                        if (!Objects.equals(resultSet.getString("withdraw"), "empty")) {
                            String[] withdraw = resultSet.getString("withdraw").split(":");
                            int[] values = new int[4];
                            for (int index = 0; index < values.length; index++) {
                                values[index] = Integer.parseInt(withdraw[index]);
                            }
                            grandExchangeOffer.getWithdraw()[0] = values[0] == -1 ? null : new Item(values[0], values[1]);
                            grandExchangeOffer.getWithdraw()[1] = values[2] == -1 ? null : new Item(values[2], values[3]);
                        }
                        OFFER_MAPPING.put(uid, grandExchangeOffer);
                        offsetUID++;
                    }
                }
            }
        } catch (SQLException | IOException ex) {
            log.error("Exception while loading Grand Exchange entries.", ex);
        }
        ResourceManager.init();
        log.info("Received and parsed {} Grand Exchange entries.", OFFER_MAPPING.size());
    }

    /**
     * Dumps the grand exchange offers.
     */
    public static void dump() {
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement prepareStatement = connection.prepareStatement("DELETE FROM grand_exchange")) {
                prepareStatement.executeUpdate();
            }
            if (OFFER_MAPPING.size() == 0) {
                ResourceManager.dump();
                return;
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO grand_exchange VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                for (long uid : OFFER_MAPPING.keySet()) {
                    GrandExchangeOffer grandExchangeOffer = OFFER_MAPPING.get(uid);
                    if (grandExchangeOffer == null) {
                        continue;
                    }
                    preparedStatement.setLong(1, uid);
                    preparedStatement.setInt(2, grandExchangeOffer.getItemId());
                    preparedStatement.setBoolean(3, grandExchangeOffer.isSell());
                    preparedStatement.setInt(4, grandExchangeOffer.getAmount());
                    preparedStatement.setInt(5, grandExchangeOffer.getCompletedAmount());
                    preparedStatement.setInt(6, grandExchangeOffer.getOfferedValue());
                    preparedStatement.setLong(7, grandExchangeOffer.getTimeStamp());
                    preparedStatement.setInt(8, grandExchangeOffer.getState().ordinal());
                    preparedStatement.setInt(9, grandExchangeOffer.getTotalCoinExchange());
                    preparedStatement.setInt(10, grandExchangeOffer.getPidn());
                    String withdraw = "empty";
                    if (grandExchangeOffer.getWithdraw()[0] != null || grandExchangeOffer.getWithdraw()[1] != null) {
                        int[][] withdraws = {
                            { grandExchangeOffer.getWithdraw()[0] == null ? -1
                                : grandExchangeOffer.getWithdraw()[0].getId(),
                                grandExchangeOffer.getWithdraw()[0] == null ? -1
                                    : grandExchangeOffer.getWithdraw()[0].getCount() },
                            { grandExchangeOffer.getWithdraw()[1] == null ? -1
                                : grandExchangeOffer.getWithdraw()[1].getId(),
                                grandExchangeOffer.getWithdraw()[1] == null ? -1
                                    : grandExchangeOffer.getWithdraw()[1].getCount() } };
                        withdraw = withdraws[0][0] + ":" + withdraws[0][1] + ":" + withdraws[1][0] + ":"
                            + withdraws[1][0];
                    }
                    preparedStatement.setString(11, withdraw);
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
        } catch (SQLException | IOException ex) {
            log.error("Exception while dumping Grand Exchange entries.", ex);
        }
        ResourceManager.dump();
    }

    @Override
    public boolean call() {
        init();
        setDelay(1);
        World.submit(this);
        return true;
    }

    @Override
    public boolean pulse() {
        if ((World.getTicks() % 24000) == 0) {
            OFFER_MAPPING.values().stream().filter(offer -> offer.isActive() && offer.isLimitation())
                .forEach(org.gielinor.game.content.eco.grandexchange.offer.GEOfferDispatch::updateOffer);
            BuyingLimitation.clear();
        }

        if (dumpDatabase) {
            TaskExecutor.execute(() -> {
                synchronized (GEOfferDispatch.this) {
                    dump();
                }
            });
            TaskExecutor.execute(() -> {
                synchronized (GEOfferDispatch.this) {
                    GrandExchangeDatabase.save();
                }
            });
            dumpDatabase = false;
        }
        return false;
    }

    /**
     * Dispatches an offer.
     *
     * @param player
     *            The player.
     * @param offer
     *            The grand exchange offer.
     * @return {@code True} if successful.
     */
    public static boolean dispatch(Player player, GrandExchangeOffer offer) {
        if (offer.getAmount() < 1) {
            player.getActionSender().sendMessage("You must choose the quantity you wish to buy!");
            return false;
        }
        if (offer.getOfferedValue() < 1) {
            player.getActionSender().sendMessage("You must choose the price you wish to buy for!");
            return false;
        }
        if (offer.getState() != OfferState.PENDING || offer.getUid() != 0) {
            return false;
        }
        offer.setPidn(player.getDetails().getPidn());
        offer.setUid(nextUID());
        offer.setState(OfferState.REGISTERED);
        OFFER_MAPPING.put(offer.getUid(), offer);
        offer.setTimeStamp(System.currentTimeMillis());
        player.getGrandExchange().update(offer);
        dumpDatabase = true;
        return true;
    }

    /**
     * Updates the offer.
     *
     * @param offer
     *            The G.E. offer to update.
     */
    public static void updateOffer(GrandExchangeOffer offer) {
        if (!offer.isActive()) {
            return;
        }
        for (GrandExchangeOffer o : OFFER_MAPPING.values()) {
            if (o.isSell() != offer.isSell() && o.getItemId() == offer.getItemId() && o.isActive()) {
                exchange(offer, o);
                if (offer.getState() == OfferState.COMPLETED) {
                    break;
                }
            }
        }
        if (offer.getState() != OfferState.COMPLETED) {
            for (GrandExchangeOffer o : ResourceManager.getStock()) {
                if (o.isSell() != offer.isSell() && o.getItemId() == offer.getItemId() && o.isActive()) {
                    exchange(offer, o);
                    if (offer.getState() == OfferState.COMPLETED) {
                        break;
                    }
                }
            }
        }
    }

    /**
     * Exchanges between 2 offers.
     *
     * @param offer
     *            The grand exchange offer to update.
     * @param o
     *            The other offer to exchange with.
     */
    private static void exchange(GrandExchangeOffer offer, GrandExchangeOffer o) {
        if (o.isSell() == offer.isSell()) {
            return;
        }
        if ((offer.isSell() && o.getOfferedValue() < offer.getOfferedValue())
            || (!offer.isSell() && o.getOfferedValue() > offer.getOfferedValue())) {
            return;
        }
        int amount = offer.getAmountLeft(true);
        if (amount > o.getAmountLeft(true)) {
            amount = o.getAmountLeft(true);
        }
        if (amount < 1) {
            return;
        }
        int coinDifference = offer.isSell() ? (o.getOfferedValue() - offer.getOfferedValue())
            : (offer.getOfferedValue() - o.getOfferedValue());
        if (coinDifference < 0) {
            return;
        }
        if (EconomyManagement.getEcoState() == EcoStatus.DRAINING) {
            coinDifference *= (1.0 - EconomyManagement.getModificationRate());
        }
        offer.setCompletedAmount(offer.getCompletedAmount() + amount);
        o.setCompletedAmount(o.getCompletedAmount() + amount);
        offer.setState(offer.getAmountLeft() < 1 ? OfferState.COMPLETED : OfferState.UPDATED);
        o.setState(o.getAmountLeft() < 1 ? OfferState.COMPLETED : OfferState.UPDATED);
        if (offer.isSell()) {
            if (offer.getAmountLeft() < 1 && offer.getPlayer() != null) {
                offer.getPlayer().getActionSender().sendSound(new Audio(4042, 1, 1));
            }
            offer.addWithdraw(Item.COINS, amount * offer.getOfferedValue());
            o.addWithdraw(o.getItemId(), amount);
            BuyingLimitation.updateBoughtAmount(o.getItemId(), o.getPidn(), amount);
        } else {
            if (o.getAmountLeft() < 1 && o.getPlayer() != null) {
                o.getPlayer().getActionSender().sendSound(new Audio(4042, 1, 1));
            }
            offer.addWithdraw(offer.getItemId(), amount);
            o.addWithdraw(Item.COINS, amount * o.getOfferedValue());
            BuyingLimitation.updateBoughtAmount(offer.getItemId(), offer.getPidn(), amount);
        }
        if (coinDifference > 0) {
            addCoinDifference(offer, o, coinDifference, amount);
        }
        offer.getEntry().influenceValue(offer.getOfferedValue());
        offer.notify(UPDATE_NOTIFICATION);
        o.notify(UPDATE_NOTIFICATION);
        dumpDatabase = true;
    }

    /**
     * Adds the coin difference between 2 offers.
     *
     * @param offer
     *            The offer.
     * @param o
     *            The other offer.
     * @param coinDifference
     *            The difference in prices.
     */
    private static void addCoinDifference(GrandExchangeOffer offer, GrandExchangeOffer o, int coinDifference,
        int amount) {
        if (!offer.isSell()) {
            offer.addWithdraw(Item.COINS, coinDifference * amount);
        } else {
            o.addWithdraw(Item.COINS, coinDifference * amount);
        }
    }

    /**
     * Gets the offer for the given UID.
     *
     * @param uid
     *            The unique ID given to the offer.
     * @return The grand exchange offer.
     */
    public static GrandExchangeOffer forUID(long uid) {
        return OFFER_MAPPING.get(uid);
    }

    /**
     * Removes the offer for the given UID.
     *
     * @param uid
     *            The UID.
     * @return {@code True} if successfully removed.
     */
    public static boolean remove(long uid) {
        return OFFER_MAPPING.remove(uid) != null;
    }

    /**
     * Gets the offerMapping.
     *
     * @return the offerMapping
     */
    public static Map<Long, GrandExchangeOffer> getOfferMapping() {
        return OFFER_MAPPING;
    }

    /**
     * Gets the next UID.
     *
     * @return The UID.
     */
    private static long nextUID() {
        long id = offsetUID++;
        if (id == 0) {
            return nextUID();
        }
        return id;
    }

    /**
     * Sets the dumping flag.
     *
     * @param dump
     *            The dump to set.
     */
    public static void setDumpDatabase(boolean dump) {
        GEOfferDispatch.dumpDatabase = dump;
    }

}
