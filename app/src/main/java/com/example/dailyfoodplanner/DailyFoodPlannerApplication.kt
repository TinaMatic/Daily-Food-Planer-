package com.example.dailyfoodplanner

import com.example.dailyfoodplanner.di.component.ApplicationComponent
import com.example.dailyfoodplanner.di.component.DaggerApplicationComponent
import com.example.dailyfoodplanner.di.module.ApplicationModule
import com.example.dailyfoodplanner.notification.NotificationHelper
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class DailyFoodPlannerApplication: DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent: ApplicationComponent = DaggerApplicationComponent.builder()
            .application(this)
            .applicationModule(ApplicationModule(applicationContext))
            .build()

        //inject application instance
        appComponent.inject(this)

        return appComponent
    }

    override fun onCreate() {
        super.onCreate()

        NotificationHelper.createNotificationChanel(this)
    }
}