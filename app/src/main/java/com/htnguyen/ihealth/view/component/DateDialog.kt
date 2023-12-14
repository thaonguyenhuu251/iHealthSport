package com.htnguyen.ihealth.view.component

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.inter.OnDialog
import com.htnguyen.ihealth.databinding.DialogDateBinding
import com.htnguyen.ihealth.support.SimpleDateFormat
import com.htnguyen.ihealth.util.Constant
import java.util.*


class DateDialog : DialogFragment() {
    private lateinit var binding: DialogDateBinding
    var listener: OnDialog ? = null

    fun newInstance(date: Long): DateDialog {
        val dialog = DateDialog()
        val args = Bundle()
        args.putLong(Constant.USER_BIRTHDAY, date)
        dialog.arguments = args
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogDateBinding.inflate(layoutInflater)
        val today = arguments?.getLong(Constant.USER_BIRTHDAY)

        binding.dayPicker.init(
            SimpleDateFormat("yyyy").format(today).toString().toInt(),
            SimpleDateFormat("MM").format(today).toString().toInt() - 1,
            SimpleDateFormat("dd").format(today).toString().toInt()
        ) { view, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, monthOfYear, dayOfMonth)
            listener!!.onClickOk(calendar.timeInMillis)
            dismiss()
        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnDialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dialog = Dialog(requireContext())
        val window = dialog.window
        window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        setStyle(STYLE_NO_TITLE, R.style.MyDialog)
    }

    companion object {
        const val TAG = "DialogDate"
    }
}
