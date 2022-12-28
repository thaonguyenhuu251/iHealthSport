package com.htnguyen.ihealth.di

import com.htnguyen.ihealth.view.login.LoginViewModel
import com.htnguyen.ihealth.view.login.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel() }
    viewModel { RegisterViewModel() }
}
