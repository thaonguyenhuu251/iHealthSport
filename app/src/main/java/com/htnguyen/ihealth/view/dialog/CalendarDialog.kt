package com.htnguyen.ihealth.view.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.base.BaseDialog
import com.htnguyen.ihealth.databinding.DialogCalendarBinding
import com.htnguyen.ihealth.inter.OnCalendar
import com.htnguyen.ihealth.util.Constant
import java.util.*

class CalendarDialog : BaseDialog.Scaffold<DialogCalendarBinding>() {
    override val layout: Int = R.layout.dialog_calendar
    override val style: Int = R.style.MyDialog
    override val height: Int = WindowManager.LayoutParams.WRAP_CONTENT
    override val width: Int= WindowManager.LayoutParams.WRAP_CONTENT
    override val dim: Float? get() = 0.5f
    var listener: OnCalendar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calendar.setOnDateChangeListener { calendarView, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, monthOfYear, dayOfMonth)
            arguments?.getInt(Constant.TYPE_CALENDAR)
                ?.let { listener!!.onClickOk(calendar.timeInMillis, it) }
            dismiss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnCalendar
    }

    fun newInstance(date: Long, type: Int): CalendarDialog {
        val dialog = CalendarDialog()
        val args = Bundle()
        args.putLong(Constant.USER_BIRTHDAY, date)
        args.putInt(Constant.TYPE_CALENDAR, type)
        dialog.arguments = args
        return dialog
    }
}