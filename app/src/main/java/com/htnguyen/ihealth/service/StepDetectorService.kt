package com.htnguyen.ihealth.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.widget.Toast
import com.htnguyen.ihealth.helper.GeneralHelper
import com.htnguyen.ihealth.helper.PrefsHelper
import com.htnguyen.ihealth.util.CommonUtils
import kotlin.math.roundToInt

class StepDetectorService : Service(), SensorEventListener {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val sensorManager: SensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val countSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if(countSensor != null){
            Toast.makeText(this, "Step Detecting Start", Toast.LENGTH_SHORT).show()
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_NORMAL)
            GeneralHelper.updateNotification(this, this, PrefsHelper.getInt("FSteps"))

        }else{
            Toast.makeText(this, "Sensor Not Detected", Toast.LENGTH_SHORT).show()
        }

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (PrefsHelper.getString("TodayDate") != GeneralHelper.getToadyDate()) {
            PrefsHelper.putInt("Steps", event?.values?.get(0)?.roundToInt() ?: 0)
            PrefsHelper.putString("TodayDate", GeneralHelper.getToadyDate())
        } else {
            val storeSteps = PrefsHelper.getInt("Steps")
            val sensorSteps = event?.values?.get(0)?.roundToInt() ?: 0
            val finalSteps = sensorSteps - storeSteps
            if (finalSteps > 0) {
                PrefsHelper.putInt("FSteps", finalSteps)
                CommonUtils.updateActivityDaily(finalSteps)
                GeneralHelper.updateNotification(this, this, finalSteps)
            }
        }

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
}