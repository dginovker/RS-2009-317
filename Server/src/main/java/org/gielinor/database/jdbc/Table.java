package org.gielinor.database.jdbc;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Represents an SQL table to be adjusted.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public abstract class Table<E> {

    /**
     * Loads data from an SQL table.
     *
     * @param e The class to load.
     * @throws SQLException
     * @throws IOException
     */
    public abstract void load(E e) throws SQLException, IOException;

    /**
     * Saves data into an SQL table.
     *
     * @param e The class to save.
     * @throws SQLException
     * @throws IOException
     */
    public abstract void save(E e) throws SQLException, IOException;

}