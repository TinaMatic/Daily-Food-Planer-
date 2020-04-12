package com.example.dailyfoodplanner.model

import javax.inject.Inject

data class Notes @Inject constructor(val notesId: String?, val note: String)