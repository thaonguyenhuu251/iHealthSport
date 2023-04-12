package com.htnguyen.ihealth.view.social

import androidx.activity.viewModels
import com.htnguyen.ihealth.BR
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.base.BaseActivity
import com.htnguyen.ihealth.databinding.ActivityCreatePostBinding

class CreatePostActivity : BaseActivity<ActivityCreatePostBinding, CreatePostViewModel>() {
    override val layout: Int get() = R.layout.activity_create_post
    override val viewModel: CreatePostViewModel by viewModels()
    override fun getBindingVariable(): Int = BR.viewModel

}