package com.example.dailyfoodplanner.data

import android.util.Log
import com.example.dailyfoodplanner.model.DailyPlaner
import com.example.dailyfoodplanner.model.Notes
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class FirebaseRepositoryDailyPlaner @Inject constructor() {

    private val dailyPlanerDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference.child("DailyPlaner")

    var compositeDisposable = CompositeDisposable()

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

    fun readAllDailyPlans(): Observable<List<DailyPlaner>>{
        return Observable.create<List<DailyPlaner>> {emitter ->
            val listOfDailyPlans = arrayListOf<DailyPlaner>()

            dailyPlanerDatabase.addListenerForSingleValueEvent(object : ValueEventListener{

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val orderSnapshot = dataSnapshot.children

                    for (dailyPlan in orderSnapshot){
                        val key = dailyPlan.key
                        val date = dailyPlan.child("date").value.toString()
                        val timeBreakfast = dailyPlan.child("timeBreakfast").value.toString()
                        val recipeBreakfast = dailyPlan.child("recipeBreakfast").value.toString()
                        val timeSnack1 = dailyPlan.child("timeSnack1").value.toString()
                        val recipeSnack1 = dailyPlan.child("recipeSnack1").value.toString()
                        val timeLunch = dailyPlan.child("timeLunch").value.toString()
                        val recipeLunch = dailyPlan.child("recipeLunch").value.toString()
                        val timeSnack2 = dailyPlan.child("timeSnack2").value.toString()
                        val recipeSnack2 = dailyPlan.child("recipeSnack2").value.toString()
                        val timeDinner = dailyPlan.child("timeDinner").value.toString()
                        val recipeDinner = dailyPlan.child("recipeDinner").value.toString()

                        listOfDailyPlans.add(DailyPlaner(key, date, timeBreakfast, recipeBreakfast,
                            timeSnack1, recipeSnack1, timeLunch, recipeLunch,
                            timeSnack2, recipeSnack2, timeDinner, recipeDinner))
                    }

                    emitter.onNext(listOfDailyPlans)
                }

                override fun onCancelled(error: DatabaseError) {
                    emitter.onError(error.toException())
                }

            })
        }
    }

    fun readSingleDailyPlan(dailyPlanId: String):Observable<DailyPlaner>{
        return Observable.create<DailyPlaner> {emitter ->

            dailyPlanerDatabase.child(dailyPlanId).addListenerForSingleValueEvent(object : ValueEventListener{

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val id = dataSnapshot.key
                    val date = dataSnapshot.child("date").value.toString()
                    val timeBreakfast = dataSnapshot.child("timeBreakfast").value.toString()
                    val recipeBreakfast = dataSnapshot.child("recipeBreakfast").value.toString()
                    val timeSnack1 = dataSnapshot.child("timeSnack1").value.toString()
                    val recipeSnack1 = dataSnapshot.child("recipeSnack1").value.toString()
                    val timeLunch = dataSnapshot.child("timeLunch").value.toString()
                    val recipeLunch = dataSnapshot.child("recipeLunch").value.toString()
                    val timeSnack2 = dataSnapshot.child("timeSnack2").value.toString()
                    val recipeSnack2 = dataSnapshot.child("recipeSnack2").value.toString()
                    val timeDinner = dataSnapshot.child("timeDinner").value.toString()
                    val recipeDinner = dataSnapshot.child("recipeDinner").value.toString()

                    emitter.onNext(DailyPlaner(id, date, timeBreakfast, recipeBreakfast,
                        timeSnack1, recipeSnack1, timeLunch, recipeLunch, timeSnack2, recipeSnack2,
                        timeDinner, recipeDinner))

                }

                override fun onCancelled(error: DatabaseError) {
                    emitter.onError(error.toException())
                }

            })
        }
    }

    fun readDailyPlansForMonth(month: Int): Observable<List<DailyPlaner>>{
        return Observable.create<List<DailyPlaner>> {emitter ->
            val listOfDailyPlans = arrayListOf<DailyPlaner>()

            compositeDisposable.add(readAllDailyPlans().subscribe ({dailyPlans->
                dailyPlans.forEach {
                    if(it.date.substring(3,5).toInt().equals(month)){
                        listOfDailyPlans.add(it)
                    }
                }

                emitter.onNext(listOfDailyPlans)
            },{
                Log.d("readDailyPlansForMonth", it.toString())
            }))
        }
    }

    fun editDailyPlan(dailyPlaner: DailyPlaner): Observable<Boolean>{
        return Observable.create<Boolean> {emitter ->
            dailyPlanerDatabase.child(dailyPlaner.id!!).setValue(dailyPlaner).addOnCompleteListener { task: Task<Void> ->
                if(task.isSuccessful){
                    emitter.onNext(true)
                } else{
                    emitter.onNext(false)
                }
            }
        }

    }

    fun deleteDailyPlan(dailyPlanId: String): Observable<Boolean>{
        return Observable.create<Boolean> {emitter ->
            dailyPlanerDatabase.child(dailyPlanId).removeValue().addOnCompleteListener { task: Task<Void> ->
                if(task.isSuccessful){
                    emitter.onNext(true)
                } else{
                    emitter.onNext(false)
                }
            }
        }
    }

    fun clean(){
        compositeDisposable.clear()
    }
}