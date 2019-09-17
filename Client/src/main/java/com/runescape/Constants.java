package com.runescape;

import com.runescape.cache.media.inter.InterfaceConfiguration;

import java.io.File;
import java.lang.reflect.Method;

/**
 * The main configuration for the Client
 *
 * @author 7Winds
 */
public class Constants {
    public static boolean isOSRSAnimation(int animationId) {
        return animationId == 21217 || animationId == 21216 || animationId == 21219 || animationId == 21220 || animationId == 21953 || animationId == 21954 || animationId == 21215;
    }

    public static boolean BANK_TABS = true;
    /**
     * If the client should automatically login.
     */
    public static boolean AUTO_LOGIN = false;
    public static int TEMP_X = 3, TEMP_Y = 3;
    /**
     * If we're dumping sprites.
     */
    public static final boolean DUMP_SPRITES = false;
    /**
     * The location to dump sprites.
     */
    public static final String SPRITE_DUMP_DIRECTORY = "null";
    /**
     * The count of cache indices.
     */
    public static final int CACHE_INDICES = 7;
    /**
     * The animation index.
     */
    public static final int ANIMATION_INDEX = 1;
    /**
     * The texture index.
     */
    public static final int TEXTURE_INDEX = 5;
    /**
     * The osrs animation index.
     */
    public static final int NEW_ANIMATION_INDEX = 6;
    /**
     * The OSRS model index.
     */
    public static final int NEW_MODEL_INDEX = 5;
    /**
     * The OSRS map index.
     */
    public static final int OSRS_MAP_INDEX = 8;
    /**
     * The default gameframe.
     */
    public static final int DEFAULT_GAMEFRAME = 474;
    /**
     * Saved configurations.
     */
    public static final int[] SAVED_CONFIGS = new int[]{
            InterfaceConfiguration.GAMEFRAME.getId()
    };
    /**
     * Achievements available on the server.
     */
    public static final int ACHIEVEMENT_AMOUNT = 3;
    /**
     * The URL to launch for a forgotten username or password.
     */
    public static final String FORGOTTEN_PASSWORD_URL = "https://gielinor.org/community/index.php?/lostpassword/";
    /**
     * The jaggrab host.
     */
    public final static String JAGGRAB_HOST = "173.212.224.246";
    /**
     * Entity face directions.
     */
    public static final int[] FACE_DIRECTIONS = new int[]{768, 1024, 1280, 512, 1536, 256, 0, 1792};
    /**
     * Toggles a security feature called RSA to prevent packet sniffers
     */
    public static final boolean ENABLE_RSA = true;
    /**
     * The private chat notification timer for clearing.
     */
    public static final int PRIVATE_CHAT_NOTIFICATION_TIMER = 300;
    /**
     * A string which indicates the Client's name.
     */
    public static final String CLIENT_NAME = "Gielinor";
    /**
     * The server's NPC bits for reading.
     */
    public static final int NPC_BITS = 18;
    /**
     * Represents minimap icon placement.
     */
    public static final int[][] MINIMAP_ICONS = new int[][]{
            new int[]{76, 2860, 5088},
            new int[]{5, 2848, 5105},
            new int[]{0, 2834, 5098},
            new int[]{55, 2848, 5091},
            new int[]{38, 2842, 5086},
            new int[]{27, 2848, 5071},
            new int[]{51, 2839, 5092},
            new int[]{19, 2833, 5086},
            new int[]{77, 2841, 5099},
            new int[]{27, 3049, 4962},
            new int[]{101, 2848, 5111}
    };
    /**
     * If we should debug the client.
     */
    public static boolean DEBUG_MODE = false;
    /**
     * The default world id (if applicable).
     */
    public static int DEFAULT_WORLD_ID = 1;
    /**
     * The login server address.
     */
    public static String LOGIN_SERVER_ADDRESS = "173.212.224.246";
    /**
     * The address of the server that the client will be connecting to.
     */
    public static String SERVER_ADDRESS = "173.212.224.246";
    /**
     * The port of the server that the client will be connecting to
     */
    public static int SERVER_PORT = 43594;
    /**
     * Whether or not we should use Jaggrab.
     */
    public static boolean JAGGRAB_ENABLED = false;
    /**
     * Whether or not we're dumping the cache CRCs and versions.
     */
    public static boolean DUMPING_CRCS = false;
    /**
     * The jaggrab server port.
     */
    public static int JAGGRAB_PORT = 43596;
    /**
     * The jaggrab service port.
     */
    public static int JAGGRAB_SERVICE_PORT = 43597;
    /**
     * Whether or not client-side clipping is enabled.
     */
    public static boolean CLIENT_SIDE_CLIPPING = false;
    /**
     * The directory of the working cache.
     */
    public static String CACHE_DIRECTORY = "LIVE";
    /**
     * Displays debug messages on loginscreen and in-game
     */
    public static boolean clientData = false;
    /**
     * Enables the use of music played through the client
     */
    public static boolean enableMusic = false;
    /**
     * Used for change worlds button on login screen
     */
    public static boolean WORLD_SWITCH = false;
    /**
     * The fog beginning depth.
     */
    public static int FOG_BEGIN_DEPTH = 1830;//2300;
    /**
     * The fog ending depth.
     */
    public static int FOG_END_DEPTH = 2500;//2900;
    /**
     * The fog colour.
     */
    public static int FOG_COLOUR = 0xA7C5C7;//0x66CCFF
    /**
     * Shows the ids of items, objects, and npcs on right click
     */
    public static boolean ENABLE_IDS = true;

    /**
     * Gets the working cache path.
     *
     * @param cacheDirectory If we should retrieve the working cache directory.
     * @return The path.
     */
    public static String getCachePath(boolean cacheDirectory) {
        File CACHE_PATH = new File(System.getProperty("user.home") + File.separator + CLIENT_NAME.toLowerCase() + (cacheDirectory ? File.separator + CACHE_DIRECTORY : ""));
        if (!CACHE_PATH.exists()) {
            CACHE_PATH.mkdir();
        }
        return CACHE_PATH.getPath();
    }

    /**
     * Launches a URL through the system.
     *
     * @param url The URL to launch.
     */
    public static void loadURL(String url) {
        String osName = System.getProperty("os.name");
        try {
            if (osName.startsWith("Mac OS")) {
                Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
                Method openURL = fileMgr.getDeclaredMethod("openURL", String.class);
                openURL.invoke(null, url);
            } else if (osName.startsWith("Windows")) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else {
                String[] browsers = {"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape", "safari"};
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++) {
                    if (Runtime.getRuntime().exec(new String[]{"which", browsers[count]}).waitFor() == 0) {
                        browser = browsers[count];
                    }
                }
                if (browser == null) {
                    throw new Exception("Could not find web browser");
                } else {
                    Runtime.getRuntime().exec(new String[]{browser, url});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the Grand Exchange Buy/Sell button ids to hide / show.
     *
     * @param slotIndex The slot index.
     * @return The buttons to hide.
     */
    public static int[] getSlotButtons(int slotIndex) {
        switch (slotIndex) {
            case 1:
                return new int[]{24926, 24927, 24929, 24930};
            case 2:
                return new int[]{24933, 24934, 24936, 24937};
            case 3:
                return new int[]{24940, 24941, 24943, 24944};
            case 4:
                return new int[]{24947, 24948, 24950, 24951};
            case 5:
                return new int[]{24954, 24955, 24957, 24958};
            case 6:
                return new int[]{24961, 24962, 24964, 24965};
        }
        return null;
    }

    /**
     * Gets the Grand Exchange text / progress ids to hide / show.
     *
     * @param slotIndex The slot index.
     * @return The ids to hide.
     */
    public static int[] getSlotProgress(int slotIndex) {
        switch (slotIndex) {
            case 1:
                return new int[]{24998, 24999};
            case 2:
                return new int[]{25004, 25005};
            case 3:
                return new int[]{25010, 25011};
            case 4:
                return new int[]{25016, 25017};
            case 5:
                return new int[]{25022, 25023};
            case 6:
                return new int[]{25028, 25029};
        }
        return null;
    }

    /**
     * Gets the Grand Exchange slots to hide / show.
     *
     * @param slotIndex The slot index.
     * @return The ids to hide.
     */
    public static int[] getSlots(int slotIndex) {
        switch (slotIndex) {
            case 1:
                return new int[]{
                        24967, 24968, 24969,
                        24994, 24995, 24996, 24997, 24998, 24999
                };
            case 2:
                return new int[]{
                        24971, 24972, 24973,
                        25000, 25001, 25002, 25003, 25004, 25005
                };
            case 3:
                return new int[]{
                        24975, 24976, 24977,
                        25006, 25007, 25008, 25009, 25010, 25011
                };
            case 4:
                return new int[]{
                        24979, 24980, 24981,
                        25012, 25013, 25014, 25015, 25016, 25017
                };
            case 5:
                return new int[]{
                        24983, 24984, 24985,
                        25018, 25019, 25020, 25021, 25022, 25023
                };
            case 6:
                return new int[]{
                        24987, 24988, 24989,
                        25024, 25025, 25026, 25027, 25028, 25029
                };
        }
        return null;
    }
}
