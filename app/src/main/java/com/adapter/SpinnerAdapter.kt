package com.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import com.example.androiddatabaselesson9.R
import com.example.androiddatabaselesson9.databinding.ItemSpinnerBinding
import com.room.entity.Category

class SpinnerAdapter(var list: ArrayList<Category>, var context: Context) : BaseAdapter() {

    override fun isEnabled(position: Int): Boolean = position != 0

    override fun getCount(): Int = list.size

    override fun getItem(position: Int): Category = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return super.getDropDownView(position, convertView, parent)
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding =
            ItemSpinnerBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        binding.categoryName.text = list[position].categoryName
        if (position == 0) {
            binding.categoryName.setTextColor(ContextCompat.getColor(context, R.color.iconColor))
        }
        var itemView: View
        itemView = binding.root
        isEnabled(position)
        return itemView
    }

}