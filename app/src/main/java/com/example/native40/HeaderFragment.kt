/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.native40.databinding.HeaderFragmentBinding
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class HeaderFragment : Fragment() {

    companion object {
        fun newInstance() = HeaderFragment()
    }

    val logger: Logger by lazy {
        LoggerFactory.getLogger(javaClass.simpleName)
    }

    private val mainViewModel by activityViewModels<MainViewModel>()

    private val viewModel by lazy {
        ViewModelProvider(this)[HeaderViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logger.info("onCreateView")
        val binding = DataBindingUtil.inflate<HeaderFragmentBinding>(
            inflater,
            R.layout.header_fragment,
            container,
            false
        ).also {
            it.mainViewModel = mainViewModel
            it.lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }
}