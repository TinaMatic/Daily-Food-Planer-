package com.example.dailyfoodplanner.ui.recipeDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dailyfoodplanner.data.FirebaseRepositoryRecipes
import com.example.dailyfoodplanner.model.Recipes
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RecipeDetailsViewModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var firebaseRepositoryRecipes: FirebaseRepositoryRecipes

    private var compositeDisposable = CompositeDisposable()

    var recipeDetailsLiveData = MutableLiveData<Recipes>()

    fun getRecipeDetails(recipeId: String){
        compositeDisposable.add(firebaseRepositoryRecipes.getSingleRecipe(recipeId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
               recipeDetailsLiveData.postValue(it)
            },{}))
    }

    fun clear(){
        compositeDisposable.clear()
    }
}