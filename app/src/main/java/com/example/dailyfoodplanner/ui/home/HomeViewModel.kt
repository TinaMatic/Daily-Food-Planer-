package com.example.dailyfoodplanner.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dailyfoodplanner.data.FirebaseRepositoryDailyPlaner
import com.example.dailyfoodplanner.model.DailyPlaner
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var firebaseRepositoryDailyPlaner: FirebaseRepositoryDailyPlaner

    private var compositeDisposable = CompositeDisposable()

    var dailyPlanLiveData = MutableLiveData<DailyPlaner>()

    fun readSingleDailyPlan(dailyPlanId: String){
        compositeDisposable.add(firebaseRepositoryDailyPlaner.readSingleDailyPlan(dailyPlanId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                dailyPlanLiveData.postValue(it)
            },{}))
    }

    fun writeDailyPlaner(dailyPlaner: DailyPlaner): Observable<Boolean>{
        return firebaseRepositoryDailyPlaner.writeDailyPlaner(dailyPlaner)
    }

    fun editDailyPlan(dailyPlaner: DailyPlaner): Observable<Boolean>{
        return firebaseRepositoryDailyPlaner.editDailyPlan(dailyPlaner)
    }

    fun claer(){
        compositeDisposable.clear()
        firebaseRepositoryDailyPlaner.clean()
    }

}