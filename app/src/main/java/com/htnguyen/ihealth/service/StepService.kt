package com.htnguyen.ihealth.service

import android.app.Notification
import com.htnguyen.ihealth.util.PreferencesUtil.stepNumber
import android.hardware.SensorEventListener
import com.htnguyen.ihealth.inter.UpdateUiCallBack
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import android.content.BroadcastReceiver
import com.htnguyen.ihealth.service.StepService.StepBinder
import android.hardware.SensorManager
import android.content.Intent
import android.os.IBinder
import android.os.Build
import com.htnguyen.ihealth.service.StepService
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import com.htnguyen.ihealth.util.PreferencesUtil
import android.content.IntentFilter
import android.hardware.Sensor
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.view.main.MainActivity
import android.hardware.SensorEvent
import android.os.Binder
import android.util.Log

class StepService : Service(), SensorEventListener {
    private var mCallback: UpdateUiCallBack? = null
    private var mNotificationManager: NotificationManager? = null
    private var mBuilder: NotificationCompat.Builder? = null
    private var mBroadcastReceiver: BroadcastReceiver? = null
    private val mStepBinder = StepBinder()
    private var mSensorManager: SensorManager? = null
    var stepCount = 0
        private set
    private val mNotifyIdStep = 101
    private var mHasStepCount = 0
    private var mPreviousStepCount = 0
    private var mHasRecord = false
    override fun onCreate() {
        super.onCreate()
        initNotification()
        initTodayData()
        initBroadcastReceiver()
        Thread { startStepDetector() }.start()
    }

    override fun onBind(intent: Intent): IBinder? {
        return mStepBinder
    }

    fun registerCallback(paramICallback: UpdateUiCallBack?) {
        mCallback = paramICallback
    }

    private fun startStepDetector() {
        if (mSensorManager != null) {
            mSensorManager = null
        }
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val VERSION_CODES = Build.VERSION.SDK_INT
        if (VERSION_CODES >= 19) {
            addCountStepListener()
        } else {
            addBasePedometerListener()
        }
    }

    private fun addCountStepListener() {
        val countSensor = mSensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        val detectorSensor = mSensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        if (countSensor != null) {
            mStepSensorType = Sensor.TYPE_STEP_COUNTER
            Log.v(TAG, "Sensor.TYPE_STEP_COUNTER")
            mSensorManager!!.registerListener(this@StepService, countSensor, SensorManager.SENSOR_DELAY_NORMAL)
        } else if (detectorSensor != null) {
            mStepSensorType = Sensor.TYPE_STEP_DETECTOR
            Log.v(TAG, "Sensor.TYPE_STEP_DETECTOR")
            mSensorManager!!.registerListener(
                this@StepService, detectorSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        } else {
            Log.v(TAG, "Count sensor not available!")
            addBasePedometerListener()
        }
    }

    private fun addBasePedometerListener() {
        // TODO: 23/08/2017
    }

    fun getDefaultIntent(flags: Int): PendingIntent {
        return PendingIntent.getActivity(this, 1, Intent(), flags)
    }

    private fun initTodayData() {
        stepCount = stepNumber
        updateNotification()
    }

    private fun initBroadcastReceiver() {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        filter.addAction(Intent.ACTION_SHUTDOWN)
        filter.addAction(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        filter.addAction(Intent.ACTION_DATE_CHANGED)
        filter.addAction(Intent.ACTION_TIME_CHANGED)
        filter.addAction(Intent.ACTION_TIME_TICK)
        mBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val action = intent.action
                when (action) {
                    Intent.ACTION_SCREEN_ON -> Log.i(TAG, "screen_on")
                    Intent.ACTION_SCREEN_OFF -> Log.i(TAG, "screen_off")
                    Intent.ACTION_USER_PRESENT -> Log.i(TAG, "screen unlock")
                    Intent.ACTION_CLOSE_SYSTEM_DIALOGS -> {
                        Log.i(TAG, "receive ACTION_CLOSE_SYSTEM_DIALOGS")
                        saveData()
                    }
                    Intent.ACTION_SHUTDOWN -> {
                        Log.i(TAG, "receive ACTION_SHUTDOWN")
                        saveData()
                    }
                    Intent.ACTION_DATE_CHANGED -> {
                        Log.i(TAG, "receive ACTION_DATE_CHANGED")
                        saveData()
                    }
                    Intent.ACTION_TIME_CHANGED -> {
                        Log.i(TAG, "receive ACTION_TIME_CHANGED")
                        saveData()
                    }
                    Intent.ACTION_TIME_TICK -> {
                        Log.i(TAG, "receive ACTION_TIME_TICK")
                        saveData()
                    }
                }
            }
        }
        registerReceiver(mBroadcastReceiver, filter)
    }

    private fun initNotification() {
        mBuilder = NotificationCompat.Builder(this)
        mBuilder!!.setContentTitle(resources.getString(R.string.app_name))
            .setContentText("The number of steps today: " + stepCount + " step")
            .setContentIntent(getDefaultIntent(Notification.FLAG_ONGOING_EVENT))
            .setWhen(System.currentTimeMillis())
            .setPriority(Notification.PRIORITY_DEFAULT)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
        val notification = mBuilder!!.build()
        mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        //startForeground(mNotifyIdStep, notification)
    }

    private fun updateNotification() {
        val hangIntent = Intent(this, MainActivity::class.java)
        val hangPendingIntent =
            PendingIntent.getActivity(this, 0, hangIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        val notification = mBuilder!!.setContentTitle(resources.getString(R.string.app_name))
            .setContentText("The number of steps today: " + stepCount + " step")
            .setWhen(System.currentTimeMillis())
            .setContentIntent(hangPendingIntent)
            .build()
        mNotificationManager!!.notify(mNotifyIdStep, notification)
        if (mCallback != null) {
            mCallback!!.updateUi(stepCount)
        }
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        when (mStepSensorType) {
            Sensor.TYPE_STEP_COUNTER -> {
                val tempStep = sensorEvent.values[0].toInt()
                Log.d(TAG, "tempStep = $tempStep")
                if (!mHasRecord) {
                    mHasRecord = true
                    mHasStepCount = tempStep
                } else {
                    val thisStepCount = tempStep - mHasStepCount
                    val thisStep = thisStepCount - mPreviousStepCount
                    stepCount += thisStep
                    mPreviousStepCount = thisStepCount
                }
            }
            Sensor.TYPE_STEP_DETECTOR -> if (sensorEvent.values[0].toDouble() == 1.0) {
                stepCount++
            }
        }
        updateNotification()
    }

    override fun onAccuracyChanged(sensor: Sensor, i: Int) {}
    fun saveData() {
        stepNumber = stepCount
    }

    inner class StepBinder : Binder() {
        val service: StepService
            get() = this@StepService
    }

    companion object {
        private val TAG = "TAG: " + StepService::class.java.simpleName
        private var mStepSensorType = -1
    }
}