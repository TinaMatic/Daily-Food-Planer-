package com.example.dailyfoodplanner.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.dailyfoodplanner.R

/**
 * This is the receiver that happens every day at midnight to check if there are new alarms
 * to be scheduled for today
 */
class DailyReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(context != null && intent != null){
            if (intent.action.equals(context.getString(R.string.action_notify_daily_receiver))){
                NotificationDataUtils.scheduleAlarmForToday(context)
            }
        }
    }
}