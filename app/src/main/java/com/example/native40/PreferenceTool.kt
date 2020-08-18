/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import java.util.*

class PreferenceTool(var context: Context) {

    private val prefs: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(
            context
        )
    }

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

    private fun getKey(resId: Int): String {
        return context.getString(resId)
    }
}