/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40.extension

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.HttpException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonException
import java.net.MalformedURLException
import java.net.UnknownHostException

val Throwable.dialogCause: Throwable
    get() = this.cause?.cause ?: this

val Throwable.dialogTitle: String
    get() = when (dialogCause) {
        is FuelError -> "COMM ERROR"
        is HttpException -> "HTTP ERROR"
        is MalformedURLException -> "URL ERROR"
        is UnknownHostException -> "DNS ERROR"
        is JsonException -> "FORMAT ERROR"
        is SerializationException -> "PARSE ERROR"
        else -> "SYSTEM ERROR"
    }

val Throwable.dialogMessage: String
    get() = "${dialogCause::class.java.simpleName} : ${dialogCause.localizedMessage}"
