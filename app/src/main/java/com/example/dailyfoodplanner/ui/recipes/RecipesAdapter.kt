package com.example.dailyfoodplanner.ui.recipes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyfoodplanner.R
import com.example.dailyfoodplanner.model.Recipes
import kotlinx.android.synthetic.main.item_recipe.view.*

class RecipesAdapter (val context: Context, private val listRecipes: List<Recipes>)
    : RecyclerView.Adapter<RecipesAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listRecipes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(listRecipes[position], position)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindView(recipe: Recipes, position: Int){

            itemView.tvRecipeTitle.text = recipe.title
            itemView.tvRecipeDescription.text = recipe.description

        }
    }

}