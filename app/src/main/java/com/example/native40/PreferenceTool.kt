/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class PreferenceTool(var context: Context) {

    private val prefs: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(
            context
        )
    }

    val startPrompt: Boolean
        get() {
            return prefs.getBoolean(context.getString(R.string.preference_start_key), false)
        }
}