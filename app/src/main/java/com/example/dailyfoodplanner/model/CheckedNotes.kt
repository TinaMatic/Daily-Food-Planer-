package com.example.dailyfoodplanner.model

import javax.inject.Inject

data class CheckedNotes @Inject constructor (val notesId: String?, var isChecked: Boolean)