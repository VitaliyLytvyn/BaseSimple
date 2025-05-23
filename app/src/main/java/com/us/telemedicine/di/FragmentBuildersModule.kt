package com.us.telemedicine.di

import com.us.telemedicine.presentation.HelperFragment
import com.us.telemedicine.presentation.HomePatientFragment
import com.us.telemedicine.global.BaseFragment
import com.us.telemedicine.presentation.ChooseDoctorFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomePatientFragment

    @ContributesAndroidInjector
    abstract fun contributeBaseFragment(): BaseFragment

    @ContributesAndroidInjector
    abstract fun contributeFlowStepFragment(): HelperFragment

    @ContributesAndroidInjector
    abstract fun contributeChooseDoctorFragment(): ChooseDoctorFragment
}
