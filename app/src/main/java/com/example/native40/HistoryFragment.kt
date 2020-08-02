/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import android.app.Activity
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
import com.example.native40.databinding.HistoryFragmentBinding
import com.example.native40.db.User
import java.text.SimpleDateFormat
import java.util.*

class HistoryFragment : BaseFragment() {

    companion object {
        fun newInstance() = HistoryFragment()
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[HistoryViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logger.info("onCreateView")
        val binding = DataBindingUtil.inflate<HistoryFragmentBinding>(
            inflater,
            R.layout.history_fragment,
            container,
            false
        ).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            initBaseFragment(viewModel)
            it.recyclerView.layoutManager = LinearLayoutManager(context)
            it.recyclerView.adapter = UserAdapter(object : DiffUtil.ItemCallback<User>() {
                override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                    return oldItem == newItem
                }
            }, object : OnAdapterClickListener {
                override fun onClick(login: String) {
                    logger.info("onClick $login")
                    viewModel.dialogMessage.value = DialogMessage(
                        RequestCode.OK_CANCEL,
                        R.string.dialog_title_history_remove,
                        R.string.dialog_message_history_remove,
                        listOf(login)
                    )
                }
            }).also { adapter ->
                viewModel.items.observe(viewLifecycleOwner, Observer { items ->
                    logger.info("viewModel.items changed. ${items?.size}")
                    adapter.submitList(items)
                })
            }
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RequestCode.OK_CANCEL.rawValue) {
            logger.info("onActivityResult requestCode=$requestCode resultCode=$resultCode")
            if (resultCode == Activity.RESULT_OK) {
                data?.getStringExtra(ExtraKey.SINGLE_CHOICE.rawValue)?.let {
                    viewModel.remove(it)
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    interface OnAdapterClickListener {
        fun onClick(login: String)
    }

    class UserAdapter(
        diffCallback: DiffUtil.ItemCallback<User>,
        private val listener: OnAdapterClickListener
    ) :
        ListAdapter<User, UserAdapter.ViewHolder>(diffCallback) {
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val text1: TextView = itemView.findViewById(android.R.id.text1)
            val text2: TextView = itemView.findViewById(android.R.id.text2)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(android.R.layout.simple_list_item_2, parent, false)
            ).also { viewHolder ->
                viewHolder.itemView.setOnClickListener {
                    val text = it.findViewById<TextView>(android.R.id.text1).text.toString()
                    listener.onClick(text)
                }
            }
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.text1.text = getItem(position).login
            holder.text2.text = SimpleDateFormat(
                "yyyy/MM/dd HH:mm",
                Locale.getDefault()
            ).format(getItem(position).timeStamp)
        }
    }
}