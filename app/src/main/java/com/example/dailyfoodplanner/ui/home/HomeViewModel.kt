package com.example.dailyfoodplanner.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dailyfoodplanner.data.FirebaseRepository
import com.example.dailyfoodplanner.model.DailyPlaner
import io.reactivex.Observable
import javax.inject.Inject

class HomeViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var firebaseRepository: FirebaseRepository

    fun writeDailyPlaner(dailyPlaner: DailyPlaner): Observable<Boolean>{
        return firebaseRepository.writeDailyPlaner(dailyPlaner)
    }

}