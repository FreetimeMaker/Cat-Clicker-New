package com.freetime.catclicker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    var cats by mutableIntStateOf(0)
        private set

    var clickPower by mutableIntStateOf(1)
        private set

    var autoClickRate by mutableIntStateOf(0)
        private set

    fun addClick() {
        cats += clickPower
    }

    fun buyClickPower() {
        val cost = 10 * clickPower
        if (cats >= cost) {
            cats -= cost
            clickPower += 1
        }
    }

    fun buyAutoClicker() {
        val cost = 50 * (autoClickRate + 1)
        if (cats >= cost) {
            cats -= cost
            autoClickRate += 1
        }
    }

    fun addAutoMoney() {
        cats += autoClickRate
    }
}
