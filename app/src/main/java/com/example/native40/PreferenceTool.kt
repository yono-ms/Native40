/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import android.content.SharedPreferences
import java.util.*

class PreferenceTool(private var prefs: SharedPreferences, val getKey: ((resId: Int) -> String)) {

    val startPrompt: Boolean
        get() {
            return prefs.getBoolean(getKey(R.string.preference_start_key), false)
        }

    var timestamp: Date
        get() {
            val time = prefs.getLong(getKey(R.string.preference_timestamp_key), Date().time)
            return Date(time)
        }
        set(value) {
            prefs.edit().apply {
                putLong(getKey(R.string.preference_timestamp_key), value.time)
                apply()
            }
        }
}