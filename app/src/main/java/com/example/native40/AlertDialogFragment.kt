/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.slf4j.LoggerFactory

class AlertDialogFragment : DialogFragment() {
    companion object {
        private const val KEY_TITLE = "TITLE"
        private const val KEY_MESSAGE = "MESSAGE"
        private const val KEY_ITEMS = "ITEMS"

        private val logger =
            LoggerFactory.getLogger(AlertDialogFragment::class.java.simpleName)

        fun newInstance(
            dialogMessage: DialogMessage,
            target: Fragment? = null
        ): AlertDialogFragment {
            return AlertDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_TITLE, dialogMessage.title)
                    dialogMessage.message?.let { putInt(KEY_MESSAGE, it) }
                    dialogMessage.items?.let { putStringArray(KEY_ITEMS, it.toTypedArray()) }
                }
                target?.let { setTargetFragment(target, dialogMessage.requestCode.rawValue) }
            }
        }
    }

    val mainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { activity ->
            val builder = AlertDialog.Builder(activity)
            arguments?.getInt(KEY_TITLE)?.let { title -> builder.setTitle(title) }
            when (targetRequestCode) {
                RequestCode.ALERT.rawValue -> {
                    arguments?.getInt(KEY_MESSAGE)?.let { message ->
                        builder.setMessage(message)
                        builder.setPositiveButton(R.string.button_ok) { dialog, which ->
                            logger.info("dialog=$dialog which=$which")
                            targetFragment?.onActivityResult(targetRequestCode, which, null)
                            dialog.dismiss()
                        }
                    }
                }
                RequestCode.SINGLE_CHOICE.rawValue -> {
                    arguments?.getStringArray(KEY_ITEMS)?.let { items ->
                        builder.setItems(items) { dialog, which ->
                            logger.info("dialog=$dialog which=$which")
                            targetFragment?.onActivityResult(
                                targetRequestCode,
                                which,
                                Intent().apply {
                                    putExtra(ExtraKey.SINGLE_CHOICE.rawValue, items[which])
                                })
                            dialog.dismiss()
                        }
                    }
                }
            }
            builder.create().also { isCancelable = false }
        } ?: throw IllegalStateException("activity is null.")
    }
}