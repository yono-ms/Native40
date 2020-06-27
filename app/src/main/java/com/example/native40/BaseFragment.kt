/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class BaseFragment : Fragment() {
    val logger: Logger by lazy {
        LoggerFactory.getLogger(javaClass.simpleName)
    }

    val mainViewModel by activityViewModels<MainViewModel>()

    fun initBaseFragment(viewModel: BaseViewModel) {
        logger.info("initBaseFragment vm=$viewModel")

        viewModel.dialogMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                logger.info(it.toString())
                val dialog = AlertDialogFragment.newInstance(it, this)
                dialog.show(requireActivity().supportFragmentManager, "alert")
                viewModel.dialogMessage.value = null
            }
        })

        viewModel.destination.observe(viewLifecycleOwner, Observer {
            it?.let {
                logger.info(it.toString())
                transition(it)
                viewModel.destination.value = null
            }
        })
    }

    private fun transition(destination: Destination) {
        logger.info("transition destination=$destination")
        val id = R.id.frameLayoutContent
        requireActivity().supportFragmentManager.beginTransaction().let {
            when (destination) {
                Destination.REPLACE_HOME -> it.replace(id, HomeFragment.newInstance()).commit()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        logger.info("onActivityResult requestCode=$requestCode resultCode=$resultCode")
        super.onActivityResult(requestCode, resultCode, data)
    }
}