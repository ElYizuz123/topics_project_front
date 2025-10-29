package com.example.healtyapp.data.remote.dto

data class LoginRequest(
    val username: String,
    val password: String
)

data class TokenResponse(
    val refresh: String,
    val access: String
)

