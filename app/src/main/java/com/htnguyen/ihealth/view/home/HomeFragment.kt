package com.htnguyen.ihealth.view.home

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.ViewPager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.adapter.NewsAdapter
import com.htnguyen.ihealth.base.BaseFragment
import com.htnguyen.ihealth.databinding.FragmentHomeBinding
import com.htnguyen.ihealth.helper.PrefsHelper
import com.htnguyen.ihealth.model.ActivityDaily
import com.htnguyen.ihealth.model.EatAndDrink
import com.htnguyen.ihealth.model.HealthDaily
import com.htnguyen.ihealth.model.ModelClass
import com.htnguyen.ihealth.support.Calendar
import com.htnguyen.ihealth.support.SimpleDateFormat
import com.htnguyen.ihealth.support.dateInMillis
import com.htnguyen.ihealth.support.gps.GpsMap
import com.htnguyen.ihealth.util.*
import com.htnguyen.ihealth.util.Database
import com.htnguyen.ihealth.util.Database.Entry
import com.htnguyen.ihealth.util.Util
import com.htnguyen.ihealth.view.IHealthApplication
import com.htnguyen.ihealth.view.dialog.FollowerStepDialog
import com.htnguyen.ihealth.view.dialog.FollowerWaterDialog
import io.reactivex.rxjava3.disposables.Disposable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(), SensorEventListener {

    override val layout: Int
        get() = R.layout.fragment_home

    private val viewModel by viewModels<HomeViewModel>()

    private var sensorManager: SensorManager? = null
    var modelClassArrayList = ArrayList<ModelClass>()
    var currentPage = 0
    val DELAY_MS: Long = 3000
    val PERIOD_MS: Long = 10000
    private val entries = ArrayList<Entry>()

    private var mSelectedWeek: Int = 0
    private var disposable: Disposable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        viewModel.todayString.value = SimpleDateFormat("dd/MM/yyyy")
            .format(viewModel.today.value)
            .toString()

        binding.imgNotification.setOnClickListener {
            Event.eventOpenNotification()
        }

        disposable = IHealthApplication.eventBus.subscribe {
            it[Event.EVENT_CHANGE_FOLLOW_STEP]?.let {
                viewModel.followStep.value = it.toString().toInt()
                updateActivityDaily()
            }
            it[Event.EVENT_CHANGE_FOLLOW_WATER]?.let {
                viewModel.followWater.value = it.toString().toInt()
                updateEatAndDrink()
            }

        }

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        getEatAndDrink()
        getHealthDaily()
        getStep()
        modelClassArrayList.add(ModelClass("", "", "","", "https://cdn.brvn.vn/topics/1280px/2021/318721_318721cover_1625227070.jpg", "", ""))
        modelClassArrayList.add(ModelClass("", "", "","", "https://storage.googleapis.com/leep_app_website/2021/01/Tap-the-duc-theo-nhom1.png", "", ""))
        modelClassArrayList.add(ModelClass("", "", "","", "https://suckhoedoisong.qltns.mediacdn.vn/Images/duylinh/2016/09/08/1_.jpg", "", ""))
        modelClassArrayList.add(ModelClass("", "", "","", "https://file1.hutech.edu.vn/file/editor/homepage1/779253-nganh-quan-ly-the-duc-the-thao-hutech3.jpg", "", ""))
        modelClassArrayList.add(ModelClass("", "", "","", "https://www.cleanipedia.com/images/5iwkm8ckyw6v/9d6d615751ed57a974c1956b1a3aa90d/ccf894cfa45de82f073ae97161933470/aHR0cHNfX193d3cuY2xlYW5pcGVkaWEuY29tX2NvbnRlbnRfZGFtX3VuaWxldmVyX2xpcHRvbl9pbnRlcm5hdGlvbmFsX3NhdWRpX2FyYWJpYV9vbmxpbmVfY29tbXNfXzFfbGlwdG9uXy1fd2VsbGJlaW5nXy1fYmFubi5qcGc/990w-660h/5-sai-lầm-trong-việc-giữ-vệ-sinh-sau-khi-tập-luyện-mà-bạn-hay-mắc-phải.jpg", "", ""))
        modelClassArrayList.add(ModelClass("", "", "","", "https://acc.vn/wp-content/uploads/2023/01/bai-tap-the-duc-cho-phu-nu-sau-sinh.png", "", ""))
        setDataForWeek(Util.calendar)
        setPageView()
        optionDailyActivities()
        optionStep()
        optionHeartBeat()
        optionEatAndDrink()
        optionFellingToday()
        optionWeight()
        optionOxyInBlood()
        optionSettingStep()
        optionSettingWater()

    }

    private fun setPageView() {
        val viewPager: ViewPager = binding.layoutHomeDailyPlan.viewPagerMain
        val adapter = NewsAdapter(context, modelClassArrayList)
        viewPager.adapter = adapter
        val handler = Handler()
        val Update = Runnable {
            if (currentPage == modelClassArrayList.size - 1) {
                currentPage = 0
            }
            viewPager.setCurrentItem(currentPage++, true)
        }
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, DELAY_MS, PERIOD_MS)
    }

    private fun optionWeight() {
        binding.layoutHomeWeight.textView4.text = Html.fromHtml("<big><b>" + viewModel.progressWeight.value + "</b></big> kG", Html.FROM_HTML_MODE_COMPACT)
    }

    private fun optionSettingStep() {
        binding.layoutStep.root.setOnClickListener {
            FollowerStepDialog().newInstance(viewModel.followStep.value!!).show(requireActivity(), "BKAV")
        }
    }

    private fun optionSettingWater() {
        binding.layoutHomeWater.root.setOnClickListener {
            FollowerWaterDialog().newInstance(viewModel.followWater.value!!).show(requireActivity(), "BKAV")
        }
    }

    private fun optionEatAndDrink() {
        binding.layoutHomeWater.txtAdd.setOnClickListener {
            viewModel.water.value = viewModel.water.value?.plus(1)
            updateEatAndDrink()
        }

        binding.layoutHomeWater.txtMinus.setOnClickListener {
            viewModel.water.value = viewModel.water.value?.plus(-1)
            updateEatAndDrink()
        }

    }

    private fun optionDailyActivities() {

        binding.layoutDailyActivities.content.setOnClickListener {
            requireActivity().startActivity(Intent(requireActivity(), GpsMap::class.java))
        }

        binding.layoutDailyActivities.circularProgressBar.apply {
            setProgressWithAnimation(CommonUtils.getCaloriesInt(viewModel.step.value ?: 0).toFloat(), 1000)
            progressMax = CommonUtils.getCaloriesInt(viewModel.followStep.value ?: 0).toFloat()
        }

        binding.layoutDailyActivities.circularProgressBar.onProgressChangeListener = { progress ->

        }

        binding.layoutDailyActivities.circularProgressBar.onIndeterminateModeChangeListener = { isEnable ->

        }

        binding.layoutDailyActivities.circularProgressBar2.apply {
            setProgressWithAnimation(CommonUtils.getDistanceInt(viewModel.step.value ?: 0).toFloat(), 1000)
            progressMax = CommonUtils.getDistanceInt(viewModel.followStep.value ?: 0).toFloat()

        }

    }

    private fun optionStep() {
        binding.layoutStep.textView4.text = Html.fromHtml("<big><big><big><b>" + viewModel.step.value + "</b></big></big></big>/" + viewModel.followStep.value , Html.FROM_HTML_MODE_COMPACT)
        binding.layoutStep.processBarStep.progress = viewModel.step.value.toString().toInt()
        binding.layoutStep.processBarStep.max = viewModel.followStep.value.toString().toInt()
        binding.layoutDailyActivities.circularProgressBar3.apply {
            setProgressWithAnimation((viewModel.step.value ?: 0).toFloat(), 1000)
            progressMax = (viewModel.followStep.value ?: 0).toFloat()

        }

    }

    private fun optionHeartBeat() {
        binding.layoutHomeHeartbeat.textView4.text = Html.fromHtml("<big><b>" + viewModel.heartBeat.value + "</b></big> bpm", Html.FROM_HTML_MODE_COMPACT)
        binding.layoutHomeHeartbeat.txtMeasure.setOnClickListener {
            requireActivity().startActivity(Intent(requireActivity(), MeasureHeartBeatActivity::class.java))
        }
    }

    private fun optionOxyInBlood() {
        binding.layoutHomeBlood.content.setOnClickListener {
            requireActivity().startActivity(Intent(requireActivity(), MeasureOxyBloodActivity::class.java))
        }
    }

    private fun optionFellingToday() {
        binding.layoutHomeYouFell.rgFelling.setOnCheckedChangeListener { group, checkedId ->
            val checkedRadioButton = group.findViewById<View>(checkedId) as RadioButton
            val isChecked = checkedRadioButton.isChecked
            if (isChecked) {
                when (checkedId) {
                    R.id.rdGood -> {
                        CommonUtils.updateFelling(1)
                        viewModel.fellingToday.value = 1
                        binding.layoutHomeYouFell.rdGood.isChecked = true
                    }
                    R.id.rdDis -> {
                        CommonUtils.updateFelling(0)
                        viewModel.fellingToday.value = 0
                        binding.layoutHomeYouFell.rdDis.isChecked = true
                    }
                    else -> {
                        CommonUtils.updateFelling(-1)
                        viewModel.fellingToday.value = -1
                        binding.layoutHomeYouFell.rdBad.isChecked = true
                    }
                }
            }
        }


    }

    private fun setFellingToday() {
        when (viewModel.fellingToday.value) {
            1 -> binding.layoutHomeYouFell.rdGood.isChecked = true
            0 -> binding.layoutHomeYouFell.rdDis.isChecked = true
            else -> binding.layoutHomeYouFell.rdBad.isChecked = true
        }
    }

    fun updateActivityDaily() {
        binding.layoutStep.textView4.text = Html.fromHtml("<big><big><big><b>" + viewModel.step.value + "</b></big></big></big>/" + viewModel.followStep.value , Html.FROM_HTML_MODE_COMPACT)
        val activityDaily = ActivityDaily(
            step = viewModel.step.value,
            followStep = viewModel.followStep.value,
            timeActive = CommonUtils.getDistanceInt(viewModel.step.value!!).toFloat(),
            calo = CommonUtils.getCaloriesInt(viewModel.step.value!!).toFloat()
        )

        FirebaseUtils.activityDaily
            .child("record_history")
            .child(PreferencesUtil.idPrivate.toString())
            .child("activity_daily")
            .child("date" + Calendar().dateInMillis.toString())
            .setValue(activityDaily)
            .addOnSuccessListener {

            }.addOnFailureListener {

            }
    }

    private fun updateEatAndDrink() {
        binding.layoutHomeWater.txtMinus.isClickable = viewModel.water.value!! > 0
        binding.layoutHomeWater.textView4.text = Html.fromHtml("<big><b>" + viewModel.water.value + "</b></big>/" + viewModel.followWater.value + " Glass", Html.FROM_HTML_MODE_COMPACT)
        val eatAndDrink = EatAndDrink(
            kcal = viewModel.kcal.value,
            followKcal = viewModel.followKcal.value,
            water = viewModel.water.value,
            followWater = viewModel.followWater.value,
            waterMil = viewModel.waterMil.value
        )
        FirebaseUtils.activityDaily
            .child("record_history")
            .child(PreferencesUtil.idPrivate.toString())
            .child("eat_and_drink")
            .child("date" + Calendar().dateInMillis.toString())
            .setValue(eatAndDrink)
            .addOnSuccessListener {

            }.addOnFailureListener { e ->

            }
    }


    private fun getStep() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val activityDaily = postSnapshot.getValue<ActivityDaily>()
                    if (activityDaily != null) {
                        viewModel.followStep.value = activityDaily.followStep
                        binding.layoutHomeWater.textView4.text = Html.fromHtml("<big><b>" + viewModel.water.value + "</b></big>/" + viewModel.followWater.value + " Glass", Html.FROM_HTML_MODE_COMPACT)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        FirebaseUtils.activityDaily
            .child("record_history")
            .child(PreferencesUtil.idPrivate.toString())
            .child("activity_daily").addValueEventListener(postListener)
    }

    private fun getStepDay(day: Long) {
        FirebaseUtils.activityDaily
            .child("record_history")
            .child(PreferencesUtil.idPrivate.toString())
            .child("activity_daily")
            .child("date$day")
            .get().addOnSuccessListener { data ->
                val activityDaily = data.getValue<ActivityDaily>()
                if (activityDaily != null) {
                    binding.layoutChart.chart.setDiagramEntry(Entry(day, activityDaily.step ?: 0))
                    entries.add(0, Entry(day, activityDaily.step ?: 0))
                }
            }.addOnCompleteListener {
                binding.layoutChart.chart.update()
            }
    }

    private fun getEatAndDrink() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                for (postSnapshot in dataSnapshot.children) {
                    val eatAndDrink = postSnapshot.getValue<EatAndDrink>()
                    if (eatAndDrink != null) {
                        viewModel.water.value = eatAndDrink.water
                        binding.layoutHomeWater.textView4.text = Html.fromHtml("<big><b>" + viewModel.water.value + "</b></big>/" + viewModel.followWater.value + " Glass", Html.FROM_HTML_MODE_COMPACT)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        FirebaseUtils.activityDaily
            .child("record_history")
            .child(PreferencesUtil.idPrivate.toString())
            .child("eat_and_drink").addValueEventListener(postListener)
    }

    private fun getHealthDaily() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val healthDaily = postSnapshot.getValue<HealthDaily>()
                    if (healthDaily != null) {
                        viewModel.heartBeat.value = healthDaily.heartBeat
                        viewModel.fellingToday.value = healthDaily.fellingToday
                        binding.layoutHomeHeartbeat.textView4.text = Html.fromHtml("<big><b>" + viewModel.heartBeat.value + "</b></big> bpm", Html.FROM_HTML_MODE_COMPACT)
                    }
                }

                setFellingToday()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        FirebaseUtils.activityDaily
            .child("record_history")
            .child(PreferencesUtil.idPrivate.toString())
            .child("health_daily").addValueEventListener(postListener)
    }

    override fun onResume() {
        super.onResume()
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null) {

        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    private fun setDataForWeek(selected: Calendar) {
        mSelectedWeek = selected.get(Calendar.WEEK_OF_YEAR)
        val min = Calendar.getInstance()
        min.timeInMillis = selected.timeInMillis
        min.set(Calendar.DAY_OF_WEEK, Calendar.getInstance().firstDayOfWeek)
        val max = Calendar.getInstance()
        max.timeInMillis = min.timeInMillis
        max.add(Calendar.DAY_OF_YEAR, 6)
        binding.layoutChart.chart.clearDiagram()

        for (i in min.timeInMillis .. max.timeInMillis step 86400000) {
            getStepDay(i)
        }

    }

    override fun onSensorChanged(event: SensorEvent?) {
        val totalStepSinceReboot: Int = event?.values?.get(0)?.roundToInt() ?: 0
        viewModel.step.value = totalStepSinceReboot - PrefsHelper.getInt("Steps")
        viewModel.calories.value = CommonUtils.getCaloriesInt(totalStepSinceReboot - PrefsHelper.getInt("Steps"))
        viewModel.meter.value = CommonUtils.getDistanceInt(totalStepSinceReboot - PrefsHelper.getInt("Steps"))
        optionStep()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}