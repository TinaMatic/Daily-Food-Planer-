package com.example.dailyfoodplanner.ui.notes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dailyfoodplanner.data.FirebaseRepositoryNotes
import com.example.dailyfoodplanner.model.CheckedNotes
import com.example.dailyfoodplanner.model.Notes
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NotesViewModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var firebaseRepositoryNotes: FirebaseRepositoryNotes

    var notesLiveData: MutableLiveData<List<Notes>> = MutableLiveData()
    var notesError: MutableLiveData<Boolean> = MutableLiveData()
    var notesLoading: MutableLiveData<Boolean> = MutableLiveData()

    private var compositeDisposable = CompositeDisposable()

    fun getAllNotes(){
        notesLoading.value = true

        compositeDisposable.add(
            firebaseRepositoryNotes.getAllNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    notesError.value = false
                    notesLiveData.postValue(it)
                    notesLoading.value = false
                },{
                    notesError.value = true
                    notesLoading.value = false
                })
        )
    }

    fun addNote(note: Notes): Observable<Boolean>{
        return firebaseRepositoryNotes.addNotes(note)
    }

    fun deleteNote(listOfNotes: List<CheckedNotes>): Observable<Boolean>{
        return firebaseRepositoryNotes.deleteNotes(listOfNotes)
    }

    fun editNote(note: Notes){
        if(note.note.isNotEmpty()){
            firebaseRepositoryNotes.editNote(note)
        }
    }

    fun clear(){
        compositeDisposable.clear()
    }
}