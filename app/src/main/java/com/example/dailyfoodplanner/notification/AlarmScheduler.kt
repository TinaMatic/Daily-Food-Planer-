package com.example.dailyfoodplanner.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.dailyfoodplanner.R
import com.example.dailyfoodplanner.constants.Constants.Companion.BREAKFAST_REQUEST_CODE
import com.example.dailyfoodplanner.constants.Constants.Companion.DAILY_RECEIVER_REQUEST_CODE
import com.example.dailyfoodplanner.constants.Constants.Companion.DINNER_REQUEST_CODE
import com.example.dailyfoodplanner.constants.Constants.Companion.KEY_ID
import com.example.dailyfoodplanner.constants.Constants.Companion.LUNCH_REQUEST_CODE
import com.example.dailyfoodplanner.constants.Constants.Companion.MEAL_TYPE
import com.example.dailyfoodplanner.constants.Constants.Companion.SNACK1_REQUEST_CODE
import com.example.dailyfoodplanner.constants.Constants.Companion.SNACK2_REQUEST_CODE
import com.example.dailyfoodplanner.model.DailyPlaner
import com.example.dailyfoodplanner.utils.DateTimeUtils
import com.example.dailyfoodplanner.utils.DateTimeUtils.Companion.getHourForMeal
import com.example.dailyfoodplanner.utils.DateTimeUtils.Companion.getMinuteForMeal
import java.util.*

object AlarmScheduler {

//    var alarmCount = 0

    //schedules all the alarms for DailyPlaner
    fun scheduleAlarmForDailyPlaner(context: Context, dailyPlaner: DailyPlaner){

        //get the AlarmManager reference
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        //Schedule the alarm based on the days
        val dayOfWeek = DateTimeUtils.convertDateToCalendarObject(dailyPlaner.date).get(Calendar.DAY_OF_WEEK)

        //get the PendingIntent for the alarm
        val alarmIntentBreakfast = createPendingIntent(context, dailyPlaner, "breakfast", BREAKFAST_REQUEST_CODE)
        val alarmIntentSnack1 = createPendingIntent(context, dailyPlaner, "snack1", SNACK1_REQUEST_CODE)
        val alarmIntentLunch = createPendingIntent(context, dailyPlaner, "lunch", LUNCH_REQUEST_CODE)
        val alarmIntentSnack2 = createPendingIntent(context, dailyPlaner, "snack2", SNACK2_REQUEST_CODE)
        val alarmIntentDinner = createPendingIntent(context, dailyPlaner, "dinner", DINNER_REQUEST_CODE)

        //schedule the alarm
        scheduleAlarm(dailyPlaner, "breakfast", dayOfWeek, alarmIntentBreakfast, alarmManager)
        scheduleAlarm(dailyPlaner, "snack1", dayOfWeek, alarmIntentSnack1, alarmManager)
        scheduleAlarm(dailyPlaner, "lunch", dayOfWeek, alarmIntentLunch, alarmManager)
        scheduleAlarm(dailyPlaner, "snack2", dayOfWeek, alarmIntentSnack2, alarmManager)
        scheduleAlarm(dailyPlaner, "dinner", dayOfWeek, alarmIntentDinner, alarmManager)

    }

    //schedules a single alarm
    private fun scheduleAlarm(dailyPlaner: DailyPlaner, typeOfMeal: String, dayOfWeek: Int,
                      alarmIntent: PendingIntent?, alarmManager: AlarmManager) {

        val dayOfMonth = dailyPlaner.date.substring(0, 2).toInt()
        val month = dailyPlaner.date.substring(3, 5).toInt()
        val year = dailyPlaner.date.substring(6).toInt()
        val hour = getHourForMeal(dailyPlaner, typeOfMeal)
        val minute = getMinuteForMeal(dailyPlaner, typeOfMeal)

        //set up the time to schedule the alarm
        val dateTimeAlarm = Calendar.getInstance()
        dateTimeAlarm.set(Calendar.YEAR, year)
        dateTimeAlarm.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        dateTimeAlarm.set(Calendar.MONTH, month - 1)
        dateTimeAlarm.set(Calendar.HOUR_OF_DAY, hour)
        dateTimeAlarm.set(Calendar.MINUTE, minute)
        dateTimeAlarm.set(Calendar.SECOND, 0)
        dateTimeAlarm.set(Calendar.MILLISECOND, 0)
        dateTimeAlarm.set(Calendar.DAY_OF_WEEK, dayOfWeek)

        //compare the dateTimeToAlarm to today
        val today = Calendar.getInstance(Locale.getDefault())
        if (shouldNotifyToday(dayOfWeek, today, dateTimeAlarm)) {
            //schedule for today
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, dateTimeAlarm.timeInMillis, alarmIntent)
        }
    }

    //creates a PendingIntent for the Alarm
    private fun createPendingIntent(context: Context, dailyPlaner: DailyPlaner, mealType: String?, requestCode: Int): PendingIntent?{
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java)
        intent.action = context.getString(R.string.action_notify_daily_planer)
        intent.putExtra(KEY_ID, dailyPlaner.id)
        intent.putExtra(MEAL_TYPE, mealType)

        return PendingIntent.getBroadcast(context.applicationContext, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }


    //determines if the alarm should be scheduled today
    private fun shouldNotifyToday(dayOfWeek: Int, today: Calendar, dateTimeToAlarm: Calendar): Boolean {
        return dayOfWeek == today.get(Calendar.DAY_OF_WEEK) &&
                today.get(Calendar.HOUR_OF_DAY) <= dateTimeToAlarm.get(Calendar.HOUR_OF_DAY) &&
                today.get(Calendar.MINUTE) <= dateTimeToAlarm.get(Calendar.MINUTE)
    }


    //removes the notification if it was previously scheduled
    fun removeAlarmsForDailyPlaner(context: Context, dailyPlanerId: String){
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java)
        intent.action = context.getString(R.string.action_notify_daily_planer)
        intent.putExtra(KEY_ID, dailyPlanerId)

        //schedule the alarms based on the days
        val alarmIntentBreakfast = PendingIntent.getBroadcast(context, BREAKFAST_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmIntentSnack1 = PendingIntent.getBroadcast(context, SNACK1_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmIntentLunch = PendingIntent.getBroadcast(context, LUNCH_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmIntentSnack2 = PendingIntent.getBroadcast(context, SNACK2_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmIntentDinner = PendingIntent.getBroadcast(context, DINNER_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)


        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(alarmIntentBreakfast)
        alarmManager.cancel(alarmIntentSnack1)
        alarmManager.cancel(alarmIntentLunch)
        alarmManager.cancel(alarmIntentSnack2)
        alarmManager.cancel(alarmIntentDinner)

    }

    //schedules the receiver to run at the beginning of each day
    fun scheduleDailyCheckup(context: Context){
        //creating pending intent
        val intent = Intent(context.applicationContext, DailyReceiver::class.java)
        intent.action = context.getString(R.string.action_notify_daily_receiver)

        val alarmIntent = PendingIntent.getBroadcast(context.applicationContext, DAILY_RECEIVER_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        //get the time when to fire the receiver
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)

        //create alarm manager
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmIntent)
    }

    fun removeDailyCheckup(context: Context){
        val intent = Intent(context.applicationContext, DailyReceiver::class.java)
        intent.action = context.getString(R.string.action_notify_daily_receiver)

        val alarmIntent = PendingIntent.getBroadcast(context.applicationContext, DAILY_RECEIVER_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(alarmIntent)
    }
}