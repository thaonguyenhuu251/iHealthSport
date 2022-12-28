package com.htnguyen.ihealth.support

import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager

fun InputMethodManager(context: Context) =
    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

fun InputMethodManager.hideKeyboard(v: View, flags: Int = 0) {
    hideSoftInputFromWindow(v.windowToken, flags)
}

fun InputMethodManager.showKeyboard(v: View, flag: Int = InputMethodManager.SHOW_IMPLICIT) {
    showSoftInput(v, flag)
}


fun View.dispatchBackButtonEvent() {
    val input = InputMethodManager(context) ?: return
    setOnClickListener {
        input.dispatchKeyEventFromInputMethod(this, KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))
        input.dispatchKeyEventFromInputMethod(this, KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK))
    }
}