/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.native40.db.User
import kotlinx.coroutines.launch

class HistoryViewModel : BaseViewModel() {
    private val dbItems: LiveData<List<User>> by lazy {
        logger.info("userDao getHistoryLiveDataLogin START")
        db.userDao().getHistoryLiveDataLogin()
    }

    val sortByLogin = MutableLiveData(true)

    val items: MediatorLiveData<List<User>> by lazy {
        MediatorLiveData<List<User>>().apply {
            fun mediate(list: List<User>?, isSortByLogin: Boolean?) {
                logger.info("mediate ${list?.size} $isSortByLogin")
                items.value =
                    if (isSortByLogin == true) list?.sortedBy { a -> a.login } else list?.sortedByDescending { a -> a.timeStamp }
            }
            addSource(dbItems) {
                mediate(it, sortByLogin.value)
            }
            addSource(sortByLogin) {
                mediate(dbItems.value, it)
            }
        }
    }

    fun remove(login: String) {
        logger.info("remove $login")
        viewModelScope.launch {
            dbItems.value?.firstOrNull { a -> a.login == login }?.let {
                db.userDao().delete(it)
            }
        }
    }
}