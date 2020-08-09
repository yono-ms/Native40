/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import com.example.native40.network.model.HttpBinGetModel
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.HttpException
import com.github.kittinunf.fuel.coroutines.awaitObjectResponseResult
import com.github.kittinunf.fuel.coroutines.awaitStringResponseResult
import com.github.kittinunf.fuel.serialization.kotlinxDeserializerOf
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.JsonObjectSerializer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

class FuelUnitTest {
    @Before
    fun before() {
        //FuelManager.instance.basePath = "https://httpbin.org"
    }

    /**
     * bad url
     */
    @Test
    fun badProtocol() {
        runBlocking {
            kotlin.runCatching {
                val (request, response, result) = Fuel.get("protocol://hostNameIsBad/apiIsBad")
                    .awaitStringResponseResult()
                println(request)
                println(response)
                result.get()
            }.onSuccess {
                assert(false)
            }.onFailure {
                // GitHub Actionsではなぜか動かない
                //assert(it is MalformedURLException)
                println("COMM ERROR : ${it.message}")
            }
        }
    }

    /**
     * unknown host
     */
    @Test
    fun badHost() = runBlocking {
        kotlin.runCatching {
            val (request, response, result) = Fuel.get("https://hostNameIsBad/apiIsBad")
                .awaitStringResponseResult()
            println(request)
            println(response)
            result.get()
        }.onSuccess {
            assert(false)
        }.onFailure {
            assert(it is FuelError)
            assert(it.cause is FuelError)
            assert(it.cause?.cause is UnknownHostException)
            println("COMM ERROR : ${it.message}")
        }
        Unit
    }

    /**
     * 404 Not found.
     */
    @Test
    fun badApi() = runBlocking {
        kotlin.runCatching {
            val (request, response, result) = Fuel.get("https://httpbin.org/apiIsBad")
                .awaitStringResponseResult()
            println(request)
            println(response)
            result.get()
        }.onSuccess {
            assert(false)
        }.onFailure {
            assert(it is FuelError)
            assert(it.cause is FuelError)
            assert(it.cause?.cause is HttpException)
            println("COMM ERROR : ${it.message}")
        }
        Unit
    }

    /**
     * 文字列
     */
    @Test
    fun goodGet() = runBlocking {
        kotlin.runCatching {
            val (request, response, result) = Fuel.get("https://httpbin.org/get")
                .awaitStringResponseResult()
            println(request)
            println(response)
            result.get()
        }.onSuccess {
            println(it)
            assertNotNull(it)
        }.onFailure {
            println("COMM ERROR : ${it.message}")
            assert(false)
        }
        Unit
    }

    /**
     * JSONオブジェクト.
     * 配列から始まる場合はJsonArraySerializerを使用する.
     * kotlinx-serialization-runtime
     * fuel-kotlinx-serialization
     */
    @Test
    fun goodGetObject() = runBlocking {
        kotlin.runCatching {
            val (request, response, result) = Fuel.get("https://httpbin.org/get")
                .awaitObjectResponseResult(
                    kotlinxDeserializerOf(
                        JsonObjectSerializer,
                        Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true))
                    )
                )
            println(request)
            println(response)
            result.get()
        }.onSuccess {
            println(it)
            assertEquals("https://httpbin.org/get", it.getPrimitive("url").content)
        }.onFailure {
            println("COMM ERROR : ${it.message}")
            assert(false)
        }
        Unit
    }

    /**
     * data class のシリアライズ.
     * kotlinx-serialization-runtime
     * fuel-kotlinx-serialization
     */
    @Test
    fun goodGetParse() = runBlocking {
        kotlin.runCatching {
            val (request, response, result) = Fuel.get("https://httpbin.org/get")
                .awaitObjectResponseResult(
                    kotlinxDeserializerOf(
                        HttpBinGetModel.serializer(),
                        Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true))
                    )
                )
            println(request)
            println(response)
            result.get()
        }.onSuccess {
            println(it)
            assertEquals("https://httpbin.org/get", it.url)
        }.onFailure {
            println("COMM ERROR : ${it.message}")
            assert(false)
        }
        Unit
    }
}