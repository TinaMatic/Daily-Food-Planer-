package com.example.dailyfoodplanner.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Vibrator
import android.util.Log
import com.example.dailyfoodplanner.R
import com.example.dailyfoodplanner.constants.Constants.Companion.KEY_ID
import com.example.dailyfoodplanner.constants.Constants.Companion.MEAL_TYPE
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AlarmReceiver: BroadcastReceiver() {
    private val TAG = AlarmReceiver::class.java.simpleName

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive() called with context = [$context], intent =[$intent] ")

        if(context != null && intent != null && intent.action != null){
            if(intent.action.equals(context.getString(R.string.action_notify_daily_planer))){
                if(intent.extras != null){
                    val dailyPlanId = intent.extras?.getString(KEY_ID)
                    Log.d("dailyPlanId", dailyPlanId)

                    val mealType = intent.extras?.getString(MEAL_TYPE)

                    NotificationHelper.createNotification(context, mealType!!)
                    val vibrate = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    vibrate.vibrate(2000L)

                }
            }
        }
    }
}