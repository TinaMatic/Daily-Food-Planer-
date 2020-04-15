package com.example.dailyfoodplanner.ui.schedule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dailyfoodplanner.data.FirebaseRepositoryDailyPlaner
import com.example.dailyfoodplanner.model.DailyPlaner
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ScheduleViewModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var firebaseRepositoryDailyPlaner: FirebaseRepositoryDailyPlaner

    private var compositeDisposable = CompositeDisposable()

    var dailyPlansForMonthLiveData: MutableLiveData<List<DailyPlaner>> = MutableLiveData()
    var dailyPlansErrorLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun readDailyPlansForMonth(month: Int){

        compositeDisposable.add(firebaseRepositoryDailyPlaner.readDailyPlansForMonth(month)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                val sortedList = it.sortedBy {dailyPlan->
                    dailyPlan.date.substring(0,2).toInt()
                }

                dailyPlansForMonthLiveData.postValue(sortedList)
                dailyPlansErrorLiveData.value = false
            }, {
                dailyPlansErrorLiveData.value = true
            }))
    }


    fun clear(){
        firebaseRepositoryDailyPlaner.compositeDisposable.clear()
        compositeDisposable.clear()
    }

}