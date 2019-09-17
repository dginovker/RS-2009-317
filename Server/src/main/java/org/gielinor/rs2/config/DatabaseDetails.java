package org.gielinor.rs2.config;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents database constants.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class DatabaseDetails {

    public static enum DatabaseType {

        FORUM,
        GAME;

        public static DatabaseType getType(String type) {
            type = type.toLowerCase();

            switch (type) {
                case "game":
                    return GAME;
                case "forum":
                case "forums":
                    return FORUM;
            }

            throw new IllegalArgumentException("no database: " + type);
        }

    }

    private static Map<DatabaseType, DatabaseDetails> databases = new HashMap<>();

    public static Map<DatabaseType, DatabaseDetails> getDatabases() {
        return databases;
    }

    public static DatabaseDetails getGameSingleton() {
        return databases.get(DatabaseType.GAME);
    }

    public static DatabaseDetails getForumSingleton() {
        return databases.get(DatabaseType.FORUM);
    }

    public static void parseDetails() throws YamlException, FileNotFoundException {
        YamlReader reader = new YamlReader(new FileReader(Constants.HIGHSCORE_VOTE_CONFIGURATION_PATH));
        while (true) { // TODO this is bad and it should probably be rewritten
            DatabaseDetails details = reader.read(DatabaseDetails.class);
            if (details == null) {
                break;
            }
            DatabaseType type = DatabaseType.getType(details.name);
            databases.put(type, details);
        }
    }

    /**
     * The name of the database (used for identifying which one is which in deserializing)
     */
    public String name;

    /**
     * MySQL Server host address.
     */
    public String host;

    /**
     * MySQL Port.
     */
    public String port;

    /**
     * MySQL Database name.
     */
    public String dbname;

    /**
     * MySQL Username.
     */
    public String username;

    /**
     * MySQL Password.
     */
    public String password;

    public int poolSize;

    /**
     * Spawned ground items table.
     */
    public final String GROUND_ITEM_TABLE = "ground_item_spawn";

}
