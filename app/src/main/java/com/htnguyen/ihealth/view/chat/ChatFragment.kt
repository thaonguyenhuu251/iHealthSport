package com.htnguyen.ihealth.view.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.base.BaseFragment
import com.htnguyen.ihealth.databinding.FragmentChatBinding


class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>() {

    override val layout: Int
        get() = R.layout.fragment_chat

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        binding: FragmentChatBinding
    ) {
        TODO("Not yet implemented")
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChatBinding {
        TODO("Not yet implemented")
    }


}