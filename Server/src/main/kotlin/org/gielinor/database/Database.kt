package org.gielinor.database

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * Loads a singular variables with smart casting too
 */
inline fun <Data> Connection.loadVariableFromDatabase(query: String, thingsToLoad: ResultSet.() -> Data): Data? {
    this.use {
        prepareStatement(query).use {
            val resultSet = it.executeQuery()
            resultSet.use {
                it.apply {
                    while (it.next()) {
                        return it.thingsToLoad()
                    }
                }
            }
        }
    }
    return null
}

/**
 * Loads a singular variables with smart casting too
 */
fun Connection.loadVariableFromDatabase(query: String): Any? {
    this.use {
        prepareStatement(query).use {
            val resultSet = it.executeQuery()
            resultSet.use {
                it.apply {
                    while (it.next()) {
                        return it.getObject(1)
                    }
                }
            }
        }
    }
    return null
}

/**
 * Loads data to assign external variables
 */
inline fun Connection.loadDataFromDatabase(query: String, thingsToLoad: ResultSet.() -> Unit) {
    this.use {
        prepareStatement(query).use {
            val resultSet = it.executeQuery()
            resultSet.use {
                it.apply {
                    while (it.next()) {
                        it.thingsToLoad()
                    }
                }
            }
        }
    }
}

inline fun Connection.saveDataToDatabase(query: String, thingsToSave: PreparedStatement.() -> Unit) {
    this.use {
        prepareStatement(query).use {
            it.thingsToSave()
            it.executeUpdate()
        }
    }
}

fun Connection.performDatabaseFunction(query: String) {
    this.use {
        prepareStatement(query).use {
            it.executeUpdate()
        }
    }
}