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

class StartViewModel : BaseViewModel() {

    val progressInt: MutableLiveData<Int> by lazy { MutableLiveData(0) }
    val progress: LiveData<String> = Transformations.map(progressInt) {
        "$it"
    }

    fun start() {
        logger.info("start.")
        viewModelScope.launch {
            progressInt.value = progressInt.value?.plus(1)
            delay(200)
            progressInt.value = progressInt.value?.plus(1)
            delay(200)
            progressInt.value = progressInt.value?.plus(1)
            dialogMessage.value =
                DialogMessage(RequestCode.ALERT, R.string.app_name, R.string.app_name)
        }
    }
}