package com.example.dailyfoodplanner.notification

import android.content.Context
import com.example.dailyfoodplanner.data.FirebaseRepositoryDailyPlaner
import com.example.dailyfoodplanner.model.DailyPlaner
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

object NotificationDataUtils {

    var firebaseRepositoryDailyPlaner = FirebaseRepositoryDailyPlaner()

    var compositeDisposable = CompositeDisposable()


    //schedules alarms for data in the local database
    fun scheduleAlarmsForData(context: Context) {
        val dailyPlanerList = loadAllDailyPlaners()
        for (dailyPlan in dailyPlanerList){
            AlarmScheduler.scheduleAlarmForDailyPlaner(context, dailyPlan)
        }
    }

    //deletes alarms for all the data
    fun removeAlaramsForData(context: Context){
        val dailyPlanerList = loadAllDailyPlaners()
        for (dailyPlan in dailyPlanerList){
            AlarmScheduler.removeAlarmsForDailyPlaner(context, dailyPlan.id!!)
        }
    }

    //load all data from the database to schedule the alarms
    fun loadAllDailyPlaners(): List<DailyPlaner> {
        val dailyPlanerList = arrayListOf<DailyPlaner>()
        compositeDisposable.add(firebaseRepositoryDailyPlaner.readAllDailyPlans()
            .subscribe {
                dailyPlanerList.addAll(it)
            })

        return dailyPlanerList
    }

    fun getSingleDailyPlanById(dailyPlanId: String): DailyPlaner?{
        var dailyPlan: DailyPlaner? =
            DailyPlaner(null, "24/04/2020", "13:00", "None",
            "15:00", "None", "15:00", "None", "15:00", "None",
            "15:00", "None")

//        compositeDisposable.add(firebaseRepositoryDailyPlaner.readSingleDailyPlan(dailyPlanId)
//            .subscribe {
//                //                dailyPlan = it
//                return@subscribe it
//            })

        return dailyPlan
    }

}