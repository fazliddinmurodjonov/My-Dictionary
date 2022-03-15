package com.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddatabaselesson9.databinding.ItemWordFavouriteBinding
import com.room.entity.Words

class WordsAdapter(var wordList: ArrayList<Words>) : RecyclerView.Adapter<WordsAdapter.ViewHolder>() {
    lateinit var itemClick: OnItemClickListener

    fun interface OnItemClickListener {
        fun onClick(word: Words)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClick = listener
    }

    inner class ViewHolder(var binding: ItemWordFavouriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(word: Words, position: Int) {
            binding.word.text = word.wordName
            binding.translate.text = word.wordTranslation
            binding.root.setOnClickListener {
                itemClick.onClick(word)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemWordFavouriteBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val word = wordList[position]
        holder.onBind(word, position)
    }

    override fun getItemCount(): Int = wordList.size
}