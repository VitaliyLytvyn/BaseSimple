package com.us.telemedicine.global.extention

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


inline fun <reified T : ViewModel> Fragment.viewModel(factory: ViewModelProvider.Factory, body: T.() -> Unit): T {
    val vm = ViewModelProvider(this, factory)[T::class.java]
    vm.body()
    return vm
}

inline fun <reified T : ViewModel> AppCompatActivity.viewModel(factory: ViewModelProvider.Factory, body: T.() -> Unit): T {
    val vm = ViewModelProvider(this, factory)[T::class.java]
    vm.body()
    return vm
}

inline fun <reified T : ViewModel> Fragment.sharedViewModel(factory: ViewModelProvider.Factory, body: T.() -> Unit): T? {
    activity ?: return null
    val vm = ViewModelProvider(this.requireActivity(), factory)[T::class.java]
    vm.body()
    return vm
}