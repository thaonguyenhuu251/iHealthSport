package com.htnguyen.ihealth.view.social

import android.os.Bundle
import androidx.activity.viewModels
import com.htnguyen.ihealth.BR
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.base.BaseActivity
import com.htnguyen.ihealth.databinding.ActivityCreateChalengeBinding

class CreateChallengeActivity : BaseActivity<ActivityCreateChalengeBinding, CreateChallengeViewModel>() {

    override val layout: Int
        get() = R.layout.activity_create_chalenge
    override val viewModel: CreateChallengeViewModel by viewModels()
    override fun getBindingVariable() = BR.viewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.imgBack.setOnClickListener {
            finish()
        }

    }

}