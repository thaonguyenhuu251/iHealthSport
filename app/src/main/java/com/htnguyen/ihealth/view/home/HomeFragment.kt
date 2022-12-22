package com.htnguyen.ihealth.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.base.BaseFragment
import com.htnguyen.ihealth.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val layout: Int
        get() = R.layout.fragment_home

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        TODO("Not yet implemented")
    }

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        binding: FragmentHomeBinding
    ) {

    }

}