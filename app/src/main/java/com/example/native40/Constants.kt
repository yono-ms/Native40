/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

enum class RequestKey(val rawValue: String) {
    ALERT("ALERT"),
    OK_CANCEL("OK_CANCEL"),
    SINGLE_CHOICE("SINGLE_CHOICE")
}

enum class BundleKey(val rawValue: String) {
    RESULT("RESULT"),
    SELECTED("SELECTED")
}

enum class Destination {
    REPLACE_HOME,
    PUSH_HISTORY,
    PUSH_SETTINGS
}
