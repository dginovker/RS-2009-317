package org.gielinor.game.system.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gielinor.game.world.callback.CallBack;
import org.gielinor.rs2.config.Constants;
import org.gielinor.utilities.misc.FileOperations;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A utility class for fetching data on demand.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 *         TODO Convert to MySQL
 */
public class DataShelf implements CallBack {

    private static final Logger log = LoggerFactory.getLogger(DataShelf.class);

    /**
     * The mapping of data that has been parsed.
     */
    private static final Map<String, JSONObject> DATA = new HashMap<>();

    /**
     * Checks if the given JSON file exists.
     *
     * @param name The name of the file without the extension.
     * @return <code>True</code> if it exists.
     */
    private static boolean exists(String name) {
        File jsonFile = new File(Constants.ONDEMAND_PATH + "/" + name + ".json");
        if (!jsonFile.exists()) {
            log.warn("On-demand JSON file [{}] is missing.", jsonFile.getName());
            return false;
        }
        return true;
    }

    /**
     * Fetches data from a JSON file.
     *
     * @param name     The name of the file without the extension.
     * @param ondemand Whether or not to use on-demand.
     * @return The mapping of the data.
     */
    public static JSONObject fetch(String name, boolean ondemand) {
        if (!exists(name)) {
            return null;
        }
        if (ondemand) {
            return fetch(name);
        }
        JSONObject jsonObject = DATA.get(name);
        if (jsonObject == null) {
            log.warn("Missing data mapping for JSON: {}.", name);
            return null;
        }
        DATA.remove(name);
        return jsonObject;
    }

    /**
     * Fetches data from a JSON file on demand.
     *
     * @param name The name of the file without the extension.
     * @return The mapping of the data.
     */
    public static JSONObject fetch(String name) {
        if (!exists(name)) {
            return null;
        }
        String fileName = name + ".json";
        if (DATA.get(name) != null) {
            DATA.remove(name);
        }
        try {
            File file = new File(Constants.ONDEMAND_PATH + "/" + fileName);
            JSONObject jsonObject = new JSONObject(FileOperations.readFile(file));
            DATA.put(name, jsonObject);
            if (jsonObject != JSONObject.NULL) {
                return jsonObject;
            }
        } catch (Exception ex) {
            log.error("Could not parse data file: [{}].", fileName, ex);
            return null;
        }
        log.warn("Could not parse data file: [{}].", fileName);
        return null;
    }

    /**
     * Fetches data into a String class by key with on-demand.
     *
     * @param name The name of the file without the extension.
     * @param key  The key of the data.
     * @return The string.
     */
    public static String fetchString(String name, String key) {
        if (!exists(name)) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        JSONArray jsonArray = (JSONArray) fetch(name, true).get(key);
        if (jsonArray == null) {
            return null;
        }
        int len = jsonArray.length();
        for (int i = 0; i < len; i++) {
            stringBuilder.append(jsonArray.get(i).toString()).append(i != (len - 1) ? "<br>" : "");
        }
        return stringBuilder.toString();
    }

    /**
     * Fetches data into an int class by key with on-demand.
     *
     * @param name The name of the file without the extension.
     * @param key  The key of the data.
     * @return The int.
     */
    public static int fetchInt(String name, String key) {
        if (!exists(name)) {
            return -1;
        }
        JSONArray jsonArray = (JSONArray) fetch(name, true).get(key);
        if (jsonArray == null) {
            return -1;
        }
        return Integer.parseInt(jsonArray.get(0).toString());
    }

    /**
     * Fetches data into a String array.
     *
     * @param name     The name of the file without the extension.
     * @param ondemand Whether or not to use on-demand.
     * @return The string array.
     */
    public static String[] fetchStringArray(String name, boolean ondemand) {
        if (!exists(name)) {
            return null;
        }
        ArrayList<String> arrayList = new ArrayList<>();
        JSONArray jsonArray = (JSONArray) fetch(name, ondemand).get("data");
        if (jsonArray == null) {
            return null;
        }
        int len = jsonArray.length();
        for (int i = 0; i < len; i++) {
            arrayList.add(jsonArray.get(i).toString());
        }
        return arrayList.toArray(new String[arrayList.size()]);
    }

    /**
     * Fetches data into a String array with on-demand.
     *
     * @param name The name of the file without the extension.
     * @return The string array.
     */
    public static String[] fetchStringArray(String name) {
        return fetchStringArray(name, true);
    }

    /**
     * Fetches data into an int array.
     *
     * @param name     The name of the file without the extension.
     * @param ondemand Whether or not to use on-demand.
     * @return The string array.
     */
    public static int[] fetchIntArray(String name, boolean ondemand) {
        if (!exists(name)) {
            return null;
        }
        int[] returnInts = new int[0];
        JSONArray jsonArray = (JSONArray) fetch(name, ondemand).get("data");
        if (jsonArray == null) {
            return null;
        }
        int len = jsonArray.length();
        returnInts = new int[len];
        for (int i = 0; i < len; i++) {
            returnInts[i] = jsonArray.getInt(i);
        }
        return returnInts;
    }

    /**
     * Fetches data into an int array with on-demand.
     *
     * @param name The name of the file without the extension.
     * @return The string array.
     */
    public static int[] fetchIntArray(String name) {
        return fetchIntArray(name, true);
    }

    /**
     * Fetches data into a {@code Map} or {@code List}.
     *
     * @param name     The name of the file without the extension.
     * @param ondemand Whether or not to use on-demand.
     * @return The {@code Map} or {@code List}.
     */
    public static Object fetchListMap(String name, boolean ondemand) {
        if (!exists(name)) {
            return null;
        }
        Object object = fetch(name, ondemand).get("data");
        if (object instanceof JSONArray) {
            return toList((JSONArray) object);
        }
        return toMap((JSONObject) fetch(name, ondemand).get("data"));
    }

    /**
     * Fetches data into a {@code Map} or {@code List} with on-demand.
     *
     * @param name The name of the file without the extension.
     * @return The {@code Map} or {@code List}.
     */
    public static Object fetchListMap(String name) {
        return fetchListMap(name, true);
    }

    @Override
    public boolean call() {
        log.info("Parsing JSON on-demand files.");
        Path path = Paths.get(Constants.ONDEMAND_PATH);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, "*.{json}")) {
            for (Path entry : stream) {
                if (entry == null) {
                    continue;
                }
                try {
                    JSONObject jsonObject = new JSONObject(FileOperations.readFile(new File(Constants.ONDEMAND_PATH + "/" + entry.getFileName().toString())));
                    DATA.put(entry.getFileName().toString(), jsonObject);
                } catch (Exception ex) {
                    log.error("Could not parse data file: [{}].", entry.getFileName(), ex);
                }
            }
        } catch (IOException ex) {
            log.error("On-demand data path does not exist: [{}].", Constants.ONDEMAND_PATH, ex);
        }
        log.info("Parsed {} JSON on-demand files.", DATA.size());
        return true;
    }

    public static Map<String, Object> toMap(JSONObject object) {
        Map<String, Object> map = new HashMap<>();
        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

}
