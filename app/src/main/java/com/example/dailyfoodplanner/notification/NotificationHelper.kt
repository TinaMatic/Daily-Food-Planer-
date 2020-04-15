package com.example.dailyfoodplanner.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.dailyfoodplanner.R
import com.example.dailyfoodplanner.constants.Constants.Companion.DAILY_PLANER_REQUEST_CODE
import com.example.dailyfoodplanner.constants.Constants.Companion.KEY_DAILY_PLAN
import com.example.dailyfoodplanner.constants.Constants.Companion.KEY_ID
import com.example.dailyfoodplanner.model.DailyPlaner
import com.example.dailyfoodplanner.ui.main.MainActivity

class NotificationHelper {

    companion object{

        fun createNotification(context: Context){
            val channelId =  "${context.packageName}-DailyPlan"

            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val notificationBuilder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.resources.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_description))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)


            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val channel = NotificationChannel(channelId, "Daily Plan channel", NotificationManager.IMPORTANCE_HIGH)

                channel.description = "Notification channel for daily plan"
                channel.setShowBadge(true)

                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.notify(0, notificationBuilder.build())
        }

    }
}