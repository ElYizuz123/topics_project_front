package com.example.healtyapp.data.remote.dto

data class Registro(
    val id: Int,
    val cita: Int,
    val fecha: String,
    val cumplio: Boolean,
    val dolor_ejecucion: Int?, // 0-10
    val dificultad_percibida: String?, // "facil", "moderado", "dificil"
    val observaciones: String?
)

data class PageResponseRegistro<T>(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<T>
)
