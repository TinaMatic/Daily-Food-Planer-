package com.example.dailyfoodplanner.ui.notes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dailyfoodplanner.data.FirebaseRepository
import com.example.dailyfoodplanner.model.Notes
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NotesViewModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var firebaseRepository: FirebaseRepository

    var notesLiveData: MutableLiveData<List<Notes>> = MutableLiveData()
    var notesError: MutableLiveData<Boolean> = MutableLiveData()
    var notesLoading: MutableLiveData<Boolean> = MutableLiveData()

    private var compositeDisposable = CompositeDisposable()

    fun loadAllNotes(){
        notesLoading.value = true


        compositeDisposable.add(
            firebaseRepository.loadAllNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    notesError.value = false
                    notesLiveData.postValue(it)
                },{
                    notesError.value = true
                    notesLoading.value = false
                },{
                    notesError.value = false
                    notesLoading.value = false

                })
        )
    }

    fun writeNote(note: Notes): Observable<Boolean>{
        return firebaseRepository.writeNotes(note)
    }

    fun clear(){
        compositeDisposable.clear()
    }
}