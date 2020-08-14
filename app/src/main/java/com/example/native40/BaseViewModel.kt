/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.native40.db.AppDatabase
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class BaseViewModel : ViewModel() {
    val logger: Logger by lazy {
        LoggerFactory.getLogger(javaClass.simpleName)
    }

    val db: AppDatabase = Native40App.db
    val prefs: PreferenceTool = Native40App.prefs

    val dialogMessage: MutableLiveData<DialogMessage> by lazy { MutableLiveData<DialogMessage>() }
    val destination: MutableLiveData<Destination> by lazy { MutableLiveData<Destination>() }

    val connectionErrorDialogMessage: DialogMessage
        get() = DialogMessage(
            RequestCode.ALERT,
            R.string.dialog_title_connection_error,
            R.string.dialog_message_connection_error
        )
}