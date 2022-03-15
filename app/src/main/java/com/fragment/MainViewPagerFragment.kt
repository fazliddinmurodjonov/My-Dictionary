package com.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.adapter.WordsAdapter
import com.example.androiddatabaselesson9.R
import com.example.androiddatabaselesson9.databinding.FragmentMainViewPagerBinding
import com.room.database.DictionaryDatabase
import com.room.entity.Words

class MainViewPagerFragment : Fragment(R.layout.fragment_main_view_pager) {
    private val binding: FragmentMainViewPagerBinding by viewBinding()
    lateinit var dictionaryDatabase: DictionaryDatabase
    lateinit var wordList: ArrayList<Words>
    lateinit var wordsAdapter: WordsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding)
        {
            val categoryId = arguments?.getInt(ID_ARG)
            wordList = ArrayList()
            dictionaryDatabase = DictionaryDatabase.getInstance(requireContext())
            wordList.addAll(dictionaryDatabase.categoryWithWords()
                .getAllWordsByCategory(categoryId!!).word
            )
            wordsAdapter = WordsAdapter(wordList)
            wordCategoryRV.adapter = wordsAdapter
            wordsAdapter.setOnItemClickListener { word ->
                val bundle = bundleOf("wordId" to word.wordId)
                findNavController().navigate(R.id.infoFragment, bundle)
            }
        }


    }

    companion object {
        private const val ID_ARG = "id_arg"
        @JvmStatic
        fun newInstance(id: Int) =
            MainViewPagerFragment().apply {
                arguments = Bundle().apply {
                    putInt(ID_ARG, id)
                }
            }
    }
}