/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel() {
    fun onClick(mainViewModel: MainViewModel) {
        viewModelScope.launch {
            mainViewModel.busy.value = true
            delay(2000)
            mainViewModel.busy.value = false
        }
    }
}