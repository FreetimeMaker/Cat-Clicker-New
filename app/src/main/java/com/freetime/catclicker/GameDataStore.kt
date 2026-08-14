package com.freetime.catclicker

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "game_stats")

class GameDataStore(private val context: Context) {
    private val CATS = intPreferencesKey("cats")
    private val CLICK_POWER = intPreferencesKey("click_power")
    private val AUTO_CLICK_RATE = intPreferencesKey("auto_click_rate")

    val catsFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[CATS] ?: 0
    }

    val clickPowerFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[CLICK_POWER] ?: 1
    }

    val autoClickRateFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[AUTO_CLICK_RATE] ?: 0
    }

    suspend fun saveStats(cats: Int, clickPower: Int, autoClickRate: Int) {
        context.dataStore.edit { preferences ->
            preferences[CATS] = cats
            preferences[CLICK_POWER] = clickPower
            preferences[AUTO_CLICK_RATE] = autoClickRate
        }
    }
}
