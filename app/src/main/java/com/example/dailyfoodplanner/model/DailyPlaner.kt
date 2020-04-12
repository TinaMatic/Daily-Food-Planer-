package com.example.dailyfoodplanner.model

import java.sql.Time
import java.util.*
import javax.inject.Inject

data class DailyPlaner @Inject constructor(val id: String?, val date: String,
                                           val timeBreakfast: String, val recipeBreakfast: String,
                                           val timeSnack1: String, val recipeSnack1: String,
                                           val timeLunch: String, val recipeLunch: String,
                                           val timeSnack2: String, val recipeSnack2: String,
                                           val timeDinner: String, val recipeDinner: String)