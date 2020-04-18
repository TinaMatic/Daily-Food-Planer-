package com.example.dailyfoodplanner.ui.recipeDetails


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.dailyfoodplanner.R
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_recipe_details.*

/**
 * A simple [Fragment] subclass.
 */
class RecipeDetailsFragment : DaggerFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_details, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textView.text = "Hello"
    }
}
