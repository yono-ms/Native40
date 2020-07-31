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
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
            it.radioGroup.setOnCheckedChangeListener { _, i ->
                logger.info("OnCheckedChangeListener $i")
                viewModel.sort(i)
            }
            it.recyclerView.layoutManager = LinearLayoutManager(context)
            it.recyclerView.adapter =
                RecyclerAdapter(viewModel.items, object : OnAdapterClickListener {
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
                    viewModel.items.observe(viewLifecycleOwner, Observer {
                        logger.info("viewModel.items changed.")
                        adapter.notifyDataSetChanged()
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

    class RecyclerAdapter(
        private val items: LiveData<List<User>>,
        private val listener: OnAdapterClickListener
    ) :
        RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

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

        override fun getItemCount(): Int {
            return items.value?.size ?: 0
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            items.value?.get(position)?.login?.let {
                holder.itemView.findViewById<TextView>(android.R.id.text1).text = it
            }
            items.value?.get(position)?.timeStamp?.let {
                holder.itemView.findViewById<TextView>(android.R.id.text2).text =
                    SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).format(it)
            }
        }
    }
}