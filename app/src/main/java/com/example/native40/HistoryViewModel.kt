/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.native40.db.User
import kotlinx.coroutines.launch

class HistoryViewModel : BaseViewModel() {
    val items: LiveData<List<User>> by lazy {
        db.userDao().getHistoryLiveDataLogin()
    }

    fun sort(resId: Int) {
        when (resId) {
            R.id.radioButtonName -> logger.info("sort name.")
            else -> logger.info("sort date.")
        }
    }

    fun remove(login: String) {
        logger.info("remove $login")
        viewModelScope.launch {
            items.value?.firstOrNull { a -> a.login == login }?.let {
                db.userDao().delete(it)
            }
        }
    }
}