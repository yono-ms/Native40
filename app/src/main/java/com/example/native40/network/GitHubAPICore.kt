/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40.network

import com.example.native40.network.model.GitHubUserModel
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitObjectResponseResult
import com.github.kittinunf.fuel.serialization.kotlinxDeserializerOf
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonArraySerializer
import kotlinx.serialization.json.JsonConfiguration

abstract class GitHubAPICore {
    companion object {
        private const val url_users = "https://api.github.com/users/"
        val json = Json(
            JsonConfiguration.Stable.copy(
                ignoreUnknownKeys = true
            )
        )
    }

    abstract fun log(text: String)

    suspend fun getUsers(login: String): GitHubUserModel {
        // Fuel default scope is IO
        val (request, response, result) = Fuel.get("$url_users$login")
            .awaitObjectResponseResult(
                kotlinxDeserializerOf(GitHubUserModel.serializer(), json)
            )
        log("$request")
        log("$response")
        return result.get()
    }

    suspend fun getRepos(login: String): JsonArray {
        val (request, response, result) = Fuel.get("$url_users$login/repos")
            .awaitObjectResponseResult(
                kotlinxDeserializerOf(JsonArraySerializer, json)
            )
        log("$request")
        log("$response")
        return result.get()
    }
}