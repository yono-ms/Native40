/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {

    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.headerText.value = getString(R.string.fragment_title_settings)
    }
}