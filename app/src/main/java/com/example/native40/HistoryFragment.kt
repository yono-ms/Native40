/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.native40.databinding.HistoryFragmentBinding
import com.example.native40.databinding.UserItemBinding
import com.example.native40.db.User

class HistoryFragment : BaseFragment() {

    private val viewModel by viewModels<HistoryViewModel>()

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
            it.recyclerView.adapter = UserAdapter().also { adapter ->
                viewModel.items.observe(viewLifecycleOwner, { items ->
                    logger.info("viewModel.items changed. ${items?.size}")
                    adapter.submitList(items)
                })
                adapter.setOnClickListener { login ->
                    logger.info("onClick $login")
                    viewModel.dialogMessage.value = DialogMessage(
                        RequestKey.OK_CANCEL,
                        R.string.dialog_title_history_remove,
                        R.string.dialog_message_history_remove,
                        listOf(login)
                    )
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener(RequestKey.OK_CANCEL.rawValue) { requestKey, bundle ->
            logger.info("onActivityResult requestKey=$requestKey bundle=$bundle")
            if (bundle.getInt(BundleKey.RESULT.rawValue) == Activity.RESULT_OK) {
                bundle.getString(BundleKey.SELECTED.rawValue)?.let {
                    viewModel.remove(it)
                }
            }
        }
    }

    class UserAdapter :
        ListAdapter<User, UserAdapter.ViewHolder>(object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }) {

        class ViewHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = DataBindingUtil.inflate<UserItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.user_item,
                parent,
                false
            )
            return ViewHolder(binding).also {
                it.itemView.setOnClickListener {
                    val text = binding.user?.login ?: "???"
                    onClick(text)
                }
            }
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.binding.user = getItem(position)
        }

        lateinit var onClick: (login: String) -> Unit

        fun setOnClickListener(listener: (login: String) -> Unit) {
            this.onClick = listener
        }
    }
}