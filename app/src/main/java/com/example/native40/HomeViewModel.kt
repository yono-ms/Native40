/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.native40.db.User
import com.example.native40.extension.toDisplayDateFromISO
import com.example.native40.network.GitHubAPI
import kotlinx.coroutines.launch
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.util.*

class HomeViewModel(private val state: SavedStateHandle) : BaseViewModel() {

    companion object {
        enum class StateKey(val rawValue: String) {
            Login("Login")
        }
    }

    override fun onSaveInstanceState() {
        logger.info("onSaveInstanceState login=${login.value}")
        state.set(StateKey.Login.rawValue, login.value)
    }

    val login: MutableLiveData<String> by lazy { MutableLiveData(state.get(StateKey.Login.rawValue)) }

    val items: MutableLiveData<List<Repository>> by lazy {
        MutableLiveData<List<Repository>>(
            listOf()
        )
    }

    data class Repository(
        val name: String,
        val updatedAt: String,
        val htmlUrl: String
    )

    fun onHistory() {
        viewModelScope.launch {
            val list = db.userDao().getAllHistory()
            if (list.isEmpty()) {
                return@launch
            }
            val histories = list.map { x -> x.login }
            dialogMessage.value = DialogMessage(
                RequestKey.SINGLE_CHOICE,
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
                items.value = listOf()
                GitHubAPI().getRepos(login.value.toString()).map { a ->
                    val name = a.jsonObject["name"]?.jsonPrimitive?.content.toString()
                    val updatedAt =
                        a.jsonObject["updated_at"]?.jsonPrimitive?.content.toString()
                            .toDisplayDateFromISO()
                    val htmlUrl = a.jsonObject["html_url"]?.jsonPrimitive?.content.toString()
                    Repository(name, updatedAt, htmlUrl)
                }
            }.onSuccess {
                items.value = it
                db.userDao().insertAll(User(0, login.value.toString(), Date()))
                logger.info("Success.")
            }.onFailure {
                dialogMessage.value = DialogMessage(RequestKey.ALERT, throwable = it)
            }.also {
                mainViewModel.busy.value = false
            }
        }
    }
}