package com.htnguyen.ihealth.support

import android.widget.RadioButton
import android.widget.RadioGroup

val RadioGroup.checked: RadioButton?
    get() = findViewById(checkedRadioButtonId)