package com.htnguyen.ihealth.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.CompoundButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.htnguyen.ihealth.BR
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.base.BaseActivity
import com.htnguyen.ihealth.databinding.ActivityMainBinding
import com.htnguyen.ihealth.model.User
import com.htnguyen.ihealth.service.StepDetectorService
import com.htnguyen.ihealth.util.Event
import com.htnguyen.ihealth.util.FirebaseUtils
import com.htnguyen.ihealth.util.FirebaseUtils.db
import com.htnguyen.ihealth.util.PreferencesUtil
import com.htnguyen.ihealth.view.IHealthApplication
import com.htnguyen.ihealth.view.chat.ChatFragment
import com.htnguyen.ihealth.view.component.LoadingDialog2
import com.htnguyen.ihealth.view.dialog.CalendarDialog
import com.htnguyen.ihealth.view.home.HomeFragment
import com.htnguyen.ihealth.view.login.LoginActivity
import com.htnguyen.ihealth.view.profile.ProfileFragment
import com.htnguyen.ihealth.view.search.SearchFragment
import com.htnguyen.ihealth.view.social.SocialFragment
import io.reactivex.rxjava3.disposables.Disposable
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val layout: Int get() = R.layout.activity_main
    override val viewModel: MainViewModel by viewModel()
    private var disposable: Disposable? = null

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    private val imageResId = intArrayOf(
        R.drawable.ic_main_home,
        R.drawable.ic_home_group,
        R.drawable.ic_home_chat,
        R.drawable.ic_home_search,
        R.drawable.ic_home_profile
    )

    private val stringResId = intArrayOf(
        R.string.main_home,
        R.string.main_social,
        R.string.main_chat,
        R.string.main_search,
        R.string.main_profile
    )

    private var loadingDialog: LoadingDialog2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !PreferencesUtil.isNotification) {
            checkNotification()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkNotification()
            dispatchTakePictureIntent()
            checkRecognition()
            checkPermissionCamera()
        }


        loadingDialog = LoadingDialog2(this)
        setViewPager()
        setTabLayout()
        openDrawerSetting()
        getProfileUser()

        val intent = Intent(this, StepDetectorService::class.java)
        startService(intent)

        binding.drawerMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        disposable = IHealthApplication.eventBus.subscribe {
            it[Event.EVENT_OPEN_NOTIFICATION]?.let {
                binding.drawerMain.openDrawer(GravityCompat.START)
            }

            it[Event.EVENT_OPEN_SETTING]?.let {
                binding.drawerMain.openDrawer(GravityCompat.END)
            }

        }


    }

    private fun setTabLayout() {
        for (index in imageResId.indices) {
            binding.tabLayoutMain.addTab(
                binding.tabLayoutMain.newTab()
                    .setIcon(imageResId[index])
                    .setText(stringResId[index])
            )
        }

        binding.tabLayoutMain.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.position.let { binding.viewPagerMain.setCurrentItem(it, false) }
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

                binding.navMain.findViewById<SwitchCompat>(R.id.swDarkMode).setOnCheckedChangeListener { buttonView, isChecked ->
                    PreferencesUtil.isDarkMode = isChecked
                    if (!PreferencesUtil.isDarkMode) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                }

                binding.navMain.findViewById<TextView>(R.id.txtLogout)
                    .setOnClickListener {
                        FirebaseUtils.firebaseAuth.signOut()
                        startActivity(
                            Intent(
                                this@MainActivity,
                                LoginActivity::class.java
                            )
                        )
                        finish()
                        PreferencesUtil.idUser = null
                        PreferencesUtil.passWord = null
                        PreferencesUtil.idPrivate = null
                        PreferencesUtil.userName = null
                        PreferencesUtil.userBirthDay = 0L
                        PreferencesUtil.userGender = false
                        PreferencesUtil.userHeight = 0f
                        PreferencesUtil.userWeight = 0f
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

    private fun getProfileUser() {
        loadingDialog?.showDialog()
        db.collection("user").document(PreferencesUtil.idUser!!).get()
            .addOnSuccessListener { result ->
                val user = result.toObject(User::class.java)
                if (user != null) {
                    PreferencesUtil.userName = user.name
                    PreferencesUtil.userBirthDay = user.birthDay
                    PreferencesUtil.userGender = user.gender ?: false
                    PreferencesUtil.userHeight = user.height
                    PreferencesUtil.userWeight = user.weight
                    PreferencesUtil.userPhotoUrl = user.photoUrl
                    PreferencesUtil.idPrivate = user.idUser
                    loadingDialog?.dismissDialog()
                }
            }
            .addOnFailureListener { exception ->
                loadingDialog?.dismissDialog()
            }
    }
}