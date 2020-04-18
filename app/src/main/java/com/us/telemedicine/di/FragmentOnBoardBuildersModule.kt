package com.us.telemedicine.di

import com.us.telemedicine.presentation.onboard.SignInFragment
import com.us.telemedicine.global.BaseFragment
import com.us.telemedicine.presentation.onboard.ForgotPasswordFragment
import com.us.telemedicine.presentation.onboard.SignUpFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Suppress("unused")
@Module
abstract class FragmentOnBoardBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeAuthFragment(): SignInFragment

    @ContributesAndroidInjector
    abstract fun contributeSignUpFragment(): SignUpFragment

    @ContributesAndroidInjector
    abstract fun contributeForgotPasswordFragment(): ForgotPasswordFragment

    @ContributesAndroidInjector
    abstract fun contributeBaseFragment(): BaseFragment
}