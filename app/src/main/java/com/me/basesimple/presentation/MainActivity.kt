package com.me.basesimple.presentation


import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.navigation.NavigationView
import com.me.basesimple.R
import com.me.basesimple.databinding.ActivityMainBinding
import com.me.basesimple.domain.Authenticator
import com.me.basesimple.global.extention.*
import com.me.basesimple.presentation.onboard.startOnBoardActivity
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import timber.log.Timber.d
import timber.log.Timber.e
import javax.inject.Inject


class MainActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }

    private lateinit var appBarConfiguration : AppBarConfiguration
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mViewModel: MainViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        // Make sure this is before calling super.onCreate
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        obtainViewModel()

        if(!mViewModel.isLoggedIn()){
            goToLoginActivity()
            return
        }

        // Set up Action Bar
        val toolbar = mBinding.toolbar
        setSupportActionBar(toolbar)

        //check if Google Play Services available
        if (checkPlayServices()) {
            // Start application
            setComponents()
        } else {
            e("MainActivity GoogleApiAvailability = NOT AVAILABLE")
        }
    }

    private fun obtainViewModel() {
        mViewModel= viewModel(viewModelFactory) {
            //observe(userLiveData, ::renderAuthState)
            observe(authenticationState, ::renderAuthState)
        }
    }

    private fun renderAuthState(authenticationState: Authenticator.AuthenticationState?) {
        when (authenticationState) {
            Authenticator.AuthenticationState.AUTHENTICATED -> d( "Authenticated")
            // If the user is not logged in, they should not be able to set any preferences,
            // so navigate them to the login fragment
           Authenticator.AuthenticationState.UNAUTHENTICATED -> {
               d( "UNAUTHENTICATED")
               //navController.navigate(R.id.authFragment)

           }
            else -> e("New $authenticationState state that doesn't require any UI change")
        }
    }

    private fun setComponents() {
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        navController = host.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
//                appBarConfiguration = AppBarConfiguration(
//                setOf(R.id.home_dest, R.id.flow_step_two_dest)
//                )


        setupActionBar(navController, appBarConfiguration)

        setupNavigationMenu(navController)

        setupBottomNavMenu(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.authFragment) {
                //drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                mBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                mBinding.bottomNavView.setGone()
            } else {
                mBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                mBinding.bottomNavView.setVisible()
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
//        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
//        bottomNav?.setupWithNavController(navController)
        mBinding.bottomNavView.setupWithNavController(navController)
    }

    private fun setupNavigationMenu(navController: NavController) {
        //Use NavigationUI to set up a Navigation View
        // In split screen mode, you can drag this presentation out from the left
        // This does NOT modify the actionbar
//        val sideNavView = findViewById<NavigationView>(R.id.nav_view)
//        sideNavView?.setupWithNavController(navController)
        mBinding.navView.setupWithNavController(navController)
    }

    private fun setupActionBar(navController: NavController, appBarConfig : AppBarConfiguration) {
        // Have NavigationUI handle what your ActionBar displays
        // This allows NavigationUI to decide what label to show in the action bar
        // And, since we are passing in drawerLayout, it will also determine whether to
        // show the up arrow or drawer menu icon

        //drawerLayout = findViewById(R.id.drawer_layout)
        //setupActionBarWithNavController(navController, drawerLayout)
        setupActionBarWithNavController(navController, mBinding.drawerLayout)

        //setupActionBarWithNavController(navController, appBarConfig)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val retValue = super.onCreateOptionsMenu(menu)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)

        // The NavigationView already has these same navigation items, so we only add
        // navigation items to the menu here if there isn't a NavigationView
        if (navigationView == null) {
            menuInflater.inflate(R.menu.overflow_menu, menu)
            return true
        }
        return retValue
    }

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
        //return drawerLayout.navigateUp(findNavController(R.id.my_nav_host_fragment))

        return findNavController(R.id.my_nav_host_fragment).navigateUp(mBinding.drawerLayout)

        //return findNavController(R.id.my_nav_host_fragment).navigateUp(appBarConfiguration)
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
                val dialog = apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                dialog.setOnDismissListener { closeApp() }
                dialog.show()
            } else {
                Timber.i("This device is not supported.")
                closeApp()
            }
            return false
        }
        return true
    }

    override fun onBackPressed() {
        val currentDestination=navController.currentDestination ?: return
        when(currentDestination.id) {
            R.id.authFragment -> {
                finish()
            }
        }
        super.onBackPressed()
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