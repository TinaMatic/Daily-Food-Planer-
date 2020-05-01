package com.example.dailyfoodplanner.data

import com.example.dailyfoodplanner.constants.Constants.Companion.USERS_DATABASE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Observable
import javax.inject.Inject

class FirebaseRepositoryUsers @Inject constructor() {

    val mAuth = FirebaseAuth.getInstance()

    val usersDatabase = FirebaseDatabase.getInstance().reference

    fun getUsername(): Observable<String>{
        val currentUserId = mAuth.currentUser?.uid

        return Observable.create<String> {emitter ->
            usersDatabase.child(currentUserId!!).child(USERS_DATABASE).child("username")
                .addListenerForSingleValueEvent(object :ValueEventListener{

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val username = dataSnapshot.value.toString()
                        emitter.onNext(username)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        emitter.onError(error.toException())
                    }
                })
        }
    }
}