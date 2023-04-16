package com.htnguyen.ihealth.view.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.hardware.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.view.SurfaceHolder
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.databinding.ActivityMainBinding
import com.htnguyen.ihealth.databinding.ActivityMeasureHeartBeatBinding
import com.htnguyen.ihealth.databinding.ActivityMesureOxyBloodBinding
import com.htnguyen.ihealth.support.CameraService
import java.util.concurrent.atomic.AtomicBoolean

class MeasureOxyBloodActivity : AppCompatActivity() {

    private lateinit var camera: Camera
    private lateinit var previewHolder: SurfaceHolder
    private lateinit var wakeLock: WakeLock
    private var processing = AtomicBoolean(false)

    private var starTime = 0L
    private var samplingFreq: Double? = null

    private var redBlueRatio = 0L
    private var stdr = 0L
    private var stdb = 0L
    private var sumRed = 0L
    private var sumBlue = 0L

    private var o2: Int? = null

    private var redAvgList = ArrayList<Double>()
    private var blueAvgList = ArrayList<Double>()
    private var counter = 0


    private lateinit var binding: ActivityMesureOxyBloodBinding
    @SuppressLint("InvalidWakeLockTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMesureOxyBloodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        previewHolder = binding.preview.holder
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)

        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "NOTDIMSCREEN")

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onResume() {
        super.onResume()
        wakeLock.acquire()
        camera = Camera.open()
        camera.setDisplayOrientation(90)
        starTime = System.currentTimeMillis()
    }

    override fun onPause() {
        super.onPause()
        wakeLock.release()
        camera.setPreviewCallback(null)
        camera.stopPreview()
        camera.release()
    }

}