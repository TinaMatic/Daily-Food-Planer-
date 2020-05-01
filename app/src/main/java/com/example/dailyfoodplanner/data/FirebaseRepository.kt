package com.example.dailyfoodplanner.data

import android.util.Log
import com.example.dailyfoodplanner.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Observable
import javax.inject.Inject

class FirebaseRepository @Inject constructor() {

    var mAuth = FirebaseAuth.getInstance()
    var mAuthListener: FirebaseAuth.AuthStateListener? = null
    private var currentUser:  FirebaseUser? = null
    private val mDatabaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun loginUser(email: String, password:String):Observable<Boolean>{
        return Observable.create<Boolean> {emitter ->
            mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if(task.isSuccessful){
                        emitter.onNext(true)
                    } else{
                        emitter.onNext(false)
                    }
                }
        }
    }

    fun createAccount(email: String, password: String, username: String): Observable<Boolean>{
        return Observable.create<Boolean>{emitter ->
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {task: Task<AuthResult> ->
                    if(task.isSuccessful){
                        currentUser = mAuth.currentUser
                        val userId = currentUser!!.uid

                        val currentUserDatabase =  mDatabaseReference.child(userId).child("Users")

                        val user = User(userId, username, email, password)

                        currentUserDatabase.setValue(user).addOnCompleteListener { task: Task<Void> ->
                            if(task.isSuccessful){
                                emitter.onNext(true)
                            } else{
                                Log.d("onError", task.exception.toString())
                                emitter.onError(task.exception!!)
                            }
                        }
                    } else{
                        Log.d("onError", task.exception.toString())
                        emitter.onError(task.exception!!)
                    }
                }
        }
    }

    fun isUserLoggedIn(): Observable<Boolean>{
        return Observable.create<Boolean> {emitter ->
            mAuth = FirebaseAuth.getInstance()
            mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth: FirebaseAuth ->
                val user = firebaseAuth.currentUser

                if(user != null){
                    emitter.onNext(true)
                } else{
                    emitter.onNext(false)
                }
            }
        }
    }

    fun logout(){
        mAuth.signOut()
    }

}