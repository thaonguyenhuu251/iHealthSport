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
import android.view.View
import android.widget.RadioButton
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.ViewPager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.adapter.NewsAdapter
import com.htnguyen.ihealth.adapter.TextItemAdapter
import com.htnguyen.ihealth.base.BaseFragment
import com.htnguyen.ihealth.databinding.FragmentHomeBinding
import com.htnguyen.ihealth.helper.PrefsHelper
import com.htnguyen.ihealth.model.EatAndDrink
import com.htnguyen.ihealth.model.HealthDaily
import com.htnguyen.ihealth.model.ModelClass
import com.htnguyen.ihealth.support.Calendar
import com.htnguyen.ihealth.support.SimpleDateFormat
import com.htnguyen.ihealth.support.dateInMillis
import com.htnguyen.ihealth.support.gps.GpsMap
import com.htnguyen.ihealth.util.*
import com.htnguyen.ihealth.util.Database
import com.htnguyen.ihealth.util.Util
import java.util.*
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

    private var mSelectedWeek: Int = 0
    private var mCurrentSteps: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        viewModel.todayString.value = SimpleDateFormat(getString(R.string.common_format_date_standard))
            .format(viewModel.today.value)
            .toString()

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        getEatAndDrink()
        getHealthDaily()

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
            progress = 105f
            setProgressWithAnimation(progress, 1000)
            progressMax = 200f
        }

        binding.layoutDailyActivities.circularProgressBar.onProgressChangeListener = { progress ->

        }

        binding.layoutDailyActivities.circularProgressBar.onIndeterminateModeChangeListener = { isEnable ->

        }

        binding.layoutDailyActivities.circularProgressBar2.apply {
            progress = 65f
            setProgressWithAnimation(65f, 1000) // =1s
            progressMax = 200f

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
                // Get Post object and use the values to update the UI
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
                // Getting Post failed, log a message
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

        // Jump to the first day of the week
        min.set(Calendar.DAY_OF_WEEK, Calendar.getInstance().firstDayOfWeek)

        val max = Calendar.getInstance()
        max.timeInMillis = min.timeInMillis

        // Jump to the last day of the week
        max.add(Calendar.DAY_OF_YEAR, 6)

        binding.layoutChart.chart.clearDiagram()

        // Get the records of the selected week between the min and max timestamps
        val entries = Database.getInstance(requireContext()).getEntries(min.timeInMillis, max.timeInMillis)

        for (entry in entries) {
            binding.layoutChart.chart.setDiagramEntry(entry)

            val cal = Calendar.getInstance()
            cal.timeInMillis = entry.timestamp

            // Update the description text with the selected date
        }

        // If selected week is the current week, update the diagram with today's steps
        if (mSelectedWeek == Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)) {
            binding.layoutChart.chart.setCurrentSteps(mCurrentSteps)
        }
        binding.layoutChart.chart.update()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val totalStepSinceReboot: Int = event?.values?.get(0)?.roundToInt() ?: 0
        viewModel.step.value = totalStepSinceReboot - PrefsHelper.getInt("Steps")
        viewModel.calories.value = CommonUtils.getCaloriesInt(totalStepSinceReboot - PrefsHelper.getInt("Steps"))
        optionStep()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // We do not have to write anything in this function for this app
    }
}