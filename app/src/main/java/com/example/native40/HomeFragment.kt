/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.native40.databinding.HomeFragmentBinding

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
            it.buttonEditHistory.setOnClickListener {
                viewModel.destination.value = Destination.PUSH_HISTORY
            }
            it.recyclerView.layoutManager = LinearLayoutManager(context)
            it.recyclerView.adapter = PairAdapter(object :
                DiffUtil.ItemCallback<Pair<String, String>>() {
                override fun areItemsTheSame(
                    oldItem: Pair<String, String>,
                    newItem: Pair<String, String>
                ): Boolean {
                    return oldItem.first == newItem.first
                }

                override fun areContentsTheSame(
                    oldItem: Pair<String, String>,
                    newItem: Pair<String, String>
                ): Boolean {
                    return oldItem == newItem
                }
            }).also { adapter ->
                viewModel.items.observe(viewLifecycleOwner, Observer { items ->
                    logger.info("viewModel.items changed.")
                    adapter.submitList(items)
                })
            }
            initBaseFragment(viewModel)
        }
        return binding.root
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

    class PairAdapter(diffCallback: DiffUtil.ItemCallback<Pair<String, String>>) :
        ListAdapter<Pair<String, String>, PairAdapter.ViewHolder>(diffCallback) {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val text1: TextView = itemView.findViewById(android.R.id.text1)
            val text2: TextView = itemView.findViewById(android.R.id.text2)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(android.R.layout.simple_list_item_2, parent, false)
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.text1.text = getItem(position).first
            holder.text2.text = getItem(position).second
        }
    }
}