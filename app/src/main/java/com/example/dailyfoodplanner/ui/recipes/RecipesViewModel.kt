package com.example.dailyfoodplanner.ui.recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dailyfoodplanner.data.FirebaseRepositoryRecipes
import com.example.dailyfoodplanner.model.Recipes
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RecipesViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var firebaseRepositoryRecipes: FirebaseRepositoryRecipes

    var allRecipesLiveData = MutableLiveData<List<Recipes>>()

    var recipeLoading = MutableLiveData<Boolean>()

    var recipeLiveDataError = MutableLiveData<Boolean>()

    private var compositeDisposable = CompositeDisposable()

    fun loadAllRecipes(){
        recipeLoading.value = true

        compositeDisposable.add(firebaseRepositoryRecipes.loadAllRecipes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                allRecipesLiveData.postValue(it)
                recipeLoading.value = false
                recipeLiveDataError.value = false
            },{
                recipeLoading.value = false
                recipeLiveDataError.value = true
            }))
    }

    fun writeRecipe(recipe: Recipes):Observable<Boolean>{
        return firebaseRepositoryRecipes.writeRecipes(recipe)
    }

    fun editRecipe(recipe: Recipes): Observable<Boolean>{
        return firebaseRepositoryRecipes.editRecipe(recipe)
    }

    fun deleteRecipe(recipeId: String): Observable<Boolean>{
        return firebaseRepositoryRecipes.deleteRecipe(recipeId)
    }

    fun searchRecipes(recipeTitle: String): Observable<List<Recipes>>{
        val searchList = arrayListOf<Recipes>()

        return Observable.create<List<Recipes>> { emitter ->
            firebaseRepositoryRecipes.loadAllRecipes().subscribe ({ listRecipes ->
                listRecipes.forEach {
                    if (it.title.contains(recipeTitle)) {
                        searchList.add(it)
                    }
                }
                emitter.onNext(searchList)
            },{})
        }
    }

    fun claer(){
        compositeDisposable.clear()
    }
}