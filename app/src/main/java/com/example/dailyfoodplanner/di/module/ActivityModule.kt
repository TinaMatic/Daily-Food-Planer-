package com.example.dailyfoodplanner.di.module

import com.example.dailyfoodplanner.ui.login.LoginActivity
import com.example.dailyfoodplanner.ui.main.MainActivity
import com.example.dailyfoodplanner.ui.register.RegisterActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector()
    abstract fun bindRegisterActivity(): RegisterActivity


}