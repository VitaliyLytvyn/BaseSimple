package com.us.telemedicine.presentation.onboard

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.us.telemedicine.R
import com.us.telemedicine.databinding.FragmentForgotPasswordBinding
import com.us.telemedicine.di.Injectable
import com.us.telemedicine.global.BaseFragment
import com.us.telemedicine.global.BaseViewModel
import com.us.telemedicine.global.extention.failure
import com.us.telemedicine.global.extention.observe

class ForgotPasswordFragment : BaseFragment(), Injectable {

    private lateinit var mViewModel: AuthViewModel
    override fun getViewModel(): BaseViewModel = mViewModel
    private lateinit var navController: NavController

    // This property is only valid between onCreateView and onDestroyView.
    private val mBinding get() = _binding as FragmentForgotPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //setHasOptionsMenu(true)
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        val view = mBinding.root
        navController = findNavController()
        setUpVieModel()

        return view
    }

    private fun setUpVieModel() {
        mViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(AuthViewModel::class.java)

        mViewModel.run {
            observe(recoveryResult, ::handleRecoverySuccess)
            observe(resetPasswordFormState, ::handleRecoverFormState)
            failure(failure, ::handleFailure)
        }
    }

    private fun handleRecoverFormState(signInFormState: RecoverFormState?) {
        signInFormState ?: return

        mBinding.resetButton.isEnabled = signInFormState.isDataValid
        signInFormState.emailError?.let {
            mBinding.email.error = getString(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                mViewModel.resetDataChanged(
                    mBinding.email.text.toString()
                )
            }
        }
        mBinding.email.addTextChangedListener(afterTextChangedListener)
        mBinding.email.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                startRecoverProcess()
            }
            false
        }

        mBinding.resetButton.setOnClickListener {
            startRecoverProcess()
        }

        mBinding.rememberPassword.setOnClickListener {
            mViewModel.navigate(ForgotPasswordFragmentDirections.actionPasswordFragmentPop())
        }
    }

    private fun startRecoverProcess(){
        hideKeyboard()
        showProgress()
        mViewModel.recoverPassword(mBinding.email.text.toString())
    }

    private fun handleRecoverySuccess(result: Boolean?) {
        if (result == true) notify(R.string.password_was_changed)
    }

}
