package org.gielinor.rs2.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * A configuration parser will loadPlayer simple configuration files.
 * <p>
 * An example of one of the files it is capable of parsing is shown below:
 * <p>
 * <code>
 * # this is a comment
 * key1: value1
 * key2: value2
 * key3[subkey1]: value3
 * key3[subkey2]: value4
 * </code>
 *
 * @author Graham Edgecombe
 *
 */
public class ConfigurationParser {

    /**
     * The buffered READER.
     */
    private final BufferedReader READER;

    /**
     * The simple SIMPLE_MAPPINGS i.e. <code>key:value</code>.
     */
    private final Map<String, String> SIMPLE_MAPPINGS = new HashMap<>();

    /**
     * The complex SIMPLE_MAPPINGS i.e. <code>key[index]:value</code>.
     */
    private final Table<String, String, String> COMPLEX_MAPPINGS = HashBasedTable.create();

    /**
     * Creates the configuration parser and parses configuration.
     *
     * @param is
     *            The input stream.
     * @throws IOException
     *             if an I/O error occurs.
     */
    public ConfigurationParser(InputStream is) throws IOException {
        READER = new BufferedReader(new InputStreamReader(is));
        parse();
    }

    /**
     * Gets the complex SIMPLE_MAPPINGS.
     *
     * @return The complex SIMPLE_MAPPINGS i.e. <code>key[index]:value</code>.
     */
    public Table<String, String, String> getComplexMappings() {
        return COMPLEX_MAPPINGS;
    }

    /**
     * Gets the SIMPLE_MAPPINGS.
     *
     * @return The simple SIMPLE_MAPPINGS i.e. <code>key:value</code>.
     */
    public Map<String, String> getMappings() {
        return SIMPLE_MAPPINGS;
    }

    /**
     * Parses the configuration.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    public void parse() throws IOException {
        String line;

        while ((line = READER.readLine()) != null) {
            line = line.trim();

            if (line.length() > 0 && line.charAt(0) == '#' || line.length() == 0) {
                continue;
            }

            String[] parts = line.split(":");

            if (parts.length != 2) {
                continue;
            }

            String key = parts[0].trim();
            String value = parts[1].trim();

            if (key.length() > 0 && key.charAt(key.length() - 1) == ']') {
                int idx = key.indexOf('[');

                if (idx > -1) {
                    String index = key.substring(idx + 1, key.length() - 1);

                    key = key.substring(0, idx);

                    COMPLEX_MAPPINGS.put(key, index, value);
                }
            } else {
                SIMPLE_MAPPINGS.put(key, value);
            }
        }

        READER.close();
    }

}
