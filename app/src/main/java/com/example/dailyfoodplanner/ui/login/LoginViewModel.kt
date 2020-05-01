package com.example.dailyfoodplanner.ui.login

import androidx.lifecycle.ViewModel
import com.example.dailyfoodplanner.data.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Observable
import javax.inject.Inject

class LoginViewModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var firebaseRepository: FirebaseRepository

    fun login(email: String, password: String): Observable<Boolean>{
        return firebaseRepository.loginUser(email, password)
    }

    fun isUserLoggedIn(): Observable<Boolean>{
        return firebaseRepository.isUserLoggedIn()
    }

    fun getFirebaseAuth(): FirebaseAuth{
        return firebaseRepository.mAuth
    }

    fun getAuthListener(): FirebaseAuth.AuthStateListener?{
        return firebaseRepository.mAuthListener
    }


}