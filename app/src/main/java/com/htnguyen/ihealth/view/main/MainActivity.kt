package com.htnguyen.ihealth.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.databinding.ActivityMainBinding
import com.htnguyen.ihealth.view.chat.ChatFragment
import com.htnguyen.ihealth.view.home.HomeFragment
import com.htnguyen.ihealth.view.profile.ProfileFragment
import com.htnguyen.ihealth.view.search.SearchFragment
import com.htnguyen.ihealth.view.social.SocialFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val imageResId = intArrayOf(
        R.drawable.ic_main_home,
        R.drawable.ic_main_social,
        R.drawable.ic_main_chat,
        R.drawable.ic_main_search,
        R.drawable.ic_main_profile
    )

    private val stringResId = intArrayOf(
        R.string.main_home,
        R.string.main_social,
        R.string.main_chat,
        R.string.main_search,
        R.string.main_profile
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setViewPager()
        setTabLayout()
    }

    private fun setTabLayout() {
        for (index in imageResId.indices) {
            binding.tabLayoutMain.addTab(
                binding.tabLayoutMain.newTab()
                    .setIcon(imageResId[index])
                    .setText(stringResId[index])
            )
        }
        /*TabLayoutMediator(binding.tabLayoutMain, binding.viewPagerMain) { tab, position ->
            tab.setIcon(imageResId[position])
        }.attach()*/
        binding.tabLayoutMain.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.position.let { binding.viewPagerMain.setCurrentItem(it, false) }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun setViewPager() {
        binding.viewPagerMain.adapter = BottomNavigationMainAdapter(this@MainActivity)
        binding.viewPagerMain.isUserInputEnabled = false


    }

    private class BottomNavigationMainAdapter(private val fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> HomeFragment()
                1 -> SocialFragment()
                2 -> ChatFragment()
                3 -> SearchFragment()
                4 -> ProfileFragment()
                else -> HomeFragment()
            }
        }

        override fun getItemCount(): Int {
            return 5
        }
    }
}