package com.htnguyen.ihealth.view.social

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.base.BaseFragment
import com.htnguyen.ihealth.databinding.FragmentSocialBinding

class SocialFragment : BaseFragment<FragmentSocialBinding, SocialViewModel>() {
    override val layout: Int get() = R.layout.fragment_social
    private val viewModel by viewModels<SocialViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
    }

    private fun initView() {
       binding.fab.visibility = View.VISIBLE
    }


}