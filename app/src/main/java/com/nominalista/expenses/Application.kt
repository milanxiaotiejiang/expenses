package com.nominalista.expenses

import androidx.appcompat.app.AppCompatDelegate
import com.jakewharton.threetenabp.AndroidThreeTen
import com.nominalista.expenses.data.preference.PreferenceDataSource
import com.nominalista.expenses.data.room.ApplicationDatabase
import com.nominalista.expenses.data.room.RoomDataStore
import com.nominalista.expenses.data.store.DataStore

class Application : android.app.Application() {

    val defaultDataStore: DataStore
        get() {
            return localDataStore
        }

    private val localDataStore: DataStore by lazy {
        val database = ApplicationDatabase.build(this)

        RoomDataStore(
                database.expenseDao(),
                database.tagDao(),
                database.expenseTagJoinDao()
        )
    }

    val preferenceDataSource: PreferenceDataSource by lazy {
        PreferenceDataSource()
    }

    override fun onCreate() {
        super.onCreate()
        initializeThreeTeen()
        applyTheme()
    }

    private fun initializeThreeTeen() {
        AndroidThreeTen.init(this)
    }

    private fun applyTheme() {
        val theme = preferenceDataSource.getTheme(applicationContext)
        AppCompatDelegate.setDefaultNightMode(theme.toNightMode())
    }
}
