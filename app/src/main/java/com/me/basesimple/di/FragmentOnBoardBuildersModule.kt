package com.me.basesimple.di

import com.me.basesimple.presentation.onboard.AuthFragment
import com.me.basesimple.global.BaseFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Suppress("unused")
@Module
abstract class FragmentOnBoardBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeAuthFragment(): AuthFragment

    @ContributesAndroidInjector
    abstract fun contributeBaseFragment(): BaseFragment
}