package com.htnguyen.ihealth.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.htnguyen.ihealth.helper.PrefsHelper
import com.htnguyen.ihealth.support.Calendar
import com.htnguyen.ihealth.support.hour
import com.htnguyen.ihealth.support.minute
import com.htnguyen.ihealth.support.second
import com.htnguyen.ihealth.util.PreferencesUtil


class TimeChangedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (Calendar().hour == 0 && Calendar().minute == 0 && Calendar().second == 0 ) {
            PreferencesUtil.stepNumber = PrefsHelper.getInt("FSteps")
        }
    }
}