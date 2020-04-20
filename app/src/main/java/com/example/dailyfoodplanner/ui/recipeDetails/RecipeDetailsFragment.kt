package com.example.dailyfoodplanner.ui.recipeDetails


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.example.dailyfoodplanner.R
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_recipe_details.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class RecipeDetailsFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var recipeDetailsViewModel: RecipeDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_details, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipeDetailsViewModel = ViewModelProvider(this, viewModelFactory).get(RecipeDetailsViewModel::class.java)

        val recipeId = RecipeDetailsFragmentArgs.fromBundle(arguments!!).recipeId

        loadRecipeDeatils(recipeId!!)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        recipeDetailsViewModel.clear()
    }

    fun loadRecipeDeatils(recipeId: String){
        recipeDetailsViewModel.loadRecipeDetails(recipeId)

        recipeDetailsViewModel.recipeDetailsLiveData.observe(this, Observer {
            tvRecipeTitle.text = it.title
            tvRecipeDescription.text = it.description

            var recipeIngredientsList = ""

            it.ingredients.forEach { ingredients->
                recipeIngredientsList += "$ingredients\n"
            }

            tvRecipeIngredients.text = recipeIngredientsList
        })
    }



}
