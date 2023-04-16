package com.htnguyen.ihealth.view.dialog

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.base.BaseDialog
import com.htnguyen.ihealth.databinding.DialogFollowerWatersBinding

class FollowerWaterDialog : BaseDialog.Scaffold<DialogFollowerWatersBinding>() {
    override val layout: Int = R.layout.dialog_follower_waters
    override val style: Int = R.style.MyDialog
    override val height: Int = WindowManager.LayoutParams.WRAP_CONTENT
    override val width: Int= WindowManager.LayoutParams.WRAP_CONTENT
    override val dim: Float? get() = 0.2f
    override val positionY: Int = 550

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.numberpicker.minValue = 3
        binding.numberpicker.maxValue = 10
        binding.numberpicker.value = 4

    }
}