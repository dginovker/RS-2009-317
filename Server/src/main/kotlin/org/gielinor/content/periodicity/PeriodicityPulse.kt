package org.gielinor.content.periodicity

import org.gielinor.database.DataSource

import org.gielinor.database.loadVariableFromDatabase
import org.gielinor.database.saveDataToDatabase

object PeriodicityTable {
    const val TABLE_NAME = "periodicity_pulses"
}

/**
 * The pulse itself.
 * @author Arham 4
 */
abstract class PeriodicityPulse {

    /**
     * NOTE: This is run at server startup REGARDLESS of what check() says.
     */
    abstract fun pulse()

    /**
     * NOTE: This is called EVERYDAY
     */
    open fun check() {}

    /**
     * strictly loads data
     */
    open fun init() {}

    open fun save() {}

    fun loadData(column: String, table: String): Any? {
        return DataSource.getGameConnection().loadVariableFromDatabase("SELECT $column FROM $table") { getObject(1) }
    }

    fun saveData(column: String, table: String, data: Any?) {
        if (data != null) {
            DataSource.getGameConnection().saveDataToDatabase("UPDATE $table SET $column = ?") {
                setObject(1, data)
            }
        }
    }
}