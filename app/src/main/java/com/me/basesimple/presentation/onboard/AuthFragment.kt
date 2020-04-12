package com.me.basesimple.presentation.onboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.me.basesimple.R
import com.me.basesimple.databinding.AuthFragmentLayoutBinding
import com.me.basesimple.global.extention.*
import com.me.basesimple.global.BaseFragment
import com.me.basesimple.di.Injectable
import com.me.basesimple.global.BaseViewModel

class AuthFragment : BaseFragment(), Injectable {

    private lateinit var mViewModel: AuthViewModel
    override fun getViewModel(): BaseViewModel = mViewModel
    lateinit var mBinding: AuthFragmentLayoutBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //setHasOptionsMenu(true)
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.auth_fragment_layout,
            container,
            false
        )
        navController = findNavController()
        setUpVieModel()
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // If the user presses the back button, exit.
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }

        mBinding.googleSignInButton.setOnClickListener {
            mViewModel.logIn()
        }
    }

    private fun setUpVieModel() {
        mViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(AuthViewModel::class.java)

        mViewModel.run {
            //observe(loginResult, ::handleLoginSuccess)
            failure(failure, ::handleFailure)
        }

    }

    private fun handleSaveSuccess(result: Boolean?) {
        hideProgress()
        if (result != null && result == true) {
            notify(R.string.logged_in)
        }
    }

    private fun handleLoginSuccess(authResult: Boolean?) {
        hideProgress()
        if (authResult != null && authResult == true) {
            notify(R.string.logged_in)
        }
    }

}