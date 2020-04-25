package com.us.telemedicine.presentation

import android.os.Bundle
import android.view.*
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.us.telemedicine.R
import com.us.telemedicine.databinding.HomePatientFragmentBinding
import com.us.telemedicine.domain.entity.DoctorEntity
import com.us.telemedicine.global.extention.viewModel
import com.us.telemedicine.global.BaseFragment
import com.us.telemedicine.global.BaseViewModel
import com.us.telemedicine.global.extention.failure
import com.us.telemedicine.global.extention.notify
import com.us.telemedicine.global.extention.observe
import com.us.telemedicine.presentation.onboard.startOnBoardActivity
import timber.log.Timber.d
import javax.inject.Inject

class HomePatientFragment : BaseFragment() {

    @Inject
    lateinit var doctorsAdapter: DoctorsAdapter

    private lateinit var mViewModel: MainViewModel

    override fun getViewModel(): BaseViewModel = mViewModel

    private lateinit var navController: NavController

    private val args: HomePatientFragmentArgs by navArgs()

    // This property is only valid between onCreateView and onDestroyView.
    private val mBinding get() = _binding as HomePatientFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setViewModel()
        navController = findNavController()

        // Check if arguments available(Deep link in this case)
        args.token?.let { d("token: $it") }
    }

    private fun setViewModel() {
        mViewModel = viewModel(viewModelFactory) {
            observe(patientDoctorsResult, ::renderPatientDoctorsList)
            failure(failure, ::handleFailure)
        }
    }

    private fun renderPatientDoctorsList(movies: List<DoctorEntity>?) {
        doctorsAdapter.collection = movies.orEmpty()
        hideProgress()
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

        initializeView()
//        mBinding.navigateActionButton.setOnClickListener{
//            mViewModel.navigate(HomePatientFragmentDirections.chooseDoctorAction())
//        }
    }

    private fun initializeView() {
        mBinding.doctorsList.layoutManager = LinearLayoutManager(activity)
        mBinding.doctorsList.adapter = doctorsAdapter
        doctorsAdapter.clickListener = { doctor -> notify(doctor.fullName) }


        mViewModel.getPatientDoctors()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }
}
