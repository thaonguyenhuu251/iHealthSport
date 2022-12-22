package com.htnguyen.ihealth.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.base.BaseFragment
import com.htnguyen.ihealth.databinding.FragmentHomeBinding
import com.htnguyen.ihealth.databinding.FragmentProfileBinding

class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {
    override val layout: Int
        get() = R.layout.fragment_profile

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        binding: FragmentProfileBinding
    ) {
        TODO("Not yet implemented")
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding {
        TODO("Not yet implemented")
    }

}