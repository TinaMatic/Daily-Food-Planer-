package com.example.dailyfoodplanner.model

import javax.inject.Inject

data class User @Inject constructor(val userId: String, val username: String, val email: String,
                                    val password: String, val image: String?)