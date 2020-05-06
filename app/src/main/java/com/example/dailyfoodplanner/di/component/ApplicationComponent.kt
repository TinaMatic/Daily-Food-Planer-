package com.example.dailyfoodplanner.di.component

import android.app.Application
import android.content.SharedPreferences
import com.example.dailyfoodplanner.DailyFoodPlannerApplication
import com.example.dailyfoodplanner.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, ApplicationModule::class, ActivityModule::class,
    ViewModelModule::class, FragmentModule::class, BroadcastReceiverModule::class])

interface ApplicationComponent: AndroidInjector<DailyFoodPlannerApplication> {

    override fun inject(dailyFoodPlannerApplication: DailyFoodPlannerApplication)

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun application(application: Application): Builder
        fun applicationModule(applicationModule: ApplicationModule): Builder
        fun build(): ApplicationComponent
    }
}