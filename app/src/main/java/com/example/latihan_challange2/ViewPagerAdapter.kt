package com.example.latihan_challange2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val JUMLAH_MENU = 4

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> { return HomeFragment() }
            1 -> { return TrackerFragment() }
            2 -> { return HospitalFragment() }
            3 -> { return AboutFragment() }
            else -> {
                return HomeFragment()
            }
        }
    }

    override fun getItemCount(): Int {
        return JUMLAH_MENU
    }

}