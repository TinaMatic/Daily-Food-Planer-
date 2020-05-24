package com.example.dailyfoodplanner.ui.recipeDetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.dailyfoodplanner.base.RxScheduleRule
import com.example.dailyfoodplanner.data.FirebaseRepositoryRecipes
import com.example.dailyfoodplanner.model.Recipes
import io.reactivex.Observable
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class RecipeDetailsViewModelTest{

    @Mock
    lateinit var firebaseRepositoryRecipes: FirebaseRepositoryRecipes

    @InjectMocks
    lateinit var recipeDetailsViewModel: RecipeDetailsViewModel

    @get:Rule
    val rxScheduleRule = RxScheduleRule()

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getRecipeDetails_success() {
        val recipe = Recipes("recipeId", "title", "description", listOf("ingredients"))

        val recipeDetailTest = Observable.just(recipe)

        Mockito.`when`(firebaseRepositoryRecipes.getSingleRecipe("recipeId")).thenReturn(recipeDetailTest)

        recipeDetailsViewModel.getRecipeDetails("recipeId")

        assertEquals(recipe, recipeDetailsViewModel.recipeDetailsLiveData.value)
    }
}