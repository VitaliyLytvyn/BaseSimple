package com.us.telemedicine.presentation


import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.navigation.NavigationView
import com.us.telemedicine.R
import com.us.telemedicine.databinding.ActivityMainBinding
import com.us.telemedicine.domain.Authenticator
import com.us.telemedicine.global.extention.*
import com.us.telemedicine.presentation.onboard.startOnBoardActivity
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber.d
import timber.log.Timber.e
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidInjector,
    NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mViewModel: MainViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        // Make sure this is before calling super.onCreate
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)

        //mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        obtainViewModel()

        // Check if token is not expired
        if (mViewModel.isSignInRequired()) {
            goToLoginActivity()
            return
        }

        manageActionBar()
        setObservers()

        //check if Google Play Services available
        if (checkPlayServices()) {
            // Start application
            setComponents()
        } else {
            e("GoogleApiAvailability = NOT AVAILABLE")
        }
    }

    private fun obtainViewModel() {
        mViewModel = viewModel(viewModelFactory) {}
    }

    private fun manageActionBar() {
        // Set up Action Bar
        val toolbar = mBinding.toolbar
        setSupportActionBar(toolbar)
    }

    private fun setObservers() {

        mViewModel.run {

            // Observe Auth state, when Sign out it will lead to state being UNAUTHENTICATED
            observe(authenticationState, ::renderAuthState)

            observe(signOutResult, ::renderSignOutResult)
        }
    }

    private fun renderSignOutResult(result: Boolean?) {
        if (result == true) {
            hideProgress()
        }
    }

    private fun renderAuthState(authenticationState: Authenticator.AuthenticationState?) {
        when (authenticationState) {
            Authenticator.AuthenticationState.AUTHENTICATED -> d("Authenticated")
            // If the user is not logged in, they should not be able to set any preferences,
            // so navigate them to the login fragment
            Authenticator.AuthenticationState.UNAUTHENTICATED -> {
                d("UNAUTHENTICATED")
                //navController.navigate(R.id.authFragment)
                goToLoginActivity()

            }
            else -> e("New $authenticationState state that doesn't require any UI change")
        }
    }

    private fun setComponents() {
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        navController = host.navController

        setupActionBar(navController)

        setupNavigationMenu(navController)

        setupBottomNavMenu(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.authFragment) {
            }

            val dest: String = try {
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException) {
                destination.id.toString()
            }

            d("Navigated to $dest")
        }

    }

    private fun setupBottomNavMenu(navController: NavController) {
        // Use NavigationUI to set up Bottom Nav
        mBinding.bottomNavView.setupWithNavController(navController)
    }

    private fun setupNavigationMenu(navController: NavController) {
        //Use NavigationUI to set up a Navigation View
        // In split screen mode, you can drag this presentation out from the left
        // This does NOT modify the actionbar
        //mBinding.navView.setupWithNavController(navController)
        mBinding.navView.setNavigationItemSelectedListener(this)
    }

    private fun setupActionBar(navController: NavController) {
        // Have NavigationUI handle what your ActionBar displays
        // This allows NavigationUI to decide what label to show in the action bar
        // And, since we are passing in drawerLayout, it will also determine whether to
        // show the up arrow or drawer menu icon
        setupActionBarWithNavController(navController, mBinding.drawerLayout)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId;

        if (id == R.id.sign_out) {
            showProgress()
            mViewModel.signOut()
            return true
        } else
            navController.navigate(id)

        mBinding.drawerLayout.closeDrawers()
        return false
    }

    internal fun showProgress() = mBinding.progressBarContainer.setVisible()

    internal fun hideProgress() = mBinding.progressBarContainer.setGone()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //return super.onOptionsItemSelected(item)

        // Have the NavigationUI look for an action or destination matching the menu
        // item id and navigate there if found.
        // Otherwise, bubble up to the parent.
        return item.onNavDestinationSelected(findNavController(R.id.my_nav_host_fragment))
                || super.onOptionsItemSelected(item)
    }

    // Have NavigationUI handle up behavior in the ActionBar
    override fun onSupportNavigateUp(): Boolean {
        // Allows NavigationUI to support proper up navigation or the drawer layout
        // drawer menu, depending on the situation
        return findNavController(R.id.my_nav_host_fragment).navigateUp(mBinding.drawerLayout)
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private fun checkPlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                val dialog = apiAvailability.getErrorDialog(
                    this,
                    resultCode,
                    PLAY_SERVICES_RESOLUTION_REQUEST
                )
                dialog.setOnDismissListener { closeApp() }
                dialog.show()
            } else {
                d("This device is not supported.")
                closeApp()
            }
            return false
        }
        return true
    }

    private fun closeApp() = finish()

    private fun goToLoginActivity() {
        startOnBoardActivity()
        closeApp()
    }

    companion object {
        const val PLAY_SERVICES_RESOLUTION_REQUEST = 4794
    }
}

fun Context.startMainActivity() =
    Intent(this, MainActivity::class.java).apply {}.let(this::startActivity)