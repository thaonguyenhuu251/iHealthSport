package com.htnguyen.ihealth.view.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.base.BaseFragment
import com.htnguyen.ihealth.databinding.FragmentProfileBinding
import com.htnguyen.ihealth.databinding.FragmentSearchBinding
import com.htnguyen.ihealth.view.profile.ProfileViewModel

class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>() {
    override val layout: Int
        get() = R.layout.fragment_search

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        binding: FragmentSearchBinding
    ) {
        TODO("Not yet implemented")
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        TODO("Not yet implemented")
    }


}