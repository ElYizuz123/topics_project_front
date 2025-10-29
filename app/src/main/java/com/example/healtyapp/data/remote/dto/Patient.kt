package com.example.healtyapp.data.remote.dto
data class Patient(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val edad: Int?,
    val genero: String?
)

data class PageResponse<T>(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<T>
)
