package com.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fragment.MainViewPagerFragment
import com.room.entity.Category

class ViewPagerAdapterMain(
    val categoryList: ArrayList<Category>,
    fragmentActivity: FragmentActivity,
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun createFragment(position: Int): Fragment {
        return MainViewPagerFragment.newInstance(categoryList[position].categoryId!!)
    }
}