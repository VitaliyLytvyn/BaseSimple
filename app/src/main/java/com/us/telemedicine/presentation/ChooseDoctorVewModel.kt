package com.us.telemedicine.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.us.telemedicine.R
import com.us.telemedicine.domain.entity.DoctorEntity
import com.us.telemedicine.domain.interactor.GetDoctorsUC
import com.us.telemedicine.global.BaseViewModel
import com.us.telemedicine.global.SingleLiveEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChooseDoctorVewModel
@Inject constructor(
    val getDoctorsUC: GetDoctorsUC
) : BaseViewModel() {

    private val _doctors = MutableLiveData<List<DoctorEntity>>()
    val doctors: LiveData<List<DoctorEntity>> = _doctors

    private val _chooseDoctorForm = MutableLiveData<ChooseDoctorFormState>()
    val chooseDoctorForm: LiveData<ChooseDoctorFormState> = _chooseDoctorForm

    private val _showProgress = SingleLiveEvent<Boolean>()
    val showProgress: LiveData<Boolean> = _showProgress

    private var chosenPosition: Int? = null
    private var previousEnter = ""

    fun choosePosition(position: Int) {
        chosenPosition = position
    }

    fun nameChanged(newEnter: String) {

        if (newEnter.isBlank()) {
            _chooseDoctorForm.value =
                ChooseDoctorFormState(nameError = R.string.you_should_select_your_doctor)
            return
        }

        // Check if the editText value match one of the search set
        val match = _doctors.value?.any { it.fullName == newEnter } ?: false
        if (match) {
            // enable button
            _chooseDoctorForm.value = ChooseDoctorFormState(isDataValid = true)
            return
        }

        // disable button
        _chooseDoctorForm.value = ChooseDoctorFormState(isDataValid = false)

        // debounce(delay) logic start
        if (newEnter == previousEnter) return
        previousEnter = newEnter

        viewModelScope.launch {
            delay(600)  //debounce timeOut
            if (previousEnter != newEnter)
                return@launch
            // debounce(delay) logic finish

            // delay period completed -> ask for new doctors result set
            _showProgress.value = true
            getDoctorsUC(viewModelScope, mapOf("filter" to newEnter)) {
                it.fold(
                    ::handleFailure,
                    ::handleDoctorsResult
                )
            }
        }
    }

    fun saveDoctor() {
        chosenPosition ?: return
        val doctor = _doctors.value?.get(chosenPosition!!) ?: return
        // todo logic for saving a doctor


    }

    private fun handleDoctorsResult(list: List<DoctorEntity>) {
        _showProgress.value = false
        _doctors.value = list.filter { it.fullName.isNotBlank() }
    }
}