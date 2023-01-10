package com.htnguyen.ihealth.support.gps
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.htnguyen.ihealth.databinding.ActivityGpsMapBinding

class GpsMap : AppCompatActivity() {

    private lateinit var binding: ActivityGpsMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGpsMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}