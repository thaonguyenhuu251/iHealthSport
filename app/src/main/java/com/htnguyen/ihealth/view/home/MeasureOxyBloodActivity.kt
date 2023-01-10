package com.htnguyen.ihealth.view.home

import android.graphics.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager.WakeLock
import android.view.SurfaceHolder
import com.htnguyen.ihealth.R
import java.util.concurrent.atomic.AtomicBoolean

class MeasureOxyBloodActivity : AppCompatActivity() {

    private lateinit var camera: Camera
    private lateinit var previewHolder: SurfaceHolder
    private lateinit var wakeLock: WakeLock
    private var processing = AtomicBoolean(false)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mesure_oxy_blood)
    }
}