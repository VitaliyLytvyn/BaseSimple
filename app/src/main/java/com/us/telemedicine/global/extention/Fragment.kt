package com.us.telemedicine.global.extention

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.us.telemedicine.R


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

internal fun Fragment.hideKeyboard() {
    view ?: return
    val imm =
        requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(requireView().windowToken, 0)
}

internal fun Fragment.notify(@StringRes message: Int) = notify(getString(message))

internal fun Fragment.notify(message: String) {
    view?.let {
        Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
    }
}

internal fun Fragment.notifyLong(@StringRes message: Int) = notifyLong(getString(message))

internal fun Fragment.notifyLong(message: String) {
    view?.let {
        Snackbar.make(it, message, Snackbar.LENGTH_LONG).show()
    }
}

internal fun Fragment.notifyWithAction(
    @StringRes message: Int,
    @StringRes actionText: Int,
    action: () -> Any
) {
    val s = activity?.getString(message) ?: return
    notifyWithAction(s, actionText, action)
}

internal fun Fragment.notifyWithAction(message: String, @StringRes actionText: Int, action: () -> Any) {
    view?.let {
        val snackBar = Snackbar.make(it, message, Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(actionText) { _ -> action.invoke() }
        snackBar.setActionTextColor(colorFrom(R.color.colorTextPrimary))
        snackBar.show()
    }
}