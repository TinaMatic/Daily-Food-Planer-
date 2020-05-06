package com.example.dailyfoodplanner.di.module

import android.content.Context
import android.content.SharedPreferences
import com.example.dailyfoodplanner.constants.Constants.Companion.SHARED_PREFERENCES
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule (private val context: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context{
        return context
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences{
        return context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }
}