package com.example.dailyfoodplanner.data

import android.util.Log
import com.example.dailyfoodplanner.constants.Constants.Companion.NOTES_DATABASE
import com.example.dailyfoodplanner.model.CheckedNotes
import com.example.dailyfoodplanner.model.Notes
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class FirebaseRepositoryNotes @Inject constructor() {

    private val notesDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference

    private val mAuth = FirebaseAuth.getInstance()

    fun writeNotes(notes: Notes): Observable<Boolean> {
        val currentUserId = mAuth.currentUser!!.uid
        val notesId = notesDatabase.push().key

        return Observable.create<Boolean> { emitter ->
            val tempNotesObject = Notes(notesId, notes.note)

            notesDatabase.child(currentUserId).child(NOTES_DATABASE).child(notesId!!)
                .setValue(tempNotesObject).addOnCompleteListener { task: Task<Void> ->
                if(task.isSuccessful){
                    emitter.onNext(true)
                } else{
                    emitter.onNext(false)
                }
            }
        }
    }

    fun loadAllNotes(): Observable<List<Notes>> {
        val currentUserId = mAuth.currentUser!!.uid

        return Observable.create<List<Notes>> { emitter ->
            notesDatabase.child(currentUserId).child(NOTES_DATABASE)
                .addValueEventListener(object : ValueEventListener {

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
        val currentUserId = mAuth.currentUser!!.uid

        return Observable.create<Boolean> { emitter ->
            listOfNotes.forEach {
                notesDatabase.child(currentUserId).child(NOTES_DATABASE).child(it.notesId!!)
                    .removeValue().addOnCompleteListener { task: Task<Void> ->
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
        val currentUserId = mAuth.currentUser!!.uid
        notesDatabase.child(currentUserId).child(NOTES_DATABASE).child(note.notesId!!).setValue(note)
    }
}