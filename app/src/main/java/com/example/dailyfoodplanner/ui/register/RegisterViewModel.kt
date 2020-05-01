package com.example.dailyfoodplanner.ui.register

import androidx.lifecycle.ViewModel
import com.example.dailyfoodplanner.data.FirebaseRepository
import io.reactivex.Observable
import javax.inject.Inject

class RegisterViewModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var firebaseRepository: FirebaseRepository

    fun registerUser(email: String, password: String, username: String): Observable<Boolean>{
        return firebaseRepository.createAccount(email, password, username)
    }
}