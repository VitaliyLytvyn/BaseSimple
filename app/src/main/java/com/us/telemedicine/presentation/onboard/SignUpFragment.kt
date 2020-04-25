package com.us.telemedicine.presentation.onboard

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.us.telemedicine.R
import com.us.telemedicine.databinding.FragmentSignUpBinding
import com.us.telemedicine.di.Injectable
import com.us.telemedicine.global.BaseFragment
import com.us.telemedicine.global.BaseViewModel
import com.us.telemedicine.domain.entity.Role
import com.us.telemedicine.domain.entity.UserEntity
import com.us.telemedicine.global.extention.*
import timber.log.Timber.d

/**
 * A simple [Fragment] subclass.
 */
class SignUpFragment : BaseFragment(), Injectable {

    private lateinit var mViewModel: AuthViewModel
    override fun getViewModel(): BaseViewModel = mViewModel
    private lateinit var navController: NavController
    private var mRole: String? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val mBinding get() = _binding as FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //setHasOptionsMenu(true)
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        navController = findNavController()
        setUpVieModel()

        return mBinding.root
    }

    private fun setUpVieModel() {
        mViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(AuthViewModel::class.java)

        mViewModel.run {
            observe(signUpResult, ::handleSignUpSuccess)
            observe(signUpFormState, ::handleSignUpFormState)
            failure(failure, ::handleFailure)
        }
    }

    private fun handleSignUpSuccess(signUpSuccess: Boolean?) {
        hideProgress()
        if (signUpSuccess == true) {
            mViewModel.navigate(
                SignUpFragmentDirections
                    .signInAfterCreateUser()
                    .setMessageNotify(stringFrom(R.string.user_created_check_email))
            )
        }
    }

    private fun handleSignUpFormState(signInFormState: SignUpFormState?) {
        signInFormState ?: return

        mBinding.signUpButton.isEnabled = signInFormState.isDataValid

        signInFormState.emailError?.let {
            mBinding.email.error = getString(it)
        }
        signInFormState.phoneError?.let {
            mBinding.phone.error = getString(it)
        }
        signInFormState.firstNameError?.let {
            mBinding.firstName.error = getString(it)
        }
        signInFormState.lastNameError?.let {
            mBinding.lastName.error = getString(it)
        }
        signInFormState.passwordError?.let {
            mBinding.password.error = getString(it)
        }
        signInFormState.confirmPasswordError?.let {
            mBinding.confirmPassword.error = getString(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRolesAdapter()
        setupEditTexts()

        //mBinding.haveAccount.setOnClickListener { mViewModel.navigate(SignUpFragmentDirections.actionSignUpFragmentPop()) }
        mBinding.haveAccount.setOnClickListener { mViewModel.navigate(SignUpFragmentDirections.signInAfterCreateUser()) }
    }

    private fun setupEditTexts() {
        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {

                mViewModel.signUpDataChanged(
                    mBinding.email.text.toString(),
                    mBinding.phone.text.toString(),
                    mBinding.firstName.text.toString(),
                    mBinding.lastName.text.toString(),
                    mBinding.password.text.toString(),
                    mBinding.confirmPassword.text.toString()
                )
            }
        }

        mBinding.email.addTextChangedListener(afterTextChangedListener)
        mBinding.phone.addTextChangedListener(afterTextChangedListener)
        mBinding.firstName.addTextChangedListener(afterTextChangedListener)
        mBinding.lastName.addTextChangedListener(afterTextChangedListener)
        mBinding.password.addTextChangedListener(afterTextChangedListener)
        mBinding.confirmPassword.addTextChangedListener(afterTextChangedListener)
        mBinding.confirmPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                signUpProcess()
            }
            false
        }

        mBinding.signUpButton.setOnClickListener { signUpProcess() }
    }

    private fun signUpProcess() {
        mRole ?: return
        hideKeyboard()
        showProgress()
        mViewModel.signUp(
            UserEntity(
                firstName = mBinding.firstName.text.trim().toString(),
                lastName = mBinding.lastName.text.trim().toString(),
                email = mBinding.email.text.toString().nullIfBlank(),
                phone = mBinding.phone.text.toString().nullIfBlank(),
                password = mBinding.password.text.toString(),
                role = Role.valueOf(mRole!!),
                id = null
            )
        )
    }

    private fun setupRolesAdapter() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.user_roles,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            mBinding.spinnerRoles.adapter = adapter
        }
        mBinding.spinnerRoles.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
                d("parent.getItemAtPosition(pos) = ${parent.getItemAtPosition(pos)}")
                mRole = parent.getItemAtPosition(pos).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

}
