package com.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.adapter.WordsAdapter
import com.example.androiddatabaselesson9.R
import com.example.androiddatabaselesson9.databinding.FragmentFavouriteBinding
import com.room.database.DictionaryDatabase
import com.room.entity.Words

class FavouriteFragment : Fragment(R.layout.fragment_favourite) {
    private val binding: FragmentFavouriteBinding by viewBinding()
    lateinit var dictionaryDatabase: DictionaryDatabase
    lateinit var wordsAdapter: WordsAdapter
    lateinit var wordFavouriteList: ArrayList<Words>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding)
        {
            dictionaryDatabase = DictionaryDatabase.getInstance(requireContext())
            wordFavouriteList = ArrayList()
            wordFavouriteList.addAll(dictionaryDatabase.wordsDao().getFavouriteWords())
            wordsAdapter = WordsAdapter(wordFavouriteList)
            wordFavouriteRV.adapter = wordsAdapter
            wordsAdapter.setOnItemClickListener { word ->
                val bundle = bundleOf("wordId" to word.wordId)
                findNavController().navigate(R.id.infoFragment, bundle)
            }
        }
    }

}