package com.us.telemedicine.presentation.onboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.us.telemedicine.R
import com.us.telemedicine.databinding.ActivityOnBoardBinding
import com.us.telemedicine.global.BaseActivity


class OnBoardActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mBinding: ActivityOnBoardBinding
    private lateinit var mViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityOnBoardBinding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)
        //mBinding = DataBindingUtil.setContentView(this, R.layout.activity_on_board)

        obtainViewModel()
        setUpActionBar()
        setNavigationComponents()
    }

    // Need to enable show/hide Progress from base activity
    override fun getProgressBarLayout() = mBinding.progressBarContainer

    private fun setUpActionBar(){
        val toolbar = mBinding.toolbar
        setSupportActionBar(toolbar)

        //appBarConfiguration = AppBarConfiguration(setOf(R.id.login_host_fragment))
        //setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun obtainViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(AuthViewModel::class.java)
        mViewModel.run {
            //observe(authenticationState, ::renderAuthState)
        }
    }

    private fun setNavigationComponents() {
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.login_host_fragment) as NavHostFragment? ?: return
        navController = host.navController

        //appBarConfiguration = AppBarConfiguration(setOf(R.id.login_host_fragment))
        appBarConfiguration = AppBarConfiguration(navController.graph)

        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    // Have NavigationUI handle up behavior in the ActionBar
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}

fun Context.startOnBoardActivity() =
    Intent(this, OnBoardActivity::class.java).apply {}.let(this::startActivity)
