package com.example.dailyfoodplanner.di.component

import android.app.Application
import com.example.dailyfoodplanner.DailyFoodPlannerApplication
import com.example.dailyfoodplanner.di.module.ActivityModule
import com.example.dailyfoodplanner.di.module.ApplicationModule
import com.example.dailyfoodplanner.di.module.FragmentModule
import com.example.dailyfoodplanner.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, ApplicationModule::class, ActivityModule::class,
    ViewModelModule::class, FragmentModule::class])

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