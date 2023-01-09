package com.htnguyen.ihealth.view.home

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.fragment.app.viewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.base.BaseFragment
import com.htnguyen.ihealth.databinding.FragmentHomeBinding
import com.htnguyen.ihealth.model.ActivityDaily
import com.htnguyen.ihealth.model.EatAndDrink
import com.htnguyen.ihealth.support.Calendar
import com.htnguyen.ihealth.support.SimpleDateFormat
import com.htnguyen.ihealth.support.dateInMillis
import com.htnguyen.ihealth.support.gps.GpsMap
import com.htnguyen.ihealth.support.hour
import com.htnguyen.ihealth.util.FirebaseUtils
import com.htnguyen.ihealth.util.PreferencesUtil
import kotlin.math.roundToInt

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(), SensorEventListener {

    override val layout: Int
        get() = R.layout.fragment_home

    private val viewModel by viewModels<HomeViewModel>()

    private var sensorManager: SensorManager? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        viewModel.todayString.value = SimpleDateFormat(getString(R.string.common_format_date_standard))
            .format(viewModel.today.value)
            .toString()

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        getEatAndDrink()
        optionDailyActivities()
        optionStep()
        optionHeartBeat()
        optionEatAndDrink()
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
        binding.layoutStep.processBarStep.progress = viewModel.step.value.toString().toInt()
        binding.layoutStep.processBarStep.max = viewModel.followStep.value.toString().toInt()
    }

    private fun updateActivityDaily() {
        binding.layoutStep.textView4.text = Html.fromHtml("<big><big><big><b>" + viewModel.step.value + "</b></big></big></big>/" + viewModel.followStep.value , Html.FROM_HTML_MODE_COMPACT)
        val activityDaily = ActivityDaily(
            step = viewModel.step.value,
            followStep = 6000,
            timeActive = 0f,
            calo = 0f
        )

        binding.layoutDailyActivities.circularProgressBar3.apply {
            setProgressWithAnimation((viewModel.step.value ?: 0).toFloat(), 1000)
            progressMax = (viewModel.followStep.value ?: 0).toFloat()

        }

        FirebaseUtils.activityDaily
            .child("record_history")
            .child(PreferencesUtil.idPrivate.toString())
            .child("activity_daily")
            .child("date" + Calendar().dateInMillis.toString())
            .setValue(activityDaily)
            .addOnSuccessListener {

            }.addOnFailureListener { e ->

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
        binding.layoutHomeWater.textView4.text = Html.fromHtml("<big><b>" + viewModel.water.value + "</b></big>/" + viewModel.followWater.value + " Glass", Html.FROM_HTML_MODE_COMPACT)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                for (postSnapshot in dataSnapshot.children) {
                    val eatAndDrink = postSnapshot.getValue<EatAndDrink>()
                    if (eatAndDrink != null) {
                        viewModel.water.value = eatAndDrink.water

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


    override fun onResume() {
        super.onResume()
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null) {

        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }


    override fun onSensorChanged(event: SensorEvent?) {
        val totalStepSinceReboot: Int = event?.values?.get(0)?.roundToInt() ?: 0
        viewModel.step.value = totalStepSinceReboot
        updateActivityDaily()
        optionStep()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // We do not have to write anything in this function for this app
    }

    private fun optionHeartBeat() {
        binding.layoutHomeHeartbeat.txtMeasure.setOnClickListener {
            requireActivity().startActivity(Intent(requireActivity(), MeasureHeartBeatActivity::class.java))
        }
    }



}