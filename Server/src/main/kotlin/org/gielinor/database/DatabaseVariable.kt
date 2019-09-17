package org.gielinor.database

import org.gielinor.rs2.config.DatabaseDetails.DatabaseType

class DatabaseVariable<T> @JvmOverloads constructor(
    private val databaseType: DatabaseType = DatabaseType.GAME,
    value: T? = null,
    private val table: String,
    private val column: String,
    private val secondaryColumn: String? = null,
    private val secondaryColumnValue: Any? = null) {
    var value: T? = value
        set(value) {
            field = value
            save()
        }

    fun save() {
        DataSource.getConnection(databaseType)?.performDatabaseFunction("UPDATE $table SET $column=$value ${if (secondaryColumn != null) "WHERE $secondaryColumn=$secondaryColumnValue" else ""}")
    }

    @Suppress("UNCHECKED_CAST")
    fun load() {
        val databaseValue = DataSource.getConnection(databaseType)?.loadVariableFromDatabase("SELECT $column FROM $table ${if (secondaryColumn != null) "WHERE $secondaryColumn=$secondaryColumnValue" else ""}")
        if (databaseValue != null) value = databaseValue as T?
    }

    protected fun finalize() {
        DataSource.getDatabaseVariables().remove(this)
    }

    init {
        load()
        DataSource.getDatabaseVariables().add(this)
    }
}
