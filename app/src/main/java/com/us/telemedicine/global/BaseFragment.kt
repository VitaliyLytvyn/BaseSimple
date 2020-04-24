package com.us.telemedicine.global

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.us.telemedicine.R
import com.us.telemedicine.di.Injectable
import com.us.telemedicine.domain.platform.NavigationCommand
import com.us.telemedicine.global.extention.*
import com.us.telemedicine.global.extention.notify
import com.us.telemedicine.presentation.MainActivity
import com.us.telemedicine.presentation.onboard.OnBoardActivity
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber.d
import timber.log.Timber.e
import javax.inject.Inject


abstract class BaseFragment : Fragment(), EasyPermissions.PermissionCallbacks, Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    abstract fun getViewModel(): BaseViewModel

    protected var _binding: ViewBinding? = null

    /** Observe a [NavigationCommand] [Event] [LiveData].
     * When this [LiveData] is updated, [Fragment] will navigate to its destination  */
    private fun observeNavigation(viewModel: BaseViewModel) {
        viewModel.navigation.observe(viewLifecycleOwner, Observer { command ->
            command ?: return@Observer

            when (command) {
                is NavigationCommand.To -> {
                    findNavController().navigate(command.directions, getExtras())
                }
                is NavigationCommand.Back -> findNavController().navigateUp()
            }
        })
    }

    // Potentially to be overwritten
    protected open fun handleFailure(failure: Failure?) {
        hideProgress()
        when (failure) {
            is Failure.NetworkConnection -> notify(getString(R.string.failure_network_connection))
            is Failure.ServerError -> {
                notify(failure.cause ?: getString(R.string.failure_unknown_error))
            }
            is Failure.OtherError -> {
                notify(failure.cause ?: getString(R.string.failure_unknown_error))
            }
        }
    }

    // [FragmentNavigatorExtras] mainly used to enable Shared Element transition
    open fun getExtras(): FragmentNavigator.Extras = FragmentNavigatorExtras()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //Inject the fragment inside Dagger 2 dependency graph
        // must be called before super.onCreate():
        AndroidSupportInjection.inject(this)
    }

    //todo check behaviour of hiding progress when fragment is on stop - onStop callback doesn't work
    // due to interference with previous fragment's lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideProgress()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeNavigation(getViewModel())
    }

    override fun onStop() {
        super.onStop()
        hideKeyboard()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    internal fun showProgress() = progressStatus(View.VISIBLE)

    internal fun hideProgress() = progressStatus(View.GONE)

    private fun progressStatus(viewStatus: Int) {
        // safety check
        val progressBarContainer = when (requireActivity()) {
            is MainActivity -> (activity as? MainActivity)?.progressBarContainer
            is OnBoardActivity -> (activity as? OnBoardActivity)?.progressBarContainer
            else -> null
        }
        progressBarContainer?.let {
            if (it.visibility == viewStatus) return // prevents unneeded manipulations

            it.visibility = viewStatus
            if (viewStatus == View.VISIBLE) {
                progressBarContainer.setOnClickListener {}//disable clicks on presentation under progress bar
            } else
                progressBarContainer.setOnClickListener(null)//enable clicks on presentation under progress bar
        }
    }

    //for Runtime permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    //for Runtime permission
    override fun onPermissionsDenied(requestCode: Int, @NonNull perms: List<String>) {
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    //for Runtime permission
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}


    //todo for Runtime permission IN PARTICULAR FRAGMENT FOR PARTICULAR PERMISSION
    //todo - do or do NOT something after return from settings
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            //do something after return from settings
//            if (hasLocationPermissions()) {
//                // Have permissions, do the thing!
//            }
        }
    }

    //todo for Runtime permission IN PARTICULAR FRAGMENT FOR PARTICULAR PERMISSION
    @AfterPermissionGranted(RC_LOCATION_PERM)
    fun locationAndContactsTask() {
        if (hasLocationPermissions()) {
            // Have permissions, do the thing!
            d("have permission - > do the thing")
        } else {
            e("have NO permission")
            // Ask for both permissions
            EasyPermissions.requestPermissions(
                this,
                "rationale for location",
                RC_LOCATION_PERM,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    //todo for Runtime permission IN PARTICULAR FRAGMENT FOR PARTICULAR PERMISSION
    companion object {
        const val RC_LOCATION_PERM = 998
    }

}