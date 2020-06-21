/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

enum class RequestCode(val rawValue: Int) {
    ALERT(100),
    SINGLE_CHOICE(101)
}

enum class ExtraKey(val rawValue: String) {
    SINGLE_CHOICE("SINGLE_CHOICE")
}
