package com.us.telemedicine.di


import com.us.telemedicine.presentation.HelperFragment
import com.us.telemedicine.presentation.HomeFragment
import com.us.telemedicine.global.BaseFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeBaseFragment(): BaseFragment

    @ContributesAndroidInjector
    abstract fun contributeFlowStepFragment(): HelperFragment
}
