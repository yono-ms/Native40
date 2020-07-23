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
import androidx.databinding.ObservableList
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.native40.databinding.HomeFragmentBinding

class HomeFragment : BaseFragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java].also {
            initBaseFragment(it)
        }
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
            it.recyclerView.adapter = RecyclerAdapter(viewModel.items)
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

    class RecyclerAdapter(private val items: ObservableList<Pair<String, String>>) :
        RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

        init {
            items.addOnListChangedCallback(object :
                ObservableList.OnListChangedCallback<ObservableList<Pair<String, String>>>() {
                override fun onChanged(sender: ObservableList<Pair<String, String>>?) {
                    notifyDataSetChanged()
                }

                override fun onItemRangeRemoved(
                    sender: ObservableList<Pair<String, String>>?,
                    positionStart: Int,
                    itemCount: Int
                ) {
                    notifyItemRangeRemoved(positionStart, itemCount)
                }

                override fun onItemRangeMoved(
                    sender: ObservableList<Pair<String, String>>?,
                    fromPosition: Int,
                    toPosition: Int,
                    itemCount: Int
                ) {
                    notifyDataSetChanged()
                }

                override fun onItemRangeInserted(
                    sender: ObservableList<Pair<String, String>>?,
                    positionStart: Int,
                    itemCount: Int
                ) {
                    notifyItemRangeInserted(positionStart, itemCount)
                }

                override fun onItemRangeChanged(
                    sender: ObservableList<Pair<String, String>>?,
                    positionStart: Int,
                    itemCount: Int
                ) {
                    notifyItemRangeChanged(positionStart, itemCount)
                }
            })
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(android.R.layout.simple_list_item_2, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemView.findViewById<TextView>(android.R.id.text1).text = items[position].first
            holder.itemView.findViewById<TextView>(android.R.id.text2).text = items[position].second
        }
    }

}