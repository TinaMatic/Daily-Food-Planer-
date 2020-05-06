package com.example.dailyfoodplanner.data

import com.example.dailyfoodplanner.constants.Constants.Companion.RECIPES_DATABASE
import com.example.dailyfoodplanner.model.Recipes
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.reactivex.Observable
import javax.inject.Inject

class FirebaseRepositoryRecipes @Inject constructor() {

    private var recipesDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference

    private val mAuth = FirebaseAuth.getInstance()

    fun writeRecipes(recipes: Recipes): Observable<Boolean>{
        val currentUserId = mAuth.currentUser!!.uid
        val recipeId = recipesDatabase.push().key

        return Observable.create<Boolean> {emitter ->
            val tempRecipeObject = Recipes(recipeId, recipes.title, recipes.description, recipes.ingredients)
            recipesDatabase.child(currentUserId).child(RECIPES_DATABASE).child(recipeId!!)
                .setValue(tempRecipeObject).addOnCompleteListener { task: Task<Void> ->
                if(task.isSuccessful){
                    emitter.onNext(true)
                } else{
                    emitter.onNext(false)
                }
            }
        }
    }

    fun loadAllRecipes(): Observable<List<Recipes>>{
        val currentUserId = mAuth.currentUser!!.uid

        return Observable.create<List<Recipes>> {emitter ->
            val listOfRecipes = arrayListOf<Recipes>()

            recipesDatabase.child(currentUserId).child(RECIPES_DATABASE)
                .addListenerForSingleValueEvent(object : ValueEventListener{

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val orderSnapshot = dataSnapshot.children

                    for(recipe in orderSnapshot){
                        val recipeId = recipe.key
                        val title = recipe.child("title").value.toString()
                        val description = recipe.child("description").value.toString()
                        val ingredientsList = mutableListOf<String>()

                        if(recipe.hasChild("ingredients")){
                            recipe.child("ingredients").apply {
                                children.forEach {
                                    ingredientsList.add(it.value.toString())
                                }
                            }
                        }

                        listOfRecipes.add(Recipes(recipeId, title, description, ingredientsList))
                    }

                    emitter.onNext(listOfRecipes)
                }

                override fun onCancelled(error: DatabaseError) {
                    emitter.onError(error.toException())
                }

            })
        }
    }

    fun loadSingleRecipe(recipeId: String): Observable<Recipes>{
        val currentUserId = mAuth.currentUser!!.uid

        return Observable.create<Recipes> {emitter ->
            recipesDatabase.child(currentUserId).child(RECIPES_DATABASE).child(recipeId)
                .addListenerForSingleValueEvent(object: ValueEventListener{

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val id = dataSnapshot.key
                    val title = dataSnapshot.child("title").value.toString()
                    val description = dataSnapshot.child("description").value.toString()
                    val ingredientsList = mutableListOf<String>()

                    if(dataSnapshot.hasChild("ingredients")){
                        dataSnapshot.child("ingredients").apply {
                            children.forEach {
                                ingredientsList.add(it.value.toString().trim())
                            }
                        }
                    }

                    emitter.onNext(Recipes(id, title, description, ingredientsList))
                }

                override fun onCancelled(error: DatabaseError) {
                    emitter.onError(error.toException())
                }

            })
        }
    }

    fun deleteRecipe(recipeId: String): Observable<Boolean>{
        val currentUserId = mAuth.currentUser!!.uid

        return Observable.create<Boolean> {emitter ->
            recipesDatabase.child(currentUserId).child(RECIPES_DATABASE).child(recipeId)
                .removeValue().addOnCompleteListener { task: Task<Void> ->
                if(task.isSuccessful){
                    emitter.onNext(true)
                } else{
                    emitter.onNext(false)
                }
            }
        }
    }

    fun editRecipe(recipes: Recipes): Observable<Boolean>{
        val currentUserId = mAuth.currentUser!!.uid

        return Observable.create<Boolean> {emitter ->
            recipesDatabase.child(currentUserId).child(RECIPES_DATABASE).child(recipes.recipeId!!)
                .setValue(recipes).addOnCompleteListener { task: Task<Void> ->
                if(task.isSuccessful){
                    emitter.onNext(true)
                } else{
                    emitter.onNext(false)
                }
            }
        }
    }
}