/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40.network

import com.example.native40.network.model.GitHubUserModel
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitStringResponseResult
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray

abstract class GitHubAPICore {
    companion object {
        private const val url_users = "https://api.github.com/users/"
        val json = Json {
            ignoreUnknownKeys = true
        }
    }

    abstract fun log(text: String)

    suspend fun getUsers(login: String): GitHubUserModel {
        // Fuel default scope is IO
        val (request, response, result) = Fuel.get("$url_users$login").awaitStringResponseResult()
        log("$request")
        log("$response")
        val text = result.get()
        return json.decodeFromString(GitHubUserModel.serializer(), text)
    }

    suspend fun getRepos(login: String): JsonArray {
        val (request, response, result) = Fuel.get("$url_users$login/repos")
            .awaitStringResponseResult()
        log("$request")
        log("$response")
        val text = result.get()
        return json.decodeFromString(JsonArray.serializer(), text)
    }
}