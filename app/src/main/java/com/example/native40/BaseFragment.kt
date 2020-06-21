/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class BaseFragment : Fragment() {
    val logger: Logger by lazy {
        LoggerFactory.getLogger(javaClass.simpleName)
    }

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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        logger.info("onActivityResult requestCode=$requestCode resultCode=$resultCode")
        super.onActivityResult(requestCode, resultCode, data)
    }
}