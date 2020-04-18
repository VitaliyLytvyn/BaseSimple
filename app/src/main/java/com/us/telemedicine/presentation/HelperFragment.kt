package com.us.telemedicine.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.us.telemedicine.databinding.HelperFragmentBinding
import com.us.telemedicine.presentation.entity.RepoView
import com.us.telemedicine.global.extention.*
import com.us.telemedicine.global.BaseFragment
import com.us.telemedicine.global.BaseViewModel
import javax.inject.Inject


class HelperFragment : BaseFragment() {

    @Inject
    lateinit var repoesAdapter: RepoesAdapter

    private lateinit var mViewModel: MainViewModel
    override fun getViewModel(): BaseViewModel = mViewModel

    // This property is only valid between onCreateView and onDestroyView.
    private val mBinding get() = _binding as HelperFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = viewModel(viewModelFactory) {
            //observe(repoes, ::renderReposList)
            failure(failure, ::handleFailure)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //setHasOptionsMenu(true)

        val safeArgs =
            if (arguments != null) HelperFragmentArgs.fromBundle(requireArguments())
            else null
        val helperNumber = safeArgs?.helperNumber

        _binding = HelperFragmentBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()
    }

    private fun initializeView() {
        mBinding.repoList.layoutManager = LinearLayoutManager(activity)
        mBinding.repoList.adapter = repoesAdapter
        repoesAdapter.clickListener = { movie -> notify(movie.name) }
    }

    private fun renderReposList(movies: List<RepoView>?) {
        repoesAdapter.collection = movies.orEmpty()
        hideProgress()
    }

}


