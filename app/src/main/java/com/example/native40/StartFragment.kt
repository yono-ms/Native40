/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.native40.databinding.StartFragmentBinding

class StartFragment : BaseFragment() {

    companion object {
        fun newInstance() = StartFragment()
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[StartViewModel::class.java].also {
            initBaseFragment(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logger.info("onCreateView")
        val binding = DataBindingUtil.inflate<StartFragmentBinding>(
            inflater,
            R.layout.start_fragment,
            container,
            false
        ).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logger.info("onViewCreated")
        if (savedInstanceState == null) {
            viewModel.start()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RequestCode.ALERT.rawValue -> viewModel.destination.value = Destination.REPLACE_HOME
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }
}