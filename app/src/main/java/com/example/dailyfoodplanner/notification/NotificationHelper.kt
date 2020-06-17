package com.example.dailyfoodplanner.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.dailyfoodplanner.R
import com.example.dailyfoodplanner.ui.main.MainActivity

object NotificationHelper {

    //builds nad returns the NotificationCompt.Builder
    private fun buildNotification(context: Context, mealType: String): NotificationCompat.Builder {
        val channelId = "${context.packageName}-DailyPlan"

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(context.getString(R.string.notification_description))
            .setContentText(mealType.capitalize())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .setContentIntent(pendingIntent)
    }

    //sets up the notification channel
    fun createNotificationChanel(context: Context){
        val channelId = "${context.packageName}-DailyPlan"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Daily Plan channel", NotificationManager.IMPORTANCE_HIGH)

            channel.description = "Notification channel for daily plan"
            channel.setShowBadge(true)

            notificationManager.createNotificationChannel(channel)
        }
    }

    //creates a notification
    fun createNotification(context: Context, mealType: String){
        //create the notification
        val notificationBuilder = buildNotification(context, mealType)

        //call notify for the notification
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(0, notificationBuilder.build())
    }
}