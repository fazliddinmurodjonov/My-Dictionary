package com.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.size
import by.kirich1409.viewbindingdelegate.viewBinding
import com.adapter.ViewPagerAdapterMain
import com.example.androiddatabaselesson9.R
import com.example.androiddatabaselesson9.databinding.FragmentMainBinding
import com.example.androiddatabaselesson9.databinding.ItemTabLayoutBinding
import com.example.androiddatabaselesson9.databinding.ItemTabLayoutMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.room.database.DictionaryDatabase
import com.room.entity.Category

class MainFragment : Fragment(R.layout.fragment_main) {
    private val binding: FragmentMainBinding by viewBinding()
    private lateinit var categoryList: ArrayList<Category>
    lateinit var dictionaryDatabase: DictionaryDatabase
    lateinit var viewPagerAdapterMain: ViewPagerAdapterMain
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding)
        {
            dictionaryDatabase = DictionaryDatabase.getInstance(requireContext())
            categoryList = ArrayList()
            categoryList.addAll(dictionaryDatabase.categoryDao().getAllCategory())

            viewPagerAdapterMain = ViewPagerAdapterMain(categoryList, requireActivity())
            categoryWordViewPager.adapter = viewPagerAdapterMain
            TabLayoutMediator(categoryWordTabLayout, categoryWordViewPager) { tab, position ->
            }.attach()
            setTab()

        }
    }

    private fun setTab() {
        val size = binding.categoryWordTabLayout.tabCount
        for (i in 0 until size) {
            val itemTabLayoutMainBinding =
                ItemTabLayoutMainBinding.inflate(LayoutInflater.from(requireContext()),
                    null,
                    false)
            val tabAt = binding.categoryWordTabLayout.getTabAt(i)
            tabAt?.customView = itemTabLayoutMainBinding.root
            itemTabLayoutMainBinding.categoryName.text = categoryList[i].categoryName
        }
    }


}