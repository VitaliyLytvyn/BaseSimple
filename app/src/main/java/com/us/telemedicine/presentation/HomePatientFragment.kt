package com.us.telemedicine.presentation

import android.os.Bundle
import android.view.*
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.us.telemedicine.R
import com.us.telemedicine.databinding.HomePatientFragmentBinding
import com.us.telemedicine.global.extention.viewModel
import com.us.telemedicine.global.BaseFragment
import com.us.telemedicine.global.BaseViewModel
import com.us.telemedicine.presentation.onboard.startOnBoardActivity
import timber.log.Timber.d


class HomePatientFragment : BaseFragment() {

    private lateinit var mViewModel: MainViewModel

    override fun getViewModel(): BaseViewModel = mViewModel

    private lateinit var navController: NavController

    private val args: HomePatientFragmentArgs by navArgs()

    // This property is only valid between onCreateView and onDestroyView.
    private val mBinding get() = _binding as HomePatientFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = viewModel(viewModelFactory) {}

        navController = findNavController()

        // Check if arguments available(Deep link in this case)
        args.token?.let { d("token: $it") }
    }

    private fun goToLoginActivity() {
        activity?.startOnBoardActivity()
        activity?.finish()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //setHasOptionsMenu(true)

        // Check if user authenticated(token, if present, is not expired)
        // placed here to avoid interference with a case when not authenticated user comes by deep link
        if (mViewModel.isSignInRequired()) {
            goToLoginActivity()
            return null
        }

        _binding = HomePatientFragmentBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.navigateActionButton.setOnClickListener{
            mViewModel.navigate(HomePatientFragmentDirections.chooseDoctorAction())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }
}
