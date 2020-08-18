/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class StartViewModel : BaseViewModel() {

    private val progressInt: MutableLiveData<Int> by lazy { MutableLiveData(0) }
    val progress: LiveData<String> = Transformations.map(progressInt) {
        "$it"
    }

    fun start() {
        logger.info("start.")
        viewModelScope.launch {
            prefs.timestamp = Date()
            for (count in 1..3) {
                progressInt.value = count
                delay(500)
            }
            if (prefs.startPrompt) {
                dialogMessage.value =
                    DialogMessage(RequestCode.ALERT, R.string.app_name, R.string.app_name)
            } else {
                destination.value = Destination.REPLACE_HOME
            }
        }
    }
}