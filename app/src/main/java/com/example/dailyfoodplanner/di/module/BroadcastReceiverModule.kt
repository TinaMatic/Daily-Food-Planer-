package com.example.dailyfoodplanner.di.module

import com.example.dailyfoodplanner.notification.AlarmReceiver
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BroadcastReceiverModule {

    @ContributesAndroidInjector
    abstract fun bindAlarmReciver(): AlarmReceiver
}