package com.us.telemedicine.presentation

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.us.telemedicine.R
import com.us.telemedicine.databinding.HomeFragmentBinding
import com.us.telemedicine.global.extention.sharedViewModel
import com.us.telemedicine.global.extention.viewModel
import com.us.telemedicine.global.BaseFragment
import com.us.telemedicine.global.BaseViewModel
import timber.log.Timber


class HomeFragment : BaseFragment() {

    private var modelShare: SharedViewModel? = null
    private lateinit var mViewModel: MainViewModel

    override fun getViewModel(): BaseViewModel = mViewModel

    private lateinit var navController: NavController

    private val args: HomeFragmentArgs by navArgs()

    // This property is only valid between onCreateView and onDestroyView.
    private val mBinding get() = _binding as HomeFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        modelShare = sharedViewModel(viewModelFactory) {}
        mViewModel = viewModel(viewModelFactory) {}

        // Check if arguments available(Deep link in this case)
        args.token?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //setHasOptionsMenu(true)
        _binding = HomeFragmentBinding.inflate(inflater, container, false)

        navController = findNavController()
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }
}
