package com.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.adapter.ViewPagerAdapterSetting
import com.example.androiddatabaselesson9.R
import com.example.androiddatabaselesson9.databinding.CustomCategoryDialogBinding
import com.example.androiddatabaselesson9.databinding.FragmentSettingBinding
import com.example.androiddatabaselesson9.databinding.ItemCategoryBinding
import com.example.androiddatabaselesson9.databinding.ItemTabLayoutBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.room.database.DictionaryDatabase
import com.room.entity.Category
import com.util.Empty
import io.reactivex.rxjava3.core.Observable
import java.util.*
import kotlin.collections.ArrayList

class SettingFragment : Fragment(R.layout.fragment_setting) {
    private val binding: FragmentSettingBinding by viewBinding()
    lateinit var tabLayoutList: ArrayList<String>
    lateinit var dictionaryDatabase: DictionaryDatabase
    var viewpagerPosition: Int = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding)
        {
            loadActionBar()
            dictionaryDatabase = DictionaryDatabase.getInstance(requireContext())
            tabLayoutList = ArrayList()
            tabLayoutList.add("Category")
            tabLayoutList.add("Words")
            val fragments: ArrayList<Fragment> = arrayListOf(CategoryFragment(), WordsFragment())
            val viewPagerAdapterSetting = ViewPagerAdapterSetting(fragments, requireActivity())
            viewPagerSetting.adapter = viewPagerAdapterSetting
            viewPagerSetting.isUserInputEnabled = false
            TabLayoutMediator(tabLayoutSetting, viewPagerSetting) { tab, position ->
            }.attach()
            setTab()
            setHasOptionsMenu(true)
            viewPagerSetting.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    viewpagerPosition = position
                }
            })
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.setting_menu, menu)
    }

    private fun loadActionBar() {
        (activity as AppCompatActivity).supportActionBar!!.setHomeAsUpIndicator(R.drawable.back)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> {
                when (viewpagerPosition) {
                    0 -> {
                        val dialog = Dialog(requireActivity())
                        val dialogView =
                            CustomCategoryDialogBinding.inflate(LayoutInflater.from(requireContext()),
                                null,
                                false)

                        dialog.setContentView(dialogView.root)
                        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        dialogView.cancelButton.setOnClickListener {
                            dialog.dismiss()
                        }
                        dialogView.saveButton.setOnClickListener {
                            val categoryName = dialogView.category.text.toString()
                            val categoryBol = Empty.empty(categoryName)
                            val categorySpace = Empty.space(categoryName)
                            if (categoryBol && categorySpace) {
                                val category = Category()
                                category.categoryName = categoryName

                                dictionaryDatabase.categoryDao().insertCategory(category)

                                dialog.dismiss()
                            }

                        }
                        dialog.show()
                    }
                    1 -> {
                        findNavController().navigate(R.id.addWordFragment)
                    }
                    else -> {
                        findNavController().popBackStack()
                    }
                }
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun setTab() {
        val tabCount = binding.tabLayoutSetting.tabCount
        for (i in 0 until tabCount) {
            val itemTabLayoutBinding =
                ItemTabLayoutBinding.inflate(LayoutInflater.from(requireContext()), null, false)
            val tabAt = binding.tabLayoutSetting.getTabAt(i)
            tabAt?.customView = itemTabLayoutBinding.root
            itemTabLayoutBinding.text.text = tabLayoutList[i]
            if (i == 0) {
                itemTabLayoutBinding.image.setImageResource(R.drawable.category_selector)
            } else {
                itemTabLayoutBinding.image.setImageResource(R.drawable.words_selector)

            }
        }
    }


}