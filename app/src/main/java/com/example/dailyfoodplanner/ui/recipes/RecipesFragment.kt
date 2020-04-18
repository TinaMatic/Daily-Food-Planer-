package com.example.dailyfoodplanner.ui.recipes

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailyfoodplanner.R
import com.example.dailyfoodplanner.model.Recipes
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.add_recipe_dialog.*
import kotlinx.android.synthetic.main.add_recipe_dialog.view.*
import kotlinx.android.synthetic.main.fragment_recipes.*
import javax.inject.Inject

class RecipesFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var recipeViewModel: RecipesViewModel

    private lateinit var recipeAdapter: RecipesAdapter

    private var compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipeViewModel = ViewModelProvider(this, viewModelFactory).get(RecipesViewModel::class.java)

        loadAllRecipes()

        btnAddRecipe.setOnClickListener {
            showAddDialog()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        recipeViewModel.claer()
        compositeDisposable.clear()
    }

    fun loadAllRecipes(){
        recipeViewModel.loadAllRecipes()

        recipeViewModel.allRecipesLiveData.observe(this, Observer {
            setRecipeAdapter(it)
//            recipeAdapter.notifyDataSetChanged()
        })
    }

    fun setRecipeAdapter(listRecipes: List<Recipes>){
        recipeAdapter = RecipesAdapter(requireContext(), listRecipes)

        recyclerViewRecipes.layoutManager = LinearLayoutManager(context)
        recyclerViewRecipes.adapter = recipeAdapter
    }

    private fun showAddDialog(){
        var recipeTitle: String?
        var recipeDescription: String?
        var recipeIngredients: String?

        val view = LayoutInflater.from(context).inflate(R.layout.add_recipe_dialog, null)

        val builder = AlertDialog.Builder(context)
            .setTitle(getString(R.string.add_recipe_title))
            .setPositiveButton(getString(R.string.add_recipe)){dialog, id ->
                recipeTitle = view.etRecipeTitle.text.toString()
                recipeDescription = view.etRecipeDescription.text.toString()
                recipeIngredients = view.etRecipeIngredients.text.toString()

                val recipeIngredientsList = recipeIngredients?.split(",")?.toList()

                compositeDisposable.add(recipeViewModel.writeRecipe(Recipes(null, recipeTitle!!, recipeDescription!!, recipeIngredientsList!!))
                    .subscribe ({
                        if (it){
                            Toast.makeText(context, "Recipe successfully added", Toast.LENGTH_SHORT).show()
                            loadAllRecipes()
                        } else{
                            Toast.makeText(context, "Something went wrong when adding", Toast.LENGTH_SHORT).show()
                        }
                    },{}))

            }
            .setNegativeButton(getString(R.string.cancel_recipe)){dialog, id ->  }

        val alert = builder.create()
        alert.setView(view)
        alert.show()
    }
}