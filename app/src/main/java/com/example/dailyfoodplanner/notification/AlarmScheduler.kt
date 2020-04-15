package com.example.dailyfoodplanner.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.dailyfoodplanner.R
import com.example.dailyfoodplanner.constants.Constants.Companion.KEY_ID
import com.example.dailyfoodplanner.model.DailyPlaner
import com.example.dailyfoodplanner.utils.DateTimeUtils.Companion.getHourForMeal
import com.example.dailyfoodplanner.utils.DateTimeUtils.Companion.getMinuteForMeail
import java.util.*

class AlarmScheduler {

    companion object{


//        fun createPendingIntent(context: Context, dailyPlaner: DailyPlaner, day: String?): PendingIntent?{
//            val intent = Intent(context.applicationContext, AlarmReceiver::class.java).apply {
//                action = context.getString(R.string.action_notify_daily_planer)
//                type = "${dailyPlaner.date}"
//                putExtra(KEY_ID, dailyPlaner.id)
//            }
//
//            return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//        }


        fun scheduleAlarm(context: Context, dailyPlaner: DailyPlaner, typeOfMeal: String, dayOfWeek: Int){
            val dayOfMonth = dailyPlaner.date.substring(0,2).toInt()
            val month = dailyPlaner.date.substring(3,5).toInt()
            val hour = getHourForMeal(dailyPlaner, typeOfMeal)
            val minute = getMinuteForMeail(dailyPlaner, typeOfMeal)

            //set up the time to schedule the alarm
            val dateTimeAlarm = Calendar.getInstance(Locale.getDefault())
            dateTimeAlarm.timeInMillis = System.currentTimeMillis()
            dateTimeAlarm.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            dateTimeAlarm.set(Calendar.MONTH, month)
            dateTimeAlarm.set(Calendar.HOUR_OF_DAY, hour)
            dateTimeAlarm.set(Calendar.MINUTE, minute)
            dateTimeAlarm.set(Calendar.SECOND, 0)
            dateTimeAlarm.set(Calendar.MILLISECOND, 0)

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)
            val alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

            //compare the dateTimeToAlarm to today
            val today = Calendar.getInstance(Locale.getDefault())
            if(shouldNotifyToday(dayOfWeek, today, dateTimeAlarm)){
                //schedule for today
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, dateTimeAlarm.timeInMillis, alarmIntent)
                return
            }

        }


        private fun shouldNotifyToday(dayOfWeek: Int, today: Calendar, dateTimeToAlarm: Calendar): Boolean{
            return dayOfWeek == today.get(Calendar.DAY_OF_WEEK) &&
                    today.get(Calendar.HOUR_OF_DAY) <= dateTimeToAlarm.get(Calendar.HOUR_OF_DAY) &&
                    today.get(Calendar.MINUTE) <= dateTimeToAlarm.get(Calendar.MINUTE)
        }

        fun cancelAlarm(context: Context){
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            alarmManager.cancel(pendingIntent)
        }

    }
}