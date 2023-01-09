package com.htnguyen.ihealth.view.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.view.WindowManager
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.ExpandableListView.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.adapter.CustomExpandableListAdapter
import com.htnguyen.ihealth.databinding.ActivityMainBinding
import com.htnguyen.ihealth.inter.UpdateUiCallBack
import com.htnguyen.ihealth.inter.stepsCallback
import com.htnguyen.ihealth.model.User
import com.htnguyen.ihealth.service.StepDetectorService
import com.htnguyen.ihealth.service.StepService
import com.htnguyen.ihealth.support.ExpandableListDataPump.data
import com.htnguyen.ihealth.util.FirebaseUtils.db
import com.htnguyen.ihealth.util.PreferencesUtil
import com.htnguyen.ihealth.view.chat.ChatFragment
import com.htnguyen.ihealth.view.component.LoadingDialog2
import com.htnguyen.ihealth.view.home.HomeFragment
import com.htnguyen.ihealth.view.login.LoginActivity
import com.htnguyen.ihealth.view.profile.ProfileFragment
import com.htnguyen.ihealth.view.search.SearchFragment
import com.htnguyen.ihealth.view.social.SocialFragment
import android.view.ViewGroup





class MainActivity : AppCompatActivity(), stepsCallback {
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

    private var loadingDialog: LoadingDialog2? = null

    lateinit var expandableListAdapter: ExpandableListAdapter
    lateinit var expandableListTitle: List<String>
    lateinit var expandableListDetail: HashMap<String, List<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        auth = FirebaseAuth.getInstance()
        loadingDialog = LoadingDialog2(this)
        initData()
        setViewPager()
        setTabLayout()
        openDrawerSetting()
        getProfileUser()

        val intent = Intent(this, StepDetectorService::class.java)
        startService(intent)

        StepDetectorService.subscribe.register(this)
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
        /*val item_size = 50 // 50px
        val sub_item_size = 40 // 40px
        expandableListDetail = data
        expandableListTitle = ArrayList(expandableListDetail.keys)
        expandableListAdapter = CustomExpandableListAdapter(
            this, expandableListTitle,
            expandableListDetail
        )
        binding.navMain.findViewById<ExpandableListView>(R.id.expandableListView).setAdapter(expandableListAdapter)
        binding.navMain.findViewById<ExpandableListView>(R.id.expandableListView).setOnGroupExpandListener { groupPosition ->

        }

        binding.navMain.findViewById<ExpandableListView>(R.id.expandableListView).setOnGroupCollapseListener { groupPosition ->

        }

        binding.navMain.findViewById<ExpandableListView>(R.id.expandableListView).setOnGroupClickListener { expandableListView, view, i, l ->
            setListViewHeight(expandableListView, i)
            false
        }

        binding.navMain.findViewById<ExpandableListView>(R.id.expandableListView).setOnChildClickListener { parent, v, groupPosition, childPosition, id ->

            false
        }*/

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


    private var mIsBind = false
    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, service: IBinder) {
            val stepService: StepService = (service as StepService.StepBinder).service
            showStepCount(
                PreferencesUtil.stepNumber,
                stepService.stepCount
            )
            stepService.registerCallback(object : UpdateUiCallBack {
                override fun updateUi(stepCount: Int) {
                    showStepCount(PreferencesUtil.stepNumber, stepCount)
                }
            })
        }

        override fun onServiceDisconnected(componentName: ComponentName) {}
    }

    fun showStepCount(totalStepNum: Int, currentCounts: Int) {
        var currentCounts = currentCounts
        if (currentCounts < totalStepNum) {
            currentCounts = totalStepNum
        }
        //viewModel.step.value = currentCounts
    }

    private fun initData() {
        showStepCount(PreferencesUtil.stepNumber, 0)
        setupService()
    }

    private fun setupService() {
        val intent = Intent(this@MainActivity, StepService::class.java)
        mIsBind = bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)
        startService(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mIsBind) {
            unbindService(mServiceConnection)
        }
    }

    override fun subscribeSteps(steps: Int) {
        //TV_STEPS.setText(steps.toString())
        //TV_CALORIES.setText(GeneralHelper.getCalories(steps))
    }

    private fun setListViewHeight(listView: ExpandableListView, groupPosition: Int) {
        val listAdapter = listView.expandableListAdapter as ExpandableListAdapter
        var totalHeight = 0
        val desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.EXACTLY)
        for (i in 0 until listAdapter.groupCount) {
            val groupItem = listAdapter.getGroupView(i, false, null, listView)
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            totalHeight += groupItem.measuredHeight

            if (listView.isGroupExpanded(i) && i != groupPosition
                || !listView.isGroupExpanded(i) && i == groupPosition
            ) {
                for (j in 0 until listAdapter.getChildrenCount(i)) {
                    val listItem: View = listAdapter.getChildView(
                        i, j, false, null,
                        listView
                    )
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
                    totalHeight += listItem.measuredHeight
                }
            }
        }
        val params = listView.layoutParams
        var height = (totalHeight
                + listView.dividerHeight * (listAdapter.groupCount - 1))
        if (height < 10) height = 200
        params.height = height
        listView.layoutParams = params
        listView.requestLayout()
    }
}