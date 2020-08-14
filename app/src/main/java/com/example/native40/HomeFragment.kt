/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.native40.databinding.HomeFragmentBinding
import com.example.native40.databinding.RepositoryItemBinding

class HomeFragment : BaseFragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logger.info("onCreateView")
        val binding = DataBindingUtil.inflate<HomeFragmentBinding>(
            inflater,
            R.layout.home_fragment,
            container,
            false
        ).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            it.buttonHistory.setOnClickListener {
                viewModel.onHistory()
            }
            it.button.setOnClickListener {
                viewModel.onClick(mainViewModel)
            }
            it.recyclerView.layoutManager = LinearLayoutManager(context)
            it.recyclerView.adapter = RepositoryAdapter().also { adapter ->
                viewModel.items.observe(viewLifecycleOwner, Observer { items ->
                    logger.info("viewModel.items changed.")
                    adapter.submitList(items)
                })
            }
            initBaseFragment(viewModel)
            setHasOptionsMenu(true)
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.actionHistory -> {
                viewModel.destination.value = Destination.PUSH_HISTORY
                true
            }
            R.id.actionSettings -> {
                viewModel.destination.value = Destination.PUSH_SETTINGS
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RequestCode.SINGLE_CHOICE.rawValue) {
            logger.info("onActivityResult requestCode=$requestCode resultCode=$resultCode")
            data?.getStringExtra(ExtraKey.SINGLE_CHOICE.rawValue)?.let {
                viewModel.onSelect(it)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    class RepositoryAdapter :
        ListAdapter<HomeViewModel.Repository, RepositoryAdapter.ViewHolder>(object :
            DiffUtil.ItemCallback<HomeViewModel.Repository>() {
            override fun areItemsTheSame(
                oldItem: HomeViewModel.Repository,
                newItem: HomeViewModel.Repository
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: HomeViewModel.Repository,
                newItem: HomeViewModel.Repository
            ): Boolean {
                return oldItem == newItem
            }
        }) {

        class ViewHolder(val binding: RepositoryItemBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = DataBindingUtil.inflate<RepositoryItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.repository_item,
                parent,
                false
            )
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.binding.repository = getItem(position)
        }
    }
}