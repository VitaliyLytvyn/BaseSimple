package com.me.basesimple.global

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.me.basesimple.domain.platform.NavigationCommand
import com.me.basesimple.global.extention.Failure
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
    fun navigate(directions2: NavDirections) {
        _navigation.value =
            NavigationCommand.To(directions2)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}