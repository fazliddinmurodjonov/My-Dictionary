package com.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddatabaselesson9.databinding.ItemWordBinding
import com.room.entity.Words

class WordFlowableAdapter : ListAdapter<Words, WordFlowableAdapter.ViewHolder>(MyDiffUtil()) {
    lateinit var popupMenuAdapter: OnItemClickListenerForPopupMenu

    fun interface OnItemClickListenerForPopupMenu {
        fun onItemClick(words: Words, position: Int, imageView: ImageView)
    }

    fun setOnItemClickListenerForPopupMenu(popupMenuListener: OnItemClickListenerForPopupMenu) {
        popupMenuAdapter = popupMenuListener
    }

    inner class ViewHolder(var binding: ItemWordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(words: Words, position: Int) {
            binding.word.text = words.wordName
            binding.translate.text = words.wordTranslation
            binding.setting.setOnClickListener {
                popupMenuAdapter.onItemClick(words, position, binding.setting)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemWordBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        )
    }


    class MyDiffUtil : DiffUtil.ItemCallback<Words>() {
        override fun areItemsTheSame(oldItem: Words, newItem: Words): Boolean {
            return oldItem.wordId == newItem.wordId
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Words, newItem: Words): Boolean {
            return oldItem == newItem
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position), position)
    }


}