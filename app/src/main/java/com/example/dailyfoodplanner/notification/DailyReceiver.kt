package com.example.dailyfoodplanner.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.dailyfoodplanner.R

class DailyReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(context != null && intent != null){
            if (intent.action.equals(context.getString(R.string.action_notify_daily_receiver))){
                NotificationDataUtils.scheduleAlarmForToday(context)
            }
        }
    }
}