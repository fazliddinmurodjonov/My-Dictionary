package com.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.adapter.WordFlowableAdapter
import com.example.androiddatabaselesson9.R
import com.example.androiddatabaselesson9.databinding.CustomDeleteDialogBinding
import com.example.androiddatabaselesson9.databinding.FragmentWordsBinding
import com.room.database.DictionaryDatabase
import com.room.entity.Words
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.function.Consumer

class WordsFragment : Fragment(R.layout.fragment_words) {
    private val binding: FragmentWordsBinding by viewBinding()
    lateinit var dictionaryDatabase: DictionaryDatabase
    lateinit var wordFlowableAdapter: WordFlowableAdapter
    lateinit var wordList: ArrayList<Words>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding)
        {
            dictionaryDatabase = DictionaryDatabase.getInstance(requireContext())
            wordList = ArrayList()
            wordFlowableAdapter = WordFlowableAdapter()
            dictionaryDatabase.wordsDao().getAllWordWithFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(@SuppressLint("NewApi")
                object : Consumer<List<Words>>,
                    @NonNull io.reactivex.rxjava3.functions.Consumer<List<Words>> {
                    override fun accept(t: List<Words>) {
                        wordFlowableAdapter.submitList(t)
                    }

                },
                    @SuppressLint("NewApi")
                    object : Consumer<Throwable>,
                        @NonNull io.reactivex.rxjava3.functions.Consumer<Throwable> {
                        override fun accept(t: Throwable) {

                        }

                    })
            wordsRV.adapter = wordFlowableAdapter
            wordFlowableAdapter.setOnItemClickListenerForPopupMenu { words, position, imageView ->
                val popupMenu = PopupMenu(requireContext(), imageView)
                popupMenu.inflate(R.menu.popup_menu)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    popupMenu.setForceShowIcon(true)
                }
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.edit -> {
                            val bundleOf = bundleOf("wordId" to words.wordId)
                            findNavController().navigate(R.id.editWordFragment, bundleOf)
                        }
                        R.id.delete -> {
                            val dialog = Dialog(requireContext())
                            val dialogView =
                                CustomDeleteDialogBinding.inflate(LayoutInflater.from(requireContext()),
                                    null,
                                    false)

                            dialog.setContentView(dialogView.root)
                            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            dialogView.title.text =
                                "Do you want to delete this ${words.wordName} ?"
                            dialogView.negativeButton.setOnClickListener {
                                dialog.dismiss()
                            }
                            dialogView.positiveButton.setOnClickListener {
                                dictionaryDatabase.wordsDao().deleteWords(words)

                                dialog.dismiss()
                            }
                            dialog.show()
                        }
                    }
                    true
                }
                popupMenu.show()
            }
        }
    }

}