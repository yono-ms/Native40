/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40.extension

import com.example.native40.R
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.HttpException
import kotlinx.serialization.SerializationException
//import kotlinx.serialization.json.JsonException
import java.net.MalformedURLException
import java.net.UnknownHostException

/**
 * Use cause^2 on FuelError.
 */
val Throwable.dialogCause: Throwable
    get() = if (this is FuelError) this.cause?.cause ?: this else this

/**
 * Dialog Title Resource ID.
 */
val Throwable.dialogTitle: Int
    get() = when (dialogCause) {
        is FuelError -> R.string.dialog_title_fuel_error
        is HttpException -> R.string.dialog_title_http_exception
        is MalformedURLException -> R.string.dialog_title_malformed_url_exception
        is UnknownHostException -> R.string.dialog_title_unknown_host_exception
//        is JsonException -> R.string.dialog_title_json_exception
        is SerializationException -> R.string.dialog_title_serialization_exception
        else -> R.string.dialog_title_other_exception
    }

/**
 * Dialog Message.
 */
val Throwable.dialogMessage: String
    get() = "${dialogCause::class.java.simpleName} : ${dialogCause.localizedMessage}"
