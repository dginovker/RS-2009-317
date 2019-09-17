package org.gielinor.rs2.config;

import java.util.List;

import org.gielinor.game.system.data.DataShelf;

/**
 * Represents a changing server variable.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class ServerVar {


    /**
     * Fetches an integer server variable.
     *
     * @param constantName The name of the server variable.
     * @return The server variable.
     */
    public static int fetchInt(String constantName) {
        return fetch(constantName, 0);
    }

    /**
     * Fetches a string server variable.
     *
     * @param constantName The name of the server variable.
     * @return The server variable.
     */
    public static String fetch(String constantName) {
        return fetch(constantName, "");
    }

    /**
     * Fetches a server variable with a fail value.
     *
     * @param constantName The name of the server variable.
     * @param fail         The fail value.
     * @return The server variable.
     */
    public static String fetch(String constantName, String fail) {
        List constantsMap = (List) DataShelf.fetchListMap("server_var");
        if (constantsMap == null) {
            return fail;
        }
        for (Object constant : constantsMap) {
            List constantList = (List) constant;
            if (constantList.get(0) == null || constantList.get(1) == null) {
                continue;
            }
            if (((String) constantList.get(0)).equalsIgnoreCase(constantName)) {
                return (String) constantList.get(1);
            }
        }
        return fail;
    }

    /**
     * Fetches a server variable with a fail value.
     *
     * @param constantName The name of the server variable.
     * @param fail         The fail value.
     * @return The server variable.
     */
    public static int fetch(String constantName, int fail) {
        List constantsMap = (List) DataShelf.fetchListMap("server_var");
        if (constantsMap == null) {
            return fail;
        }
        for (Object constant : constantsMap) {
            List constantList = (List) constant;
            if (constantList.get(0) == null || constantList.get(1) == null) {
                continue;
            }
            if (((String) constantList.get(0)).equalsIgnoreCase(constantName)) {
                return (int) constantList.get(1);
            }
        }
        return fail;
    }

}
