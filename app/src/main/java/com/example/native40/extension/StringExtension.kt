/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40.extension

import java.text.SimpleDateFormat
import java.util.*

fun String.toDisplayDateFromISO(): String {
    val isoFormat = "yyyy-MM-dd'T'HH:mm:ssX"
    val displayFormat = "yyyy/MM/dd HH:mm"
    val date = SimpleDateFormat(isoFormat, Locale.US).parse(this)
    return if (date != null) {
        SimpleDateFormat(displayFormat, Locale.getDefault()).format(date)
    } else {
        "----/--/-- --:--"
    }
}