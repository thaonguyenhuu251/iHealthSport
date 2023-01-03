package com.htnguyen.ihealth.view.social

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.base.BaseFragment
import com.htnguyen.ihealth.databinding.FragmentProfileBinding
import com.htnguyen.ihealth.view.profile.ProfileViewModel

class SocialFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {
    override val layout: Int
        get() = R.layout.fragment_social


}