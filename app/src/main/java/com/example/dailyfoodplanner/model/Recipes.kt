package com.example.dailyfoodplanner.model

import javax.inject.Inject

data class Recipes @Inject constructor(val recipeId: String?, val title: String,
                                       val description: String, val ingredients: List<String>)