package org.gielinor.game.content.eco.grandexchange;

import org.gielinor.database.DataSource;
import org.gielinor.game.content.eco.grandexchange.offer.GrandExchangeEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the grand exchange database.
 *
 * @author Emperor
 * @author Logan G. <logan@Gielinor.org>
 */
public final class GrandExchangeDatabase {

    private static final Logger log = LoggerFactory.getLogger(GrandExchangeDatabase.class);

    /**
     * The grand exchange database mapping.
     */
    private static final Map<Integer, GrandExchangeEntry> ORIGINAL_DATABASE = new HashMap<>();

    /**
     * The grand exchange database mapping.
     */
    private static final Map<Integer, GrandExchangeEntry> DATABASE = new HashMap<>();

    /**
     * The minimum amount of unique trades required for an entry to change its
     * value.
     */
    private static final int MINIMUM_TRADES = 50; // TODO 200 Sounds fair, let
    // players decide early
    // stages

    /**
     * The amount of hours between each update cycle.
     */
    private static final int UPDATE_CYCLE_HOURS = 12;

    /**
     * The next update.
     */
    private static long nextUpdate;

    /**
     * If the G.E database has initialized.
     */
    private static boolean initialized;

    public static void init() {
        init(false);
    }

    public static void init(boolean reinitialize) {
        if (reinitialize) {
            DATABASE.clear();
        }
        log.info("Loading Grand Exchange data...");
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT next_update FROM grand_exchange_update")) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    nextUpdate = resultSet.first() ? resultSet.getLong("next_update") : System.currentTimeMillis() + 1;
                }
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT item_id, value, unique_trades, total_value, last_update FROM grand_exchange_data")) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int itemId = resultSet.getInt("item_id");
                        GrandExchangeEntry grandExchangeEntry = new GrandExchangeEntry(itemId);
                        GrandExchangeEntry originalGrandExchangeEntry = new GrandExchangeEntry(itemId);
                        grandExchangeEntry.setValue(resultSet.getInt("value"));
                        if (grandExchangeEntry.getValue() < 1) {
                            grandExchangeEntry.setValue(1);
                        }
                        grandExchangeEntry.setUniqueTrades(resultSet.getShort("unique_trades"));
                        grandExchangeEntry.setTotalValue(resultSet.getLong("total_value"));
                        grandExchangeEntry.setLastUpdate(resultSet.getLong("last_update"));
                        DATABASE.put(itemId, grandExchangeEntry);
                        originalGrandExchangeEntry.setValue(resultSet.getInt("value"));
                        if (originalGrandExchangeEntry.getValue() < 1) {
                            originalGrandExchangeEntry.setValue(1);
                        }
                        originalGrandExchangeEntry.setUniqueTrades(resultSet.getShort("unique_trades"));
                        originalGrandExchangeEntry.setTotalValue(resultSet.getLong("total_value"));
                        originalGrandExchangeEntry.setLastUpdate(resultSet.getLong("last_update"));
                        ORIGINAL_DATABASE.put(itemId, originalGrandExchangeEntry);
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT item_id, log_value FROM grand_exchange_log")) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    Map<Integer, ArrayList<Integer>> logValues = new HashMap<>();
                    ArrayList<Integer> logList = new ArrayList<>();
                    while (resultSet.next()) {
                        int itemId = resultSet.getInt("item_id");
                        int logValue = resultSet.getInt("log_value");
                        if (!logValues.containsKey(itemId)) {
                            logList.add(logValue);
                            logValues.put(itemId, logList);
                        } else {
                            logList = logValues.get(itemId);
                            logValues.replace(itemId, logList);
                        }
                    }
                    if (logValues.size() > 0) {
                        for (int itemId : logValues.keySet()) {
                            GrandExchangeEntry grandExchangeEntry = DATABASE.get(itemId);
                            if (grandExchangeEntry == null) {
                                continue;
                            }
                            int logLength = logValues.get(itemId).size();
                            grandExchangeEntry.setLogLength(logLength);
                            for (int index = 0; index < logLength; index++) {
                                grandExchangeEntry.getValueLog()[index] = logValues.get(itemId).get(index);
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("SQL exception while updating Grand Exchange data.", ex);
        } catch (IOException ex) {
            log.error("Error whilst updating Grand Exchange data.", ex);
        }
        checkUpdate();
        initialized = true;
        if (reinitialize) {
            DATABASE.clear();
        }
        log.info("Finished loading Grand Exchange data.");
    }

    /**
     * Updates the entry values, if needed.
     */
    public static void checkUpdate() {
        if (nextUpdate < System.currentTimeMillis()) {
            updateValues();
        }
    }

    /**
     * Saves the grand exchange database.
     */
    public static void save() {
        log.info("Saving Grand Exchange data.");
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("UPDATE grand_exchange_update SET next_update = ?")) {
                preparedStatement.setLong(1, nextUpdate);
                preparedStatement.executeUpdate();
            }
            if (DATABASE.size() > 0) {
                int changes = 0;
                for (GrandExchangeEntry grandExchangeEntry : DATABASE.values()) {
                    GrandExchangeEntry originalEntry = ORIGINAL_DATABASE.get(grandExchangeEntry.getItemId());
                    if ((grandExchangeEntry.getUniqueTrades() == originalEntry.getUniqueTrades())
                        || grandExchangeEntry.getTotalValue() == originalEntry.getTotalValue()) {
                        continue;
                    }
                    changes++;
                }
                if (changes != 0) {
                    try (PreparedStatement preparedStatement1 = connection.prepareStatement(
                        "UPDATE .grand_exchange_data SET `value` = ?, unique_trades = ?, total_value = ?, last_update = ? WHERE item_id = ?")) {
                        for (GrandExchangeEntry grandExchangeEntry : DATABASE.values()) {
                            GrandExchangeEntry originalEntry = ORIGINAL_DATABASE.get(grandExchangeEntry.getItemId());
                            if ((grandExchangeEntry.getUniqueTrades() == originalEntry.getUniqueTrades())
                                || grandExchangeEntry.getTotalValue() == originalEntry.getTotalValue()) {
                                continue;
                            }
                            originalEntry.setTotalValue(grandExchangeEntry.getTotalValue());
                            originalEntry.setUniqueTrades(grandExchangeEntry.getUniqueTrades());
                            preparedStatement1.setInt(1, grandExchangeEntry.getValue());
                            preparedStatement1.setInt(2, grandExchangeEntry.getUniqueTrades());
                            preparedStatement1.setLong(3, grandExchangeEntry.getTotalValue());
                            preparedStatement1.setLong(4, grandExchangeEntry.getLastUpdate());
                            preparedStatement1.setInt(5, grandExchangeEntry.getItemId());
                            preparedStatement1.addBatch();
                        }
                        preparedStatement1.executeBatch();
                    }
                }
                try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM grand_exchange_log")) {
                    preparedStatement.executeUpdate();
                }
                try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO grand_exchange_log VALUES(?, ?)")) {
                    for (GrandExchangeEntry grandExchangeEntry : DATABASE.values()) {
                        preparedStatement.setInt(1, grandExchangeEntry.getItemId());
                        for (int i = 0; i < grandExchangeEntry.getLogLength(); i++) {
                            preparedStatement.setInt(2, grandExchangeEntry.getValueLog()[i]);
                            preparedStatement.addBatch();
                        }
                    }
                    preparedStatement.executeBatch();
                }
            }
        } catch (SQLException | IOException ex) {
            log.error("Exception whilst saving Grand Exchange data.", ex);
        }
        log.info("Finished saving Grand Exchange data.");
    }

    /**
     * Updates the item values.
     */
    public static void updateValues() {
        try {
            for (GrandExchangeEntry entry : DATABASE.values()) {
                if (entry.getUniqueTrades() < MINIMUM_TRADES || entry.getTotalValue() == 0) {
                    continue;
                }
                double newAverage = entry.getTotalValue() / entry.getUniqueTrades();
                double changePercentage = newAverage / (entry.getValue() + .001);
                if (changePercentage == 1.0) {
                    continue;
                } else if (changePercentage > 1.05) {
                    changePercentage = 1.05;
                } else if (changePercentage < 0.95) {
                    changePercentage = 0.95;
                }
                int newValue = (int) (entry.getValue() * changePercentage);
                if (newValue == entry.getValue()) { // Fixes 1gp not being
                    // influenced.
                    newValue += 1;
                }
                entry.updateValue(newValue);
                entry.setLastUpdate(nextUpdate);
            }
        } catch (Throwable t) {
            log.error("Failed to update Grand Exchange values.", t);
        }
        nextUpdate = System.currentTimeMillis() + (UPDATE_CYCLE_HOURS * (60 * 60 * 1000));
    }

    /**
     * Gets the database.
     *
     * @return The database.
     */
    public static Map<Integer, GrandExchangeEntry> getDatabase() {
        return DATABASE;
    }

    /**
     * Gets the nextUpdate.
     *
     * @return The nextUpdate.
     */
    public static long getNextUpdate() {
        return nextUpdate;
    }

    /**
     * Sets the nextUpdate.
     *
     * @param nextUpdate
     *            The nextUpdate to set.
     */
    public static void setNextUpdate(long nextUpdate) {
        GrandExchangeDatabase.nextUpdate = nextUpdate;
    }

    /**
     * Checks if the grand exchange database has initialized.
     *
     * @return {@code True} if so.
     */
    public static boolean hasInitialized() {
        return initialized;
    }

}
