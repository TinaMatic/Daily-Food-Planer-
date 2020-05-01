package com.example.dailyfoodplanner.ui.settings

import androidx.lifecycle.ViewModel
import com.example.dailyfoodplanner.data.FirebaseRepository
import com.example.dailyfoodplanner.data.FirebaseRepositoryUsers
import io.reactivex.Observable
import javax.inject.Inject

class SettingsViewModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var firebaseRepository: FirebaseRepository

    @Inject
    lateinit var firebaseRepositoryUsers: FirebaseRepositoryUsers

    fun logout(){
        firebaseRepository.logout()
    }

    fun getUsername(): Observable<String>{
        return firebaseRepositoryUsers.getUsername()
    }
}