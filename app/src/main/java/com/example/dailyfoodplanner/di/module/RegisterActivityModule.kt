package com.example.dailyfoodplanner.di.module

import com.example.dailyfoodplanner.ui.register.RegisterViewModel
import dagger.Module
import dagger.Provides

@Module
class RegisterActivityModule {

    @Provides
    fun provideRegisterViewModel(): RegisterViewModel{
        return RegisterViewModel()
    }

}