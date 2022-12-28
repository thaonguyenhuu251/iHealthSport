package com.htnguyen.ihealth.view.component

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.htnguyen.ihealth.R

class LoadingDialog2(activity: Activity) {

    private var dialog: AlertDialog

    init {
        val view = activity.layoutInflater.inflate(R.layout.dialog_loading_2, null)


        dialog = AlertDialog.Builder(activity, R.style.DialogWidth)
            .create().apply {
                setView(view)
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setCanceledOnTouchOutside(false)
            }
    }

    fun showDialog(){
        if (!dialog.isShowing){
            dialog.show()
        }
    }

    fun dismissDialog(){
        if (dialog.isShowing){
            dialog.dismiss()
        }
    }
}