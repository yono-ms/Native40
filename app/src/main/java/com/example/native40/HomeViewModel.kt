/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.native40.db.User
import com.example.native40.extension.toDisplayDateFromISO
import com.example.native40.network.GitHubAPI
import kotlinx.coroutines.launch
import kotlinx.serialization.json.content
import java.util.*

class HomeViewModel : BaseViewModel() {

    val login: MutableLiveData<String> by lazy { MutableLiveData("") }

    val items = ObservableArrayList<Pair<String, String>>()

    fun onHistory() {
        viewModelScope.launch {
            val list = db.userDao().getAllHistory()
            if (list.isEmpty()) {
                return@launch
            }
            val histories = list.map { x -> x.login }
            dialogMessage.value = DialogMessage(
                RequestCode.SINGLE_CHOICE,
                R.string.dialog_title_history_choice,
                items = histories
            )
        }
    }

    fun onSelect(result: String) {
        login.value = result
    }

    fun onClick(mainViewModel: MainViewModel) {
        if (mainViewModel.connected.value != true) {
            dialogMessage.value = connectionErrorDialogMessage
            return
        }
        viewModelScope.launch {
            kotlin.runCatching {
                mainViewModel.busy.value = true
                items.clear()
                GitHubAPI().getRepos(login.value.toString()).map { a ->
                    val name = a.jsonObject["name"]?.content ?: "no_name"
                    val updatedAt =
                        a.jsonObject["updated_at"]?.content.toString().toDisplayDateFromISO()
                    Pair(name, updatedAt)
                }
            }.onSuccess {
                items.addAll(it)
                db.userDao().insertAll(User(0, login.value.toString(), Date()))
                logger.info("Success.")
            }.onFailure {
                dialogMessage.value = DialogMessage(RequestCode.ALERT, throwable = it)
            }.also {
                mainViewModel.busy.value = false
            }
        }
    }
}