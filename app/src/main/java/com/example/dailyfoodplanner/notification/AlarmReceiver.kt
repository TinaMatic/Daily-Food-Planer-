package com.example.dailyfoodplanner.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Vibrator
import android.util.Log
import com.example.dailyfoodplanner.R
import com.example.dailyfoodplanner.constants.Constants.Companion.KEY_ID
import com.example.dailyfoodplanner.constants.Constants.Companion.MEAL_TYPE
import com.example.dailyfoodplanner.constants.Constants.Companion.PUSH_NOTIFICATIONS_ENABLED
import dagger.android.AndroidInjection
import dagger.android.DaggerBroadcastReceiver
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject


class AlarmReceiver: BroadcastReceiver() {

    private val TAG = AlarmReceiver::class.java.simpleName

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onReceive(context: Context?, intent: Intent?) {
        AndroidInjection.inject(this, context)
        Log.d(TAG, "onReceive() called with context = [$context], intent =[$intent] ")

        if(context != null && intent != null && intent.action != null){
            if(intent.action.equals(context.getString(R.string.action_notify_daily_planer))){
                if(intent.extras != null){
                    //check if notifications are turned ON
                    if(sharedPreferences.getBoolean(PUSH_NOTIFICATIONS_ENABLED, true)){
                        val dailyPlanId = intent.extras?.getString(KEY_ID)
                        Log.d("dailyPlanId", dailyPlanId)

                        val mealType = intent.extras?.getString(MEAL_TYPE)

                        NotificationHelper.createNotification(context, mealType!!)
                        val vibrate = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                        vibrate.vibrate(2000L)

                        //schedule the receiver for midnight the next day
                        AlarmScheduler.scheduleDailyCheckup(context)
                    }
                }
            }
        }
    }
}