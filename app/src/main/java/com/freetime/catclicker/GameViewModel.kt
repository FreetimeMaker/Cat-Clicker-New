package com.freetime.catclicker

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore = GameDataStore(application)

    var cats by mutableIntStateOf(0)
        private set

    var clickPower by mutableIntStateOf(1)
        private set

    var autoClickRate by mutableIntStateOf(0)
        private set

    init {
        viewModelScope.launch {
            cats = dataStore.catsFlow.first()
            clickPower = dataStore.clickPowerFlow.first()
            autoClickRate = dataStore.autoClickRateFlow.first()
        }
    }

    private fun save() {
        viewModelScope.launch {
            dataStore.saveStats(cats, clickPower, autoClickRate)
        }
    }

    fun addClick() {
        cats += clickPower
        save()
    }

    fun buyClickPower() {
        val cost = 10 * clickPower
        if (cats >= cost) {
            cats -= cost
            clickPower += 1
            save()
        }
    }

    fun buyAutoClicker() {
        val cost = 50 * (autoClickRate + 1)
        if (cats >= cost) {
            cats -= cost
            autoClickRate += 1
            save()
        }
    }

    fun addAutoMoney() {
        if (autoClickRate > 0) {
            cats += autoClickRate
            save()
        }
    }
}
