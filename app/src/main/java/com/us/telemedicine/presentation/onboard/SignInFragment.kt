package com.us.telemedicine.presentation.onboard

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.us.telemedicine.databinding.SignInFragmentLayoutBinding
import com.us.telemedicine.global.extention.*
import com.us.telemedicine.global.BaseFragment
import com.us.telemedicine.di.Injectable
import com.us.telemedicine.global.BaseViewModel
import com.us.telemedicine.presentation.startMainActivity
import timber.log.Timber.d

class SignInFragment : BaseFragment(), Injectable {

    private lateinit var mViewModel: AuthViewModel
    override fun getViewModel(): BaseViewModel = mViewModel
    private lateinit var navController: NavController
    private val args: SignInFragmentArgs by navArgs()

    // This property is only valid between onCreateView and onDestroyView.
    private val mBinding get() = _binding as SignInFragmentLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //setHasOptionsMenu(true)
        _binding = SignInFragmentLayoutBinding.inflate(inflater, container, false)
        navController = findNavController()
        setUpVieModel()
        return mBinding.root
    }

    private fun setUpVieModel() {
        mViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(AuthViewModel::class.java)

        mViewModel.run {
            observe(signInResult, ::handleLoginSuccess)
            observe(signInFormState, ::handleSignInFormState)
            failure(failure, ::handleFailure)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // If the user presses the back button, exit.
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }

        setupViews()
        // Check if arguments available
        args.messageNotify?.let { notifyLong(it) }
    }

    private fun setupViews() {
        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                mViewModel.signInDataChanged(
                    mBinding.emailOrPhone.text.toString(),
                    mBinding.password.text.toString()
                )
            }
        }
        mBinding.emailOrPhone.addTextChangedListener(afterTextChangedListener)
        mBinding.password.addTextChangedListener(afterTextChangedListener)
        mBinding.password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                startSignIn()
            }
            false
        }

        mBinding.signInButton.setOnClickListener {
            startSignIn()
        }

        mBinding.forgotPassword.setOnClickListener {
            mViewModel.navigate(SignInFragmentDirections.goForgotPasswordAction())
        }

        mBinding.tvDontHaveAccount.setOnClickListener {
            mViewModel.navigate(SignInFragmentDirections.goSignUpFragmentAction())
        }
    }

    private fun startSignIn() {
        hideKeyboard()
        showProgress()
        mViewModel.logIn(
            mBinding.emailOrPhone.text.toString(),
            mBinding.password.text.toString()
        )
    }

    private fun handleSignInFormState(signInFormState: SignInFormState?) {
        signInFormState ?: return

        mBinding.signInButton.isEnabled = signInFormState.isDataValid
        signInFormState.usernameError?.let {
            mBinding.emailOrPhone.error = getString(it)
        }
        signInFormState.passwordError?.let {
            mBinding.password.error = getString(it)
        }
    }

    private fun handleLoginSuccess(authResult: Boolean?) {
        hideProgress()
        if (authResult == true) {
            //notify(R.string.logged_in)
            requireActivity().startMainActivity()
            requireActivity().finish()
        }
    }

}