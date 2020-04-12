package com.me.basesimple.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.me.basesimple.R
import com.me.basesimple.databinding.FlowStepOneFragmentBinding
import com.me.basesimple.presentation.entity.RepoView
import com.me.basesimple.presentation.entity.UserEntity
import com.me.basesimple.global.extention.*
import com.me.basesimple.global.BaseFragment
import com.me.basesimple.global.BaseViewModel
import kotlinx.android.synthetic.main.flow_step_one_fragment.*
import timber.log.Timber.d
import timber.log.Timber.e
import javax.inject.Inject


class FlowStepFragment : BaseFragment() {

    @Inject
    lateinit var repoesAdapter: RepoesAdapter

    private lateinit var repoesViewModel: RepoesViewModel
    override fun getViewModel(): BaseViewModel = repoesViewModel
    lateinit var mBinding: FlowStepOneFragmentBinding
    //todo share feature
    private  var modelShare: SharedViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repoesViewModel = viewModel(viewModelFactory) {
            observe(repoes, ::renderReposList)
            observe(allUsers, ::renderAllUsersList)
            observe(fileLoaded, ::fileWasLoaded)
            failure(failure, ::handleFailure)
        }

        //todo share feature
        modelShare = sharedViewModel(viewModelFactory){
            observe(shared, ::updateUI)
        }

    }

    private fun updateUI(str: String?){
        text.text = str ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        d("onCreateView")
        setHasOptionsMenu(true)

        val safeArgs =
            if (arguments != null) FlowStepFragmentArgs.fromBundle(requireArguments())
            else null
        val flowStepNumber = safeArgs?.flowStepNumber

        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.flow_step_one_fragment,
            container,
            false
        )
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()
        loadReposList()

        view.findViewById<View>(R.id.next_button).setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.next_action)
        )

        view.findViewById<View>(R.id.button2).setOnClickListener{
            loadUsersList()
            downLoadTestLargeFileWithDownloadManager()
        }
    }

    private fun initializeView() {
        repoList.layoutManager = LinearLayoutManager(activity)
        repoList.adapter = repoesAdapter
        repoesAdapter.clickListener = { movie -> notify(movie.name) }
    }

    private fun downLoadTestLargeFileWithDownloadManager() {
        repoesViewModel.loadLargeFile()
    }

    private fun loadUsersList() {
        showProgress()
        repoesViewModel.loadAllUsers()
    }

    private fun loadReposList() {
        showProgress()
        repoesViewModel.loadRepoes()
    }

    private fun renderReposList(movies: List<RepoView>?) {
        repoesAdapter.collection = movies.orEmpty()
        hideProgress()
    }

    private fun renderAllUsersList(list: List<UserEntity>?) {
        list?.let {
            var r = ""
            it.map{user ->  r += user.email +"\n"}
            d("AllUsers: $r")
        }
        hideProgress()
    }

    private fun fileWasLoaded(file: String?) {
        e("FILE $file WAS LOADED!")
    }

//    private fun handleFailure(failure: Failure?) {
//        hideProgress()
//
//        when (failure) {
//            is Failure.NetworkConnection -> renderFailure(R.string.failure_network_connection)
//            is Failure.ServerError -> {
//                renderFailure(failure.cause ?: getString(R.string.failure_unknown_error))
//            }
//
//            is Failure.OtherError -> {
//                renderFailure(failure.cause ?: getString(R.string.failure_unknown_error))
//            }
//        }
//    }
//
//    private fun renderFailure(@StringRes message: Int) {
//        notifyWithAction(message, R.string.action_refresh, ::loadReposList)
//    }

    private fun renderFailure(message: String) {
        notifyWithAction(message, R.string.action_refresh, ::loadReposList)
    }
}


