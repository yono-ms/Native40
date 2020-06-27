/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val headerText = MutableLiveData("loading...")
    val busy = MutableLiveData(false)
}