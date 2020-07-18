/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.viewModelScope
import com.example.native40.network.GitHubAPI
import kotlinx.coroutines.launch
import kotlinx.serialization.json.content

class HomeViewModel : BaseViewModel() {

    val items = ObservableArrayList<Pair<String, String>>()

    fun onClick(mainViewModel: MainViewModel) {
        if (mainViewModel.connected.value != true) {
            dialogMessage.value = connectionErrorDialogMessage
            return
        }
        viewModelScope.launch {
            kotlin.runCatching {
                mainViewModel.busy.value = true
                items.clear()
                GitHubAPI().getRepos("kittinunf").map { a ->
                    val name = a.jsonObject["name"]?.content ?: "no_name"
                    val updatedAt = a.jsonObject["updated_at"]?.content.toString()
                    Pair(name, updatedAt)
                }
            }.onSuccess {
                items.addAll(it)
                logger.info("Success.")
            }.onFailure {
                dialogMessage.value = DialogMessage(RequestCode.ALERT, throwable = it)
            }.also {
                mainViewModel.busy.value = false
            }
        }
    }
}