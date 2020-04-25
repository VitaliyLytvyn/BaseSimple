package com.us.telemedicine.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.us.telemedicine.databinding.HelperFragmentBinding
import com.us.telemedicine.domain.entity.DoctorEntity
import com.us.telemedicine.presentation.entity.RepoView
import com.us.telemedicine.global.extention.*
import com.us.telemedicine.global.BaseFragment
import com.us.telemedicine.global.BaseViewModel
import timber.log.Timber
import javax.inject.Inject


class HelperFragment : BaseFragment() {

    @Inject
    lateinit var doctorsAdapter: DoctorsAdapter

    private lateinit var mViewModel: MainViewModel
    override fun getViewModel(): BaseViewModel = mViewModel

    // This property is only valid between onCreateView and onDestroyView.
    private val mBinding get() = _binding as HelperFragmentBinding

    private val args:  HelperFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = viewModel(viewModelFactory) {
            //observe(repoes, ::renderReposList)
            failure(failure, ::handleFailure)
        }

        // Check if arguments available(Deep link in this case)
        args.token444?.let { Timber.d("token: $it") }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //setHasOptionsMenu(true)

        _binding = HelperFragmentBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()
    }

    private fun initializeView() {
        mBinding.repoList.layoutManager = LinearLayoutManager(activity)
        mBinding.repoList.adapter = doctorsAdapter
        doctorsAdapter.clickListener = { doctor -> notify(doctor.fullName) }
    }

    private fun renderReposList(doctors: List<DoctorEntity>?) {
        doctorsAdapter.collection = doctors.orEmpty()
        hideProgress()
    }

}


