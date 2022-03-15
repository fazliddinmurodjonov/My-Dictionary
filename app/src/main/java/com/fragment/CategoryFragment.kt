package com.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import by.kirich1409.viewbindingdelegate.viewBinding
import com.adapter.CategoryAdapter
import com.example.androiddatabaselesson9.R
import com.example.androiddatabaselesson9.databinding.CustomCategoryDialogBinding
import com.example.androiddatabaselesson9.databinding.CustomDeleteDialogBinding
import com.example.androiddatabaselesson9.databinding.FragmentCategoryBinding
import com.room.database.DictionaryDatabase
import com.room.entity.Category
import com.util.Empty
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.function.Consumer
import kotlin.collections.ArrayList

class CategoryFragment : Fragment(R.layout.fragment_category) {
    private val binding: FragmentCategoryBinding by viewBinding()
    lateinit var dictionaryDatabase: DictionaryDatabase
    lateinit var categoryList: ArrayList<Category>
    lateinit var categoryAdapter: CategoryAdapter

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding)
        {
            dictionaryDatabase = DictionaryDatabase.getInstance(requireContext())
            categoryList = ArrayList()
            categoryAdapter = CategoryAdapter()
            dictionaryDatabase.categoryDao().getAllCategoryWithFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(@SuppressLint("NewApi")
                object : Consumer<List<Category>>,
                    @NonNull io.reactivex.rxjava3.functions.Consumer<List<Category>> {
                    override fun accept(t: List<Category>) {
                        categoryAdapter.submitList(t)
                    }

                },
                    @SuppressLint("NewApi")
                    object : Consumer<Throwable>,
                        @NonNull io.reactivex.rxjava3.functions.Consumer<Throwable> {
                        override fun accept(t: Throwable) {
                        }
                    })
            categoryRV.adapter = categoryAdapter

            categoryAdapter.setOnItemClickListenerForPopupMenu { category, position, imageView ->
                val popupMenu = PopupMenu(requireContext(), imageView)
                popupMenu.inflate(R.menu.popup_menu)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    popupMenu.setForceShowIcon(true)
                }
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.edit -> {
                            val dialog = Dialog(requireActivity())
                            val dialogView =
                                CustomCategoryDialogBinding.inflate(LayoutInflater.from(
                                    requireContext()), null, false)
                            dialog.setContentView(dialogView.root)
                            dialogView.category.setText(category.categoryName)
                            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            dialogView.cancelButton.setOnClickListener {
                                dialog.dismiss()
                            }
                            dialogView.saveButton.setOnClickListener {
                                val categoryName = dialogView.category.text.toString()
                                val categoryBol = Empty.empty(categoryName)
                                val categorySpace = Empty.space(categoryName)
                                if (categoryBol && categorySpace) {
                                    category.categoryName = categoryName

                                    dictionaryDatabase.categoryDao().updateCategory(category)

                                    dialog.dismiss()
                                }
                            }
                            dialog.show()
                        }
                        R.id.delete -> {
                            val dialog = Dialog(requireContext())
                            val dialogView =
                                CustomDeleteDialogBinding.inflate(LayoutInflater.from(requireContext()),
                                    null,
                                    false)
                            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            dialog.setContentView(dialogView.root)
                            dialogView.title.text =
                                "Do you want to delete this ${category.categoryName} ?"
                            dialogView.negativeButton.setOnClickListener {
                                dialog.dismiss()
                            }
                            dialogView.positiveButton.setOnClickListener {

                                var words = dictionaryDatabase.categoryWithWords()
                                    .getAllWordsByCategory(category.categoryId!!).word
                                for (word in words) {
                                    dictionaryDatabase.wordsDao().deleteWords(word)
                                }
                                dictionaryDatabase.categoryDao().deleteCategory(category)

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