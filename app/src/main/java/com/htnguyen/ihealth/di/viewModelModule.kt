package com.htnguyen.ihealth.di

import com.htnguyen.ihealth.view.login.LoginViewModel
import com.htnguyen.ihealth.view.login.RegisterViewModel
import com.htnguyen.ihealth.view.main.MainViewModel
import com.htnguyen.ihealth.view.profile.ProfileEditViewModel
import com.htnguyen.ihealth.view.profile.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel() }
    viewModel { RegisterViewModel() }
    viewModel { ProfileEditViewModel() }
    viewModel { ProfileViewModel() }
    viewModel { MainViewModel() }
}
