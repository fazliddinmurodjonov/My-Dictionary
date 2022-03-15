package com.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.adapter.ViewPagerAdapterFirst
import com.example.androiddatabaselesson9.R
import com.example.androiddatabaselesson9.databinding.FragmentHomeBinding
import com.example.androiddatabaselesson9.databinding.ItemTabLayoutBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment(R.layout.fragment_home) {
    var destroy = false
    private val binding: FragmentHomeBinding by viewBinding()
    lateinit var tabLayoutList: ArrayList<String>
    lateinit var fragments: ArrayList<Fragment>
    lateinit var viewPagerAdapterFirst: ViewPagerAdapterFirst
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding)
        {
            fragments = ArrayList()
            fragments = arrayListOf(MainFragment(), FavouriteFragment())
            viewPagerAdapterFirst = ViewPagerAdapterFirst(
                fragments,
                requireActivity())
            binding.viewPagerFirst.adapter = viewPagerAdapterFirst
            binding.viewPagerFirst.isUserInputEnabled = false
            TabLayoutMediator(binding.tabLayoutFirst, binding.viewPagerFirst) { tab, position ->
            }.attach()
            tabLayoutList = ArrayList()
            tabLayoutList.add("Home")
            tabLayoutList.add("Favourite")
            setTab()
            setHasOptionsMenu(true)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {
                findNavController().navigate(R.id.settingFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setTab() {
        val tabCount = binding.tabLayoutFirst.tabCount
        for (i in 0 until tabCount) {
            val itemTabLayoutBinding =
                ItemTabLayoutBinding.inflate(LayoutInflater.from(requireContext()), null, false)
            val tabAt = binding.tabLayoutFirst.getTabAt(i)
            tabAt!!.customView = itemTabLayoutBinding.root
            itemTabLayoutBinding.text.text = tabLayoutList[i]
            if (i == 0) {
                itemTabLayoutBinding.image.setImageResource(R.drawable.home_selector)
            } else {
                itemTabLayoutBinding.image.setImageResource(R.drawable.favourite_selector)
            }

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()

        if (destroy) {
            fragments = arrayListOf(MainFragment(), FavouriteFragment())
            viewPagerAdapterFirst = ViewPagerAdapterFirst(
                fragments,
                requireActivity())
            binding.viewPagerFirst.adapter = viewPagerAdapterFirst
        }
        destroy = false

    }

    override fun onDestroyView() {
        super.onDestroyView()
        destroy = true

    }
}