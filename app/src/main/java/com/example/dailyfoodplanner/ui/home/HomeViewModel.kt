package com.example.dailyfoodplanner.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dailyfoodplanner.data.FirebaseRepositoryDailyPlaner
import com.example.dailyfoodplanner.data.FirebaseRepositoryRecipes
import com.example.dailyfoodplanner.model.DailyPlaner
import com.example.dailyfoodplanner.model.Recipes
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var firebaseRepositoryDailyPlaner: FirebaseRepositoryDailyPlaner

    @Inject
    lateinit var firebaseRepositoryRecipes: FirebaseRepositoryRecipes

    private var compositeDisposable = CompositeDisposable()

    var dailyPlanLiveData = MutableLiveData<DailyPlaner>()
    var recipeLiveData = MutableLiveData<List<Recipes>>()
    var recipeLoading = MutableLiveData<Boolean>()

    fun readSingleDailyPlan(dailyPlanId: String){
        compositeDisposable.add(firebaseRepositoryDailyPlaner.readSingleDailyPlan(dailyPlanId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                dailyPlanLiveData.postValue(it)
            },{}))
    }
    fun addDailyPlaner(dailyPlaner: DailyPlaner): Observable<Pair<Boolean, DailyPlaner?>>{
        return Observable.create<Pair<Boolean, DailyPlaner?>> { emitter ->
            firebaseRepositoryDailyPlaner.readAllDailyPlans()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    //check if the daily plan with chosen date already exists
                    val doesExists = it.any { dailyPlanList ->
                        dailyPlanList.date.equals(dailyPlaner.date)
                    }

                    if (doesExists) {
                        emitter.onNext(Pair(false, dailyPlaner))
                    } else {
                        firebaseRepositoryDailyPlaner.writeDailyPlaner(dailyPlaner)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                emitter.onNext(it)
                            }
                    }
                }
        }
    }

    fun editDailyPlan(dailyPlaner: DailyPlaner): Observable<Boolean>{
        return firebaseRepositoryDailyPlaner.editDailyPlan(dailyPlaner)
    }

    fun loadAllRecipes(){
        recipeLoading.value = true

        compositeDisposable.add(firebaseRepositoryRecipes.loadAllRecipes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                recipeLiveData.postValue(it)
                recipeLoading.value = false
        },{
                recipeLoading.value = false
            }))
    }

    fun claer(){
        compositeDisposable.clear()
        firebaseRepositoryDailyPlaner.clean()
    }

}