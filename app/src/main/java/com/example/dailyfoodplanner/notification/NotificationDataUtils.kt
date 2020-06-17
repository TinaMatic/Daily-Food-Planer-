package com.example.dailyfoodplanner.notification

import android.content.Context
import com.example.dailyfoodplanner.data.FirebaseRepositoryDailyPlaner
import com.example.dailyfoodplanner.model.DailyPlaner
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

object NotificationDataUtils {

    var firebaseRepositoryDailyPlaner = FirebaseRepositoryDailyPlaner()

    private var compositeDisposable = CompositeDisposable()


    //schedules alarms for data in the local database
    fun scheduleAlarmsForData(context: Context) {
        compositeDisposable.add(loadAllDailyPlaners()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                for(dailyPlan in it){
                    AlarmScheduler.scheduleAlarmForDailyPlaner(context, dailyPlan)
                }
            })
    }

    //load all data from the database to schedule the alarms
    fun loadAllDailyPlaners(): Observable<List<DailyPlaner>>  {
        return firebaseRepositoryDailyPlaner.getAllDailyPlans()
    }

    fun isToday(date: String): Boolean{
        val dayOfMonth = date.substring(0, 2).toInt()
        val month = date.substring(3, 5).toInt() - 1
        val year = date.substring(6).toInt()

        val today = Calendar.getInstance()

        return today.get(Calendar.YEAR).equals(year) && today.get(Calendar.MONTH).equals(month) &&
                today.get(Calendar.DAY_OF_MONTH).equals(dayOfMonth)
    }

    //schedules the alarm or today
    fun scheduleAlarmForToday(context: Context){
        val today = Calendar.getInstance()
        val thisMonth = today.get(Calendar.MONTH) + 1

        compositeDisposable.add(
            firebaseRepositoryDailyPlaner.getDailyPlansForMonth(thisMonth)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {dailyPlanerList->

                var hasToday = false
                val sortedList = dailyPlanerList.sortedBy {
                    it.date.substring(0,2).toInt()
                }

                //if there is schedule for today set the alarms
                for(dailyPlan in sortedList){
                    if(isToday(dailyPlan.date)){
                        AlarmScheduler.scheduleAlarmForDailyPlaner(context, dailyPlan)
                        hasToday = true
                        break
                    }
                }

                //if there is no alarm for today schedule the receiver to run on midnight the nextDay
                if(!hasToday){
                    AlarmScheduler.scheduleDailyCheckup(context)
                }
            })
    }

    fun getSingleDailyPlanById(dailyPlanId: String): Observable<DailyPlaner>{
        return firebaseRepositoryDailyPlaner.getSingleDailyPlan(dailyPlanId)
    }

    fun clear(){
        compositeDisposable.clear()
    }

}