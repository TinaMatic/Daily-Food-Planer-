package com.example.dailyfoodplanner.data

import android.net.Uri
import androidx.core.net.toUri
import com.example.dailyfoodplanner.constants.Constants.Companion.USERS_DATABASE
import com.example.dailyfoodplanner.constants.Constants.Companion.USERS_PROFILE_PICTURES
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class FirebaseRepositoryUsers @Inject constructor() {

    private val mAuth = FirebaseAuth.getInstance()

    private val usersDatabase = FirebaseDatabase.getInstance().reference

    private val mStorageReference: StorageReference = FirebaseStorage.getInstance().reference

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

    fun getProfileImage(): Observable<Uri>{
        val currentUserId = mAuth.currentUser?.uid

        return Observable.create { emitter ->
            usersDatabase.child(currentUserId!!).child(USERS_DATABASE).child("image")
                .addListenerForSingleValueEvent(object: ValueEventListener{

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val image = dataSnapshot.value.toString().toUri()
                        emitter.onNext(image)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        emitter.onError(error.toException())
                    }
                })
        }
    }

    fun getUserProfileData(): Observable<Pair<String, Uri>>{
        val currentUserId = mAuth.currentUser?.uid

        return Observable.create<Pair<String, Uri>> {emitter ->
            usersDatabase.child(currentUserId!!).child(USERS_DATABASE)
                .addListenerForSingleValueEvent(object: ValueEventListener{

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val username = dataSnapshot.child("username").value.toString()
                        val image = dataSnapshot.child("image").value.toString().toUri()
                        emitter.onNext(Pair(username, image))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        emitter.onError(error.toException())
                    }
                })
        }
    }

    fun loadPicture(resultUri: Uri?): Observable<Boolean>{
        val currentUserId = mAuth.currentUser?.uid

        return Observable.create<Boolean> {emitter ->

            val filePath = mStorageReference.child(USERS_PROFILE_PICTURES)
                .child("$currentUserId.jpg")

            filePath.putFile(resultUri!!).addOnCompleteListener { task: Task<UploadTask.TaskSnapshot> ->
                if(task.isSuccessful){

                    var downloadUrl: Any? = null
                    filePath.downloadUrl.addOnCompleteListener { task: Task<Uri> ->
                        if(task.isSuccessful){
                            downloadUrl = task.result.toString()

                            usersDatabase.child(currentUserId!!).child(USERS_DATABASE).child("image")
                                .setValue(downloadUrl).addOnCompleteListener { task: Task<Void> ->
                                    if(task.isSuccessful){
                                        emitter.onNext(true)
                                    } else{
                                        emitter.onNext(false)
                                    }
                                }
                        }
                    }
                } else{
                    emitter.onNext(false)
                }
            }
        }
    }

}