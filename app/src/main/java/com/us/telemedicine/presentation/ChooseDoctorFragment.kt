package com.us.telemedicine.presentation

import android.R
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.us.telemedicine.databinding.FragmentChooseDoctorBinding
import com.us.telemedicine.di.Injectable
import com.us.telemedicine.global.BaseFragment
import com.us.telemedicine.global.BaseViewModel
import com.us.telemedicine.global.extention.*


class ChooseDoctorFragment : BaseFragment(), Injectable {

    private lateinit var mViewModel: ChooseDoctorVewModel
    override fun getViewModel(): BaseViewModel = mViewModel
    private lateinit var navController: NavController

    // This property is only valid between onCreateView and onDestroyView.
    private val mBinding get() = _binding as FragmentChooseDoctorBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChooseDoctorBinding.inflate(inflater, container, false)
        val view = mBinding.root
        navController = findNavController()

        setUpVieModel()
        return view
    }

    private fun setUpVieModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(ChooseDoctorVewModel::class.java)

        mViewModel.run {
            observe(chooseDoctorForm, ::handleChooseDoctorSuccess)
            observe(showProgress, ::handleShowProgress)
            failure(failure, ::handleFailure)
        }
    }

     override fun handleFailure(failure: Failure?) {
         mBinding.pbEdit.setInvisible()
         mBinding.doctorsName.setEnabled()

         super.handleFailure(failure)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeDoctorNames()
    }

    private fun setupViews() {
        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                mViewModel.nameChanged(
                    mBinding.doctorsName.text.toString()
                )
            }
        }
        mBinding.doctorsName.addTextChangedListener(afterTextChangedListener)
        mBinding.doctorsName.setOnItemClickListener { _, _, position, _ ->
            mViewModel.choosePosition(
                position = position
            )
        }

        mBinding.saveButton.setOnClickListener {
            mViewModel.saveDoctor()
            mViewModel.navigate(ChooseDoctorFragmentDirections.actionChooseDoctorPop())
        }
    }

    private fun handleShowProgress(showProgress: Boolean?) {
        if (showProgress == true) {
            mBinding.pbEdit.setVisible()
            mBinding.doctorsName.setDisabled()
        } else {
            mBinding.pbEdit.setInvisible()
            mBinding.doctorsName.setEnabled()
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.showSoftInput(mBinding.doctorsName, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun handleChooseDoctorSuccess(chooseDoctorFormState: ChooseDoctorFormState?) {
        chooseDoctorFormState ?: return

        mBinding.saveButton.isEnabled = chooseDoctorFormState.isDataValid
        chooseDoctorFormState.nameError?.let {
            mBinding.doctorsName.error = getString(it)
        }
    }

    private fun observeDoctorNames() {
        mViewModel.doctors.observe(viewLifecycleOwner, Observer { mapList ->
            mapList ?: return@Observer

            val names = mapList.map { doc -> "${doc.firstName} ${doc.lastName}" }
            mBinding.doctorsName
                .setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        R.layout.simple_spinner_dropdown_item,
                        names
                    )
                )
            mBinding.doctorsName.threshold = 1
            mBinding.doctorsName.showDropDown()
        })
    }

}
