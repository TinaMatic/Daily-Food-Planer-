package com.example.dailyfoodplanner.di.module

import com.example.dailyfoodplanner.ui.home.HomeFragment
import com.example.dailyfoodplanner.ui.notes.NotesFragment
import com.example.dailyfoodplanner.ui.recipeDetails.RecipeDetailsFragment
import com.example.dailyfoodplanner.ui.recipes.RecipesFragment
import com.example.dailyfoodplanner.ui.schedule.ScheduleFragment
import com.example.dailyfoodplanner.ui.settings.SettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun provideHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun providScheduleFragment(): ScheduleFragment

    @ContributesAndroidInjector
    abstract fun provideRecipesFragment(): RecipesFragment

    @ContributesAndroidInjector
    abstract fun provideNotesFragment(): NotesFragment

    @ContributesAndroidInjector
    abstract fun provideRecipeDetailsFragment(): RecipeDetailsFragment

    @ContributesAndroidInjector
    abstract fun provideSettingsFragment(): SettingsFragment
}