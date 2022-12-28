package com.htnguyen.ihealth.view.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.base.BaseFragment
import com.htnguyen.ihealth.databinding.FragmentHomeBinding
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val layout: Int
        get() = R.layout.fragment_home

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        TODO("Not yet implemented")
    }

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        binding: FragmentHomeBinding
    ) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        optionDailyActivities()
        optionStep()
    }

    private fun optionDailyActivities() {
        binding.layoutDailyActivities.circularProgressBar.apply {
            // Set Progress
            progress = 105f
            // or with animation
            setProgressWithAnimation(105f, 1000) // =1s

            // Set Progress Max
            progressMax = 200f

            // Set ProgressBar Color
            //progressBarColor = Color.BLACK
            // or with gradient
            //progressBarColorStart = Color.GRAY
            //progressBarColorEnd = Color.RED
            //progressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            // Set background ProgressBar Color
            //backgroundProgressBarColor = Color.GRAY
            // or with gradient
            //backgroundProgressBarColorStart = Color.WHITE
            //backgroundProgressBarColorEnd = Color.RED
            //backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            // Set Width
            //progressBarWidth = 7f // in DP
            //backgroundProgressBarWidth = 3f // in DP

            // Other
            //roundBorder = true
            //startAngle = 180f
            //progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }

        binding.layoutDailyActivities.circularProgressBar.onProgressChangeListener = { progress ->
            // Do something
        }

        binding.layoutDailyActivities.circularProgressBar.onIndeterminateModeChangeListener = { isEnable ->
            // Do something
        }

        binding.layoutDailyActivities.circularProgressBar2.apply {
            // Set Progress
            progress = 65f
            // or with animation
            setProgressWithAnimation(65f, 1000) // =1s

            // Set Progress Max
            progressMax = 200f

        }

        binding.layoutDailyActivities.circularProgressBar3.apply {
            // Set Progress
            progress = 85f
            // or with animation
            setProgressWithAnimation(85f, 1000) // =1s

            // Set Progress Max
            progressMax = 200f

        }

    }

    private fun optionStep() {
        binding.layoutStep.processBarStep.progress = 86
        binding.layoutStep.processBarStep.max = 100
    }

}