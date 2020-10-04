/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import org.slf4j.LoggerFactory

class AlertDialogFragment : DialogFragment() {

    private val args by navArgs<AlertDialogFragmentArgs>()

    private val logger = LoggerFactory.getLogger(javaClass.simpleName)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { activity ->
            AlertDialog.Builder(activity).apply {
                if (args.title != 0) {
                    setTitle(args.title)
                }
                when (args.requestKey) {
                    RequestKey.ALERT.rawValue, RequestKey.OK_CANCEL.rawValue -> {
                        if (args.message != 0) {
                            if (args.messageArgs?.isNotEmpty() == true) {
                                args.messageArgs?.let {
                                    setMessage(getString(args.message, *it))
                                }
                            } else {
                                setMessage(args.message)
                            }
                        }
                        args.messageText?.let {
                            setMessage(it)
                        }
                        setPositiveButton(R.string.button_ok) { dialog, which ->
                            logger.info("dialog=$dialog which=$which")
                            setFragmentResult(
                                args.requestKey,
                                bundleOf(
                                    BundleKey.RESULT.rawValue to Activity.RESULT_OK,
                                    // arg[0]へのOK/CANCEL
                                    BundleKey.SELECTED.rawValue to args.messageArgs?.get(0)
                                )
                            )
                            dialog.dismiss()
                        }
                        setNegativeButton(R.string.button_cancel) { dialog, which ->
                            logger.info("dialog=$dialog which=$which")
                            setFragmentResult(
                                args.requestKey,
                                bundleOf(BundleKey.RESULT.rawValue to Activity.RESULT_CANCELED)
                            )
                            dialog.dismiss()
                        }
                    }
                    RequestKey.SINGLE_CHOICE.rawValue -> {
                        args.items?.let { items ->
                            setItems(items) { dialog, which ->
                                logger.info("dialog=$dialog which=$which")
                                setFragmentResult(
                                    args.requestKey,
                                    bundleOf(
                                        BundleKey.RESULT.rawValue to Activity.RESULT_OK,
                                        BundleKey.SELECTED.rawValue to items[which]
                                    )
                                )
                                dialog.dismiss()
                            }
                        }
                    }
                }
            }.create()
                .also { isCancelable = args.requestKey == RequestKey.SINGLE_CHOICE.rawValue }
        } ?: throw IllegalStateException("activity is null.")
    }
}