package com.me.basesimple.presentation.onboard

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.snackbar.Snackbar
import com.me.basesimple.R
import com.me.basesimple.databinding.ActivityOnBoardBinding
import com.me.basesimple.domain.Authenticator
import com.me.basesimple.global.extention.observe
import com.me.basesimple.presentation.startMainActivity
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class OnBoardActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mBinding: ActivityOnBoardBinding
    private lateinit var mViewModel: AuthViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_on_board)
        obtainViewModel()
        setUpActionBar()
        setNavigationComponents()
    }

    private fun setUpActionBar(){
        val toolbar = mBinding.toolbar
        setSupportActionBar(toolbar)
    }

    private fun obtainViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(AuthViewModel::class.java)
        mViewModel.run {
            //observe(userLiveData, ::renderAuthState)
            observe(authenticationState, ::renderAuthState)
        }
    }

    // Observe the authentication state so we can know if the user has logged in successfully.
    // If the user has logged in successfully, bring them back to the home screen.
    // If the user did not log in successfully, display an error message.
    private fun renderAuthState(authenticationState: Authenticator.AuthenticationState?) {
        when (authenticationState) {
            Authenticator.AuthenticationState.AUTHENTICATED -> goInApp()
            Authenticator.AuthenticationState.INVALID_AUTHENTICATION -> Snackbar.make(
                mBinding.root, getString(R.string.logged_in),
                Snackbar.LENGTH_LONG
            ).show()
            else -> Timber.e("Authentication state that doesn't require any UI change $authenticationState")
        }
    }

    private fun setNavigationComponents() {
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.login_host_fragment) as NavHostFragment? ?: return
        navController = host.navController
        appBarConfiguration = AppBarConfiguration(setOf(R.id.authFragment))
    }

    // Have NavigationUI handle up behavior in the ActionBar
    override fun onSupportNavigateUp(): Boolean {
        //return findNavController(R.id.my_nav_host_fragment).navigateUp(mBinding.drawerLayout)
        //return navController.navigateUp(appBarConfiguration)
        return navController.navigateUp()
    }

    private fun goInApp(){
        startMainActivity()
        finish()
    }
}

fun Context.startOnBoardActivity() =
    Intent(this, OnBoardActivity::class.java).apply {}.let(this::startActivity)
