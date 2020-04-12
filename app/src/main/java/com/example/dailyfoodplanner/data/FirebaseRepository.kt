package com.example.dailyfoodplanner.data

import com.example.dailyfoodplanner.model.DailyPlaner
import com.example.dailyfoodplanner.model.Notes
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import io.reactivex.Observable
import javax.inject.Inject

class FirebaseRepository @Inject constructor() {

    val dailyPlanerDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference.child("DailyPlaner")
    val notesDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Notes")

    fun writeDailyPlaner(dailyPlaner: DailyPlaner): Observable<Boolean>{
        val messageId = dailyPlanerDatabase.push().key

        return Observable.create<Boolean> {emitter ->
            val tempDailyPlanerObject = DailyPlaner(messageId!!, dailyPlaner.date, dailyPlaner.timeBreakfast, dailyPlaner.recipeBreakfast,
                dailyPlaner.timeSnack1, dailyPlaner.recipeSnack1, dailyPlaner.timeLunch, dailyPlaner.recipeLunch,
                dailyPlaner.timeSnack2, dailyPlaner.recipeSnack2, dailyPlaner.timeDinner, dailyPlaner.recipeDinner)

            dailyPlanerDatabase.child(messageId).setValue(tempDailyPlanerObject).addOnCompleteListener {task: Task<Void> ->
                if (task.isSuccessful){
                    emitter.onNext(true)
                }else{
                    emitter.onNext(false)
                }
            }
        }
    }

    fun writeNotes(notes: Notes): Observable<Boolean>{
        val notesId = notesDatabase.push().key

        return Observable.create<Boolean> {emitter ->
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

    fun loadAllNotes(): Observable<List<Notes>>{
        return Observable.create<List<Notes>> {emitter ->
            notesDatabase.addValueEventListener(object : ValueEventListener{

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
                    emitter.onNext(listOf())
                }

            })
        }
    }
}