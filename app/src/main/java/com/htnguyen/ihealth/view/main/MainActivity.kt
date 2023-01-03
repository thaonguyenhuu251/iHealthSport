package com.htnguyen.ihealth.view.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.databinding.ActivityMainBinding
import com.htnguyen.ihealth.model.User
import com.htnguyen.ihealth.util.Constant
import com.htnguyen.ihealth.util.PreferencesUtil
import com.htnguyen.ihealth.view.chat.ChatFragment
import com.htnguyen.ihealth.view.home.HomeFragment
import com.htnguyen.ihealth.view.login.LoginActivity
import com.htnguyen.ihealth.view.profile.ProfileFragment
import com.htnguyen.ihealth.view.profile.ProfileViewModel
import com.htnguyen.ihealth.view.search.SearchFragment
import com.htnguyen.ihealth.view.social.SocialFragment


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
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
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        auth = FirebaseAuth.getInstance()
        setViewPager()
        setTabLayout()
        openDrawerSetting()
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
        binding.tabLayoutMain.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.position.let { binding.viewPagerMain.setCurrentItem(it, false) }

                if (tab.position == 4) {
                    binding.imgSetting.visibility = View.VISIBLE

                    binding.imgSetting.setOnClickListener {
                        binding.drawerMain.openDrawer(GravityCompat.END)
                    }
                } else {
                    binding.imgSetting.visibility = View.GONE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun openDrawerSetting() {
        binding.drawerMain.addDrawerListener(object :
            DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                // Whatever you want
            }

            override fun onDrawerOpened(drawerView: View) {
                binding.navMain.findViewById<TextView>(R.id.txtLogout)
                    .setOnClickListener {
                        auth.signOut()
                        startActivity(
                            Intent(
                                this@MainActivity,
                                LoginActivity::class.java
                            )
                        )

                        finish()

                        PreferencesUtil.idUser = null
                        PreferencesUtil.passWord = null
                        PreferencesUtil.userName = null
                        PreferencesUtil.userBirthDay = 978307200000L
                        PreferencesUtil.userGender = false
                        PreferencesUtil.userHeight = 180f
                        PreferencesUtil.userWeight = 45f
                    }
            }

            override fun onDrawerClosed(drawerView: View) {
                // Whatever you want
            }

            override fun onDrawerStateChanged(newState: Int) {
                // Whatever you want
            }
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