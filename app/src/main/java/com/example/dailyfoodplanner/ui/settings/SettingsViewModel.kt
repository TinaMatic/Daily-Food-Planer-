package com.example.dailyfoodplanner.ui.settings

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dailyfoodplanner.data.FirebaseRepository
import com.example.dailyfoodplanner.data.FirebaseRepositoryUsers
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SettingsViewModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var firebaseRepository: FirebaseRepository

    @Inject
    lateinit var firebaseRepositoryUsers: FirebaseRepositoryUsers

    private var compositeDisposable = CompositeDisposable()

    var userProfileLiveData = MutableLiveData<Pair<String, Uri>>()

    var userProfileLoading = MutableLiveData<Boolean>()

    fun logout(){
        firebaseRepository.logout()
    }

    fun getUserData(){

        userProfileLoading.value = true

        compositeDisposable.add(firebaseRepositoryUsers.getUserProfileData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                userProfileLiveData.postValue(it)
                userProfileLoading.value = false
            },{
                userProfileLoading.value = false
            }))
    }

    fun loadImage(image: Uri): Observable<Boolean>{
        return firebaseRepositoryUsers.loadPicture(image)
    }

    fun clear(){
        compositeDisposable.clear()
    }
}