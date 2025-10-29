package com.example.healtyapp.data.remote.dto

data class Appointment(
    val id: Int,
    val paciente: Int,
    val fecha: String, // "YYYY-MM-DD"
    val hora: String,  // "HH:mm:ss"
    val motivo: String,
    val tipo: String,  // "primera" | "seguimiento"
    val estado: String, // "pendiente" | "asistida" | "cancelada"
    val created_at: String? = null // Campo opcional del servidor
)
