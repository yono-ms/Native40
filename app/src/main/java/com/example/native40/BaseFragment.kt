/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.native40.extension.dialogMessage
import com.example.native40.extension.dialogTitle
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class BaseFragment : Fragment() {
    val logger: Logger by lazy {
        LoggerFactory.getLogger(javaClass.simpleName)
    }

    val mainViewModel by activityViewModels<MainViewModel>()

    fun initBaseFragment(viewModel: BaseViewModel) {
        logger.info("initBaseFragment vm=$viewModel")

        viewModel.dialogMessage.observe(viewLifecycleOwner, {
            it?.let {
                logger.info(it.toString())
                val dialogTitle = it.throwable?.dialogTitle ?: it.title ?: 0
                val dialogMessage = it.message ?: 0
                findNavController().navigate(
                    AlertDialogFragmentDirections.actionGlobalAlertDialog(
                        it.requestKey.rawValue,
                        dialogTitle,
                        dialogMessage,
                        it.messageArgs?.toTypedArray(),
                        it.throwable?.dialogMessage,
                        it.items?.toTypedArray()
                    )
                )
                viewModel.dialogMessage.value = null
            }
        })

        viewModel.destination.observe(viewLifecycleOwner, {
            it?.let {
                logger.info(it.toString())
                transition(it)
                viewModel.destination.value = null
            }
        })
    }

    private fun transition(destination: Destination) {
        logger.info("transition destination=$destination")
        when (destination) {
            Destination.REPLACE_HOME -> findNavController().navigate(R.id.action_start_to_home)
            Destination.PUSH_HISTORY -> findNavController().navigate(R.id.action_home_to_history)
            Destination.PUSH_SETTINGS -> findNavController().navigate(R.id.action_home_to_settings)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        logger.info("onSaveInstanceState")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.info("onCreate savedInstanceState=$savedInstanceState")
    }

    override fun onStart() {
        super.onStart()
        logger.info("onStart")
    }

    override fun onResume() {
        super.onResume()
        logger.info("onResume")
        mainViewModel.headerText.value = when (this) {
            is HomeFragment -> getString(R.string.fragment_title_home)
            is HistoryFragment -> getString(R.string.fragment_title_history)
            else -> getString(R.string.app_name)
        }
    }

    override fun onPause() {
        super.onPause()
        logger.info("onPause")
    }

    override fun onStop() {
        super.onStop()
        logger.info("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        logger.info("onDestroy")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        logger.info("onActivityResult requestCode=$requestCode resultCode=$resultCode")
        super.onActivityResult(requestCode, resultCode, data)
    }
}