package com.htnguyen.ihealth.view.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.base.BaseFragment
import com.htnguyen.ihealth.databinding.FragmentProfileBinding
import com.htnguyen.ihealth.support.SimpleDateFormat
import com.htnguyen.ihealth.util.PreferencesUtil

class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {
    override val layout: Int
        get() = R.layout.fragment_profile

    private val viewModel by viewModels<ProfileViewModel>()
    val db = Firebase.firestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        actionMoveScreen()
    }


    private fun actionMoveScreen() {
        binding.layoutProfileInformation.txtEdit.setOnClickListener {
            val intent = Intent(requireActivity(), ProfileEditActivity::class.java)
            requireActivity().startActivity(intent)
        }

        binding.viewModel = viewModel

        viewModel.birthDay.value = SimpleDateFormat(getString(R.string.common_format_date))
            .format(PreferencesUtil.userBirthDay)
            .toString()
    }
}