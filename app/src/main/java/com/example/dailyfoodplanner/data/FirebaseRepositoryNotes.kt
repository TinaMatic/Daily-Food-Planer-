package com.example.dailyfoodplanner.data

import android.util.Log
import com.example.dailyfoodplanner.model.CheckedNotes
import com.example.dailyfoodplanner.model.Notes
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class FirebaseRepositoryNotes @Inject constructor() {

    private val notesDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Notes")

    var compositeDisposable = CompositeDisposable()

    fun writeNotes(notes: Notes): Observable<Boolean> {
        val notesId = notesDatabase.push().key

        return Observable.create<Boolean> { emitter ->
            val tempNotesObject = Notes(notesId, notes.note)

            notesDatabase.child(notesId!!).setValue(tempNotesObject).addOnCompleteListener { task: Task<Void> ->
                if(task.isSuccessful){
                    emitter.onNext(true)
                } else{
                    emitter.onNext(false)
                }
            }
        }
    }

    fun loadAllNotes(): Observable<List<Notes>> {
        return Observable.create<List<Notes>> { emitter ->
            notesDatabase.addValueEventListener(object : ValueEventListener {

                val listNotes = arrayListOf<Notes>()

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    listNotes.clear()
                    val orderSnapshot = dataSnapshot.children

                    for(notes in orderSnapshot){
                        val notesId = notes.key
                        val note = notes.child("note").value.toString()

                        listNotes.add(Notes(notesId, note))
                    }
                    emitter.onNext(listNotes)
                }

                override fun onCancelled(error: DatabaseError) {
                    emitter.onError(error.toException())
                }

            })
        }
    }

    fun deleteNotes(listOfNotes: List<CheckedNotes>): Observable<Boolean>{
        return Observable.create<Boolean> { emitter ->
            listOfNotes.forEach {
                notesDatabase.child(it.notesId!!).removeValue().addOnCompleteListener { task: Task<Void> ->
                    if(task.isSuccessful){
                        emitter.onNext(true)
                    } else{
                        emitter.onNext(false)
                    }
                }
            }
        }
    }

    fun editNote(note: Notes){
        notesDatabase.child(note.notesId!!).setValue(note)
    }

    fun clean(){

    }
}