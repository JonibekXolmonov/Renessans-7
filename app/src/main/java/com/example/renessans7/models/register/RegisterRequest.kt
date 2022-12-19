package com.example.renessans7.models.register

data class RegisterRequest(
    val fullName: String,
    val password: String,
    val rePassword: String,
    val username: String
)