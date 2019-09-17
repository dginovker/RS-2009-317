package org.gielinor.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.gielinor.rs2.config.DatabaseDetails;
import org.gielinor.rs2.config.DatabaseDetails.DatabaseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class DataSource {

    private static final Logger log = LoggerFactory.getLogger(DataSource.class);

    /**
     * The BoneCP connection pools (game and forum).
     */
    private static final Map<DatabaseType, HikariDataSource> connectionPools = new HashMap<>();

    /**
     * The array list of database variables to save and load.
     */
    private static List<DatabaseVariable> databaseVariables = new ArrayList<>();

    /**
     * Configures the connection pool.
     */
    private DataSource() throws IOException {
        DatabaseDetails.parseDetails();

        for (Entry<DatabaseType, DatabaseDetails> details : DatabaseDetails.getDatabases().entrySet()) {
            log.debug("{} - MySQL data connection pool being configured.", details.getValue().dbname);
            HikariConfig conf = new HikariConfig();
            conf.setJdbcUrl("jdbc:mysql://" + details.getValue().host + ":" + details.getValue().port + "/" + details.getValue().dbname);
            conf.setUsername(details.getValue().username);
            conf.setPassword(details.getValue().password);
            conf.setMaximumPoolSize(details.getValue().poolSize);
            conf.setMaxLifetime(TimeUnit.HOURS.toMillis(6));
            conf.setPoolName(details.getValue().dbname);
            conf.getDataSourceProperties().put("cachePrepStmts", "true");
            conf.getDataSourceProperties().put("prepStmtCacheSize", "250");
            conf.getDataSourceProperties().put("prepStmtCacheSqlLimit", "2048");
            conf.getDataSourceProperties().put("useServerPrepStmts", "true");
            conf.getDataSourceProperties().put("useLocalSessionState", "true");
            conf.getDataSourceProperties().put("useLocalTransactionState", "true");
            conf.getDataSourceProperties().put("rewriteBatchedStatements", "true");
            conf.getDataSourceProperties().put("cacheResultSetMetadata", "true");
            conf.getDataSourceProperties().put("cacheServerConfiguration", "true");
            conf.getDataSourceProperties().put("elideSetAutoCommits", "true");
            conf.getDataSourceProperties().put("maintainTimeStats", "false");
            connectionPools.put(details.getKey(), new HikariDataSource(conf));
        }
        log.info("{} connection pools initialized.", connectionPools.size());
    }

    public static Connection getForumConnection() throws SQLException, IOException {
        return getConnection(DatabaseType.FORUM);
    }

    public static Connection getGameConnection() throws SQLException, IOException {
        return getConnection(DatabaseType.GAME);
    }

    /**
     * Fetches a {@link java.sql.Connection}.
     *
     * @return The connection.
     * @throws SQLException
     */
    public static Connection getConnection(DatabaseType type) throws SQLException, IOException {
        if (connectionPools == null || connectionPools.size() == 0) {
            new DataSource();
        }
        return getConnectionPool(type).getConnection();
    }

    /**
     * Gets a BoneCP connection pool.
     *
     * @return the BoneCP connection pool
     */
    public static javax.sql.DataSource getConnectionPool(DatabaseType type) {
        return connectionPools.get(type);
    }

    /**
     * Gets all the {@code DatabaseVariable}s
     *
     * @return all the {@code DatabaseVariable}s
     */
    public static List<DatabaseVariable> getDatabaseVariables() {
        return databaseVariables;
    }

}
