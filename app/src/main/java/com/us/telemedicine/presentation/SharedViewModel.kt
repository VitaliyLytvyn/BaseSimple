package com.us.telemedicine.presentation

import androidx.lifecycle.MutableLiveData
import com.us.telemedicine.global.BaseViewModel
import javax.inject.Inject

class SharedViewModel @Inject constructor() : BaseViewModel() {
    val shared = MutableLiveData<String>()

    fun share(item: String) {
        shared.value = item
    }
}
