package com.htnguyen.ihealth.view.social

import android.content.Intent
import android.os.Bundle
import com.htnguyen.ihealth.BR
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.base.BaseActivity
import com.htnguyen.ihealth.databinding.ActivityChooseChallengeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChooseChallengeActivity : BaseActivity<ActivityChooseChallengeBinding, ChooseChallengeViewModel>() {
    override val layout: Int
        get() = R.layout.activity_choose_challenge
    override val viewModel: ChooseChallengeViewModel by viewModel()

    override fun getBindingVariable() = BR.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.txtFirstPerson.setOnClickListener {
            startActivity(Intent(this, CreateChallengeActivity::class.java))
        }
    }

}