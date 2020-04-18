package com.us.telemedicine.global

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.us.telemedicine.domain.platform.NavigationCommand
import com.us.telemedicine.global.extention.Failure
import kotlinx.coroutines.*

abstract class BaseViewModel : ViewModel() {

    var failure: MutableLiveData<Failure> = MutableLiveData()

    protected fun handleFailure(failure: Failure) {
        this.failure.value = failure
    }

    // FOR NAVIGATION
    private val _navigation: SingleLiveEvent<NavigationCommand> = SingleLiveEvent()
    val navigation: LiveData<NavigationCommand> = _navigation

    /**
     * Convenient method to handle navigation from a [ViewModel]
     */
    fun navigate(directions: NavDirections) {
        _navigation.value =
            NavigationCommand.To(directions)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}