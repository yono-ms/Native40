/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40.network.model

import kotlinx.serialization.Serializable

@Serializable
data class HttpBinGetModel(
    val args: Map<String, String>,
    val headers: Map<String, String>,
    val origin: String,
    // 任意項目には初期化を入れておくとパースエラーにならない.
    val option: String? = null,
    val url: String
)