package com.htnguyen.ihealth.view.social

import androidx.lifecycle.MutableLiveData
import com.htnguyen.ihealth.base.BaseViewModel

class CreateChallengeViewModel: BaseViewModel() {
    val typeChallenge: MutableLiveData<Int> = MutableLiveData(null)
    val startDate: MutableLiveData<Long> = MutableLiveData(null)
}