/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import android.os.Bundle
import android.text.InputType
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SettingsFragment : PreferenceFragmentCompat() {

    companion object {
        private const val PATTERN_USER_NAME = "^[ -~]+$"
    }

    val logger: Logger by lazy {
        LoggerFactory.getLogger(javaClass.simpleName)
    }

    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val userName: EditTextPreference? = findPreference(getString(R.string.preference_name_key))
        userName?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_NORMAL
            editText.addTextChangedListener { editable ->
                logger.info("addTextChangedListener $editable")
                editable?.let {
                    val pattern = PATTERN_USER_NAME
                    val matched = Regex(pattern).matches(it.toString())
                    logger.info("matched=$matched")
                    if (it.isNotEmpty() && !matched) {
                        it.delete(it.length - 1, it.length)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.headerText.value = getString(R.string.fragment_title_settings)
    }
}