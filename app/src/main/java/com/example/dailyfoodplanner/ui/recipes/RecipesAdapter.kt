package com.example.dailyfoodplanner.ui.recipes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyfoodplanner.R
import com.example.dailyfoodplanner.model.Recipes
import kotlinx.android.synthetic.main.item_recipe.view.*

class RecipesAdapter (val context: Context, val listRecipes: ArrayList<Recipes>)
    : RecyclerView.Adapter<RecipesAdapter.ViewHolder>(){

    private var onItemClickListener: OnItemClickListener? = null

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

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindView(recipe: Recipes, position: Int){

            itemView.tvRecipeTitle.text = recipe.title
            itemView.tvRecipeDescription.text = recipe.description

            itemView.btnEditRecipe.setOnClickListener {
                onItemClickListener?.editRecipe(recipe, position)
            }

            itemView.btnDeleteRecipe.setOnClickListener {
                onItemClickListener?.deleteRecipe(recipe.recipeId!!, position)
            }

            itemView.btnShowMore.setOnClickListener {
                onItemClickListener?.showMore(recipe)
            }

        }
    }

    interface OnItemClickListener{
        fun editRecipe(recipe: Recipes, position: Int)
        fun deleteRecipe(recipeId: String, position: Int)
        fun showMore(recipe: Recipes)
    }
}