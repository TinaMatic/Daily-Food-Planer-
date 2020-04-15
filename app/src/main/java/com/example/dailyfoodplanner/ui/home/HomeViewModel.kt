package com.example.dailyfoodplanner.ui.home

import androidx.lifecycle.ViewModel
import com.example.dailyfoodplanner.data.FirebaseRepositoryDailyPlaner
import com.example.dailyfoodplanner.model.DailyPlaner
import io.reactivex.Observable
import javax.inject.Inject

class HomeViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var firebaseRepositoryDailyPlaner: FirebaseRepositoryDailyPlaner

    fun writeDailyPlaner(dailyPlaner: DailyPlaner): Observable<Boolean>{
        return firebaseRepositoryDailyPlaner.writeDailyPlaner(dailyPlaner)
    }

}