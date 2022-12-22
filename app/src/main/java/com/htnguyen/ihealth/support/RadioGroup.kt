package com.htnguyen.ihealth.support

import android.widget.RadioButton
import android.widget.RadioGroup


/**
 * RadioGroup
 *
 * @author QIAO LIYUN
 */
val RadioGroup.checked: RadioButton?
    get() = findViewById(checkedRadioButtonId)