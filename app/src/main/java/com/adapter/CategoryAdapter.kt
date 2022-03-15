package com.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddatabaselesson9.databinding.ItemCategoryBinding
import com.room.entity.Category

class CategoryAdapter : ListAdapter<Category, CategoryAdapter.ViewHolder>(MyDiffUtil()) {
    lateinit var popupMenuAdapter: OnItemClickListenerForPopupMenu

    fun interface OnItemClickListenerForPopupMenu {
        fun onItemClick(category: Category, position: Int, imageView: ImageView)
    }

    fun setOnItemClickListenerForPopupMenu(popupMenuListener: OnItemClickListenerForPopupMenu) {
        popupMenuAdapter = popupMenuListener
    }

    inner class ViewHolder(var binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(category: Category, position: Int) {
            binding.categoryName.text = category.categoryName
            binding.setting.setOnClickListener {
                popupMenuAdapter.onItemClick(category, position, binding.setting)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position), position)
    }

    class MyDiffUtil : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.categoryId == newItem.categoryId
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }

    }
}