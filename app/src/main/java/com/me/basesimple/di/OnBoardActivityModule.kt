package com.me.basesimple.di

import com.me.basesimple.presentation.onboard.OnBoardActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Suppress("unused")
@Module
abstract class OnBoardActivityModule {
    @ContributesAndroidInjector(modules = [FragmentOnBoardBuildersModule::class])
    abstract fun contributeOnBoardActivityModule(): OnBoardActivity
}