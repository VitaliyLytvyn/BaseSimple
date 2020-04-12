package com.me.basesimple.di


import com.me.basesimple.presentation.FlowStepFragment
import com.me.basesimple.presentation.HomeFragment
import com.me.basesimple.global.BaseFragment
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
    abstract fun contributeFlowStepFragment(): FlowStepFragment
}
