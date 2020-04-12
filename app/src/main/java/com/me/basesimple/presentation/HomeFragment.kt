package com.me.basesimple.presentation

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.me.basesimple.R
import com.me.basesimple.databinding.HomeFragmentBinding
import com.me.basesimple.global.extention.sharedViewModel
import com.me.basesimple.global.extention.viewModel
import com.me.basesimple.global.BaseFragment
import com.me.basesimple.global.BaseViewModel


/**
 * Fragment used to show how to navigate to another destination
 */
class HomeFragment : BaseFragment() {

    //todo share feature
    private var modelShare: SharedViewModel? = null
    private lateinit var homeVewModel: HomeVewModel
    lateinit var mBinding: HomeFragmentBinding

    override fun getViewModel(): BaseViewModel = homeVewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //todo  share feature
        modelShare = sharedViewModel(viewModelFactory) {}
        homeVewModel = viewModel(viewModelFactory) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.home_fragment,
            container,
            false
        )
        navController = findNavController()
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.navigate_destination_button)?.setOnClickListener {

            //todo share feature
            //modelShare?.share("shared")

            homeVewModel.navigate( HomeFragmentDirections.nextAction() )
        }

        // Update the OnClickListener to navigate using an action
        view.findViewById<Button>(R.id.navigate_action_button)?.setOnClickListener {
            homeVewModel.clickedAction(1)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }
}
