package com.example.dailyfoodplanner.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dailyfoodplanner.base.ViewModelFactory
import com.example.dailyfoodplanner.di.annotation.ViewModelKey
import com.example.dailyfoodplanner.ui.dailyPlanerDetails.DailyPlanerDetailsViewModel
import com.example.dailyfoodplanner.ui.home.HomeViewModel
import com.example.dailyfoodplanner.ui.notes.NotesViewModel
import com.example.dailyfoodplanner.ui.recipeDetails.RecipeDetailsViewModel
import com.example.dailyfoodplanner.ui.recipes.RecipesViewModel
import com.example.dailyfoodplanner.ui.schedule.ScheduleViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ScheduleViewModel::class)
    abstract fun bindScheduleViewModel(scheduleViewModel: ScheduleViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RecipesViewModel::class)
    abstract fun bindRecipesViewModel(recipesViewModel: RecipesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NotesViewModel::class)
    abstract fun bindNotesViewModel(notesViewModel: NotesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DailyPlanerDetailsViewModel::class)
    abstract fun bindDailyPlanerDetailsViewModel(dailyPlanerDetailsViewModel: DailyPlanerDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RecipeDetailsViewModel::class)
    abstract fun bindRecipeDetailsViewModel(recipeDetailsViewModel: RecipeDetailsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}