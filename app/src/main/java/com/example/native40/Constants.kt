/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

enum class RequestCode(val rawValue: Int) {
    ALERT(100),
    OK_CANCEL(101),
    SINGLE_CHOICE(102)
}

enum class ExtraKey(val rawValue: String) {
    SINGLE_CHOICE("SINGLE_CHOICE")
}

enum class Destination {
    REPLACE_HOME,
    PUSH_HISTORY
}
