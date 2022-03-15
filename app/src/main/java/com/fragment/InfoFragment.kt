package com.fragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androiddatabaselesson9.R
import com.example.androiddatabaselesson9.databinding.FragmentInfoBinding
import com.room.database.DictionaryDatabase
import com.room.entity.Words

class InfoFragment : Fragment(R.layout.fragment_info) {
    private val binding: FragmentInfoBinding by viewBinding()
    private lateinit var dictionaryDatabase: DictionaryDatabase
    var change = false
    lateinit var wordObj: Words
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        with(binding)
        {
            val wordId = arguments?.getInt("wordId")
            dictionaryDatabase = DictionaryDatabase.getInstance(requireContext())
            wordObj = dictionaryDatabase.wordsDao().getWordsById(wordId!!)
            word.text = wordObj.wordName
            if (wordObj.wordFavourite == 1) {
                imageButton.setImageResource(R.drawable.like)
            }
            imageButton.setOnClickListener {
                if (wordObj.wordFavourite == 1) {
                    imageButton.setImageResource(R.drawable.like_empty)
                    wordObj.wordFavourite = 0
                } else {
                    imageButton.setImageResource(R.drawable.like)
                    wordObj.wordFavourite = 1
                }
                change = change == false
            }
            loadActionBar(wordObj.wordName!!)
            wordTranslation.text = wordObj.wordTranslation
            if (wordObj.wordImage == null) {
                wordImage.setImageResource(R.drawable.place_holder)
            } else {
                wordImage.setImageURI(Uri.parse(wordObj.wordImage))
            }
        }

    }

    private fun loadActionBar(word: String) {
        (activity as AppCompatActivity).supportActionBar!!.setHomeAsUpIndicator(R.drawable.back)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.title = word
    }

    override fun onDestroy() {
        super.onDestroy()

        if (change) {
            dictionaryDatabase.wordsDao().updateWords(wordObj)
        }
    }

}