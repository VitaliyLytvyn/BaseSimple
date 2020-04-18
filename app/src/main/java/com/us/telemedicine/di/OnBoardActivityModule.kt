package com.us.telemedicine.di

import com.us.telemedicine.presentation.onboard.OnBoardActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Suppress("unused")
@Module
abstract class OnBoardActivityModule {
    @ContributesAndroidInjector(modules = [FragmentOnBoardBuildersModule::class])
    abstract fun contributeOnBoardActivityModule(): OnBoardActivity
}