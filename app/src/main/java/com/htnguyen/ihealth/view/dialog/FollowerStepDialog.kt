package com.htnguyen.ihealth.view.dialog

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.base.BaseDialog
import com.htnguyen.ihealth.databinding.DialogFollowerStepBinding

class FollowerStepDialog : BaseDialog.Scaffold<DialogFollowerStepBinding>() {
    override val layout: Int = R.layout.dialog_follower_step
    override val style: Int = R.style.MyDialog
    override val height: Int = WindowManager.LayoutParams.WRAP_CONTENT
    override val width: Int= WindowManager.LayoutParams.WRAP_CONTENT
    override val dim: Float? get() = 0.2f
    override val positionY: Int = 400

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val values = arrayOf("1000", "2000", "3000", "4000", "5000", "6000", "7000", "8000", "9000", "10000", "15000", "20000", "25000", "30000")
        binding.numberpicker.displayedValues = values
        binding.numberpicker.minValue = 0
        binding.numberpicker.maxValue = values.size - 1
        binding.numberpicker.value = 4

    }
}