/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.example.native40.databinding.StartFragmentBinding

class StartFragment : BaseFragment() {

    private val viewModel by viewModels<StartViewModel>()

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
            initBaseFragment(viewModel)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logger.info("onViewCreated")
        setFragmentResultListener(RequestKey.ALERT.rawValue) { _, _ ->
            viewModel.destination.value = Destination.REPLACE_HOME
        }
        if (savedInstanceState == null) {
            viewModel.start()
        }
    }
}