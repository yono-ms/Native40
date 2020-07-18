/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import com.example.native40.network.model.HttpBinGetModel
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.HttpException
import com.github.kittinunf.fuel.coroutines.awaitStringResponseResult
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.JsonException
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.MalformedURLException
import java.net.UnknownHostException

class HttpBinAPIUnitTest {
    companion object {
        const val basePath = "https://httpbin.org"
        const val apiGet = "/get"
        val json = Json(
            JsonConfiguration.Stable.copy(
                ignoreUnknownKeys = true
            )
        )
    }

    @Before
    fun before() {
        FuelManager.instance.basePath = basePath
    }

    @Test
    fun get() = runBlocking {
        kotlin.runCatching {
            val args = listOf<Pair<String, Any?>>("arg1" to "value1", "arg2" to "value2")
            val (request, response, result) = Fuel.get(apiGet, args).awaitStringResponseResult()
            println(request)
            println(response)
            val text = result.get()
            json.parse(HttpBinGetModel.serializer(), text)
        }.onSuccess {
            assertEquals(it.url, "https://httpbin.org/get?arg1=value1&arg2=value2")
            assertEquals(it.args.size, 2)
            assertEquals(it.args["arg1"], "value1")
            assertEquals(it.args["arg2"], "value2")
        }.onFailure {
            printErrorMessage(it)
            assert(false)
        }
        Unit
    }

    private fun printErrorMessage(throwable: Throwable) {
        println(throwable.javaClass.simpleName)
        // awaitObjectを使用した場合パースエラーはFuelErrorになる
        when (throwable) {
            is FuelError -> printDetailErrorMessage(throwable.cause?.cause ?: throwable)
            else -> printDetailErrorMessage(throwable)
        }
    }

    private fun printDetailErrorMessage(throwable: Throwable) {
        val message = formatErrorMessage(throwable)
        when (throwable) {
            is FuelError -> println("COMM ERROR : $message")
            is HttpException -> println("HTTP ERROR : $message")
            is MalformedURLException -> println("URL ERROR : $message")
            is UnknownHostException -> println("DNS ERROR : $message")
            is JsonException -> println("FORMAT ERROR : $message")
            is SerializationException -> println("PARSE ERROR : $message")
            else -> println("SYSTEM ERROR : $message")
        }
    }

    private fun formatErrorMessage(throwable: Throwable): String {
        return "${throwable::class.java.simpleName} : ${throwable.localizedMessage}"
    }
}