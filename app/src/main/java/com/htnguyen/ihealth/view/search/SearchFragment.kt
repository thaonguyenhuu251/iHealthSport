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

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        TODO("Not yet implemented")
    }

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        binding: FragmentSearchBinding
    ) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listRecent()
        listNew()
        listTrending()
        listActivities()
        listShred()
        listWorkout()
        listYoga()
    }

    private fun listRecent() {
        binding.layoutRecent.txtTitle.setText(R.string.search_recent)
    }

    private fun listNew() {
        binding.layoutNew.txtTitle.setText(R.string.search_new)
    }

    private fun listTrending() {
        binding.layoutTrending.txtTitle.setText(R.string.search_trending)
    }

    private fun listActivities() {
        binding.layoutActivities.txtTitle.setText(R.string.search_activity)
    }

    private fun listShred() {
        binding.layoutShredChallenges.txtTitle.setText(R.string.search_shred)
    }

    private fun listWorkout() {
        binding.layoutWorkout.txtTitle.setText(R.string.search_workouts)
    }

    private fun listYoga() {
        binding.layoutYoga.txtTitle.setText(R.string.search_yoga)
    }

}