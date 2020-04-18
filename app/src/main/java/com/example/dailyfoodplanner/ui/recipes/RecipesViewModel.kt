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

    private var compositeDisposable = CompositeDisposable()

    fun loadAllRecipes(){
        compositeDisposable.add(firebaseRepositoryRecipes.loadAllRecipes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                allRecipesLiveData.postValue(it)
            },{}))
    }

    fun writeRecipe(recipe: Recipes):Observable<Boolean>{
        return firebaseRepositoryRecipes.writeRecipes(recipe)
    }

    fun claer(){
        compositeDisposable.clear()
    }
}