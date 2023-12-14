package com.htnguyen.ihealth.view.social

import android.os.Bundle
import androidx.activity.viewModels
import com.htnguyen.ihealth.BR
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.base.BaseActivity
import com.htnguyen.ihealth.databinding.ActivityCreateChalengeBinding
import com.htnguyen.ihealth.inter.OnCalendar
import com.htnguyen.ihealth.inter.OnDialog
import com.htnguyen.ihealth.util.Constant
import com.htnguyen.ihealth.view.dialog.CalendarDialog

class CreateChallengeActivity :
    BaseActivity<ActivityCreateChalengeBinding, CreateChallengeViewModel>(), OnCalendar {

    override val layout: Int
        get() = R.layout.activity_create_chalenge
    override val viewModel: CreateChallengeViewModel by viewModels()
    override fun getBindingVariable() = BR.viewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.imgBack.setOnClickListener {
            finish()
        }

        if (intent != null) {
            when (intent.getStringExtra(Constant.CHALLENGE_POST)) {
                Constant.CHALLENGE_FIRST_PERSON -> viewModel.typeChallenge.value = 0
                Constant.CHALLENGE_FIRST_TEAM -> viewModel.typeChallenge.value = 1
                Constant.CHALLENGE_FARTHEST_PERSON -> viewModel.typeChallenge.value = 2
                else -> viewModel.typeChallenge.value = 3
            }
        }

        binding.txtFirstDate.setOnClickListener {
            CalendarDialog().newInstance(0L, 0).show(this)
        }

        binding.txtEndDate.setOnClickListener {
            CalendarDialog().newInstance(0L, 1).show(this)
        }


    }

    override fun onClickOk(date: Long, type: Int) {
        if (type == 0) {
            viewModel.startDate.value = date
        }
    }

}