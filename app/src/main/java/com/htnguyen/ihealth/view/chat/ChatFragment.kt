package com.htnguyen.ihealth.view.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.base.BaseFragment
import com.htnguyen.ihealth.databinding.FragmentChatBinding


class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>() {

    override val layout: Int
        get() = R.layout.fragment_chat

}