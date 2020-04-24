package com.us.telemedicine.presentation


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navArgs
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import com.us.telemedicine.R
import com.us.telemedicine.databinding.ActivityMainBinding
import com.us.telemedicine.domain.entity.Role
import com.us.telemedicine.global.BaseActivity
import com.us.telemedicine.global.extention.*
import com.us.telemedicine.presentation.onboard.startOnBoardActivity
import timber.log.Timber.d


class MainActivity : BaseActivity(),
    NavigationView.OnNavigationItemSelectedListener {


    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mViewModel: MainViewModel
    //private val args by navArgs<MainActivityArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Make sure this is before calling super.onCreate
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        obtainViewModel()

        manageActionBar()
        setObservers()

        setComponents()
    }

    // Need to enable show/hide Progress from base activity
    override fun getProgressBarLayout() = mBinding.progressBarContainer

    private fun obtainViewModel() {
        mViewModel = viewModel(viewModelFactory) {}
    }

    private fun manageActionBar() {
        val toolbar = mBinding.toolbar
        setSupportActionBar(toolbar)
    }

    private fun setObservers() {
        mViewModel.run {
            observe(signOutResult, ::renderSignOutResult)
        }
    }

    private fun renderSignOutResult(result: Boolean?) {
        if (result == true) {
            hideProgress()
            goToLoginActivity()
        }
    }

    private fun setComponents() {
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        navController = host.navController

        // Choose graph
        val navGraph = when (mViewModel.getUserType()) {
            Role.Doctor -> R.navigation.doctor_navigation
            Role.Patient -> R.navigation.patient_navigation
            else -> R.navigation.patient_navigation
        }
        navController.setGraph(navGraph, intent.extras)

        setupActionBar(navController)
        setupNavigationMenu(navController)
        setupBottomNavMenu(navController)
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
        mBinding.navView.inflateMenu(R.menu.nav_drawer_menu)///todo check -> removed from main layout
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
            mBinding.drawerLayout.closeDrawers()
            showProgress()
            mViewModel.signOut()
            return true
        } else
            navController.navigate(id)

        mBinding.drawerLayout.closeDrawers()
        return false
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
        return findNavController(R.id.my_nav_host_fragment).navigateUp(mBinding.drawerLayout)
    }

    private fun goToLoginActivity() {
        startOnBoardActivity()
        finish()
    }
}

fun Context.startMainActivity() =
    Intent(this, MainActivity::class.java).apply {}.let(this::startActivity)