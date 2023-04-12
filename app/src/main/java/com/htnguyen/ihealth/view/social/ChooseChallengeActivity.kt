package com.htnguyen.ihealth.view.social

import android.content.Intent
import android.os.Bundle
import com.htnguyen.ihealth.BR
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.base.BaseActivity
import com.htnguyen.ihealth.databinding.ActivityChooseChallengeBinding
import com.htnguyen.ihealth.util.Constant
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
            moveCreateChallenge(Constant.CHALLENGE_FIRST_PERSON)
        }

        binding.txtFirstTeam.setOnClickListener {
            moveCreateChallenge(Constant.CHALLENGE_FIRST_TEAM)
        }

        binding.txtGoPerson.setOnClickListener {
            moveCreateChallenge(Constant.CHALLENGE_FARTHEST_PERSON)
        }

        binding.txtGoTeam.setOnClickListener {
            moveCreateChallenge(Constant.CHALLENGE_FARTHEST_TEAM)
        }
    }

    private fun moveCreateChallenge(putExtra: String) {
        val intent = Intent(this, CreateChallengeActivity::class.java)
        intent.putExtra(Constant.CHALLENGE_POST, putExtra)
        startActivity(intent)
    }

}