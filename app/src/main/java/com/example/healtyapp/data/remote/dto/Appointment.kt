package com.example.healtyapp.data.remote.dto

data class Appointment(
    val id: Int,
    val paciente: Int,
    val fecha: String, // "YYYY-MM-DD"
    val hora: String,  // "HH:mm:ss"
    val motivo: String,
    val tipo: String,  // "primera" | "seguimiento"
    val estado: String, // "pendiente" | "asistida" | "cancelada"
    val tipo_ejercicio: String? = null,
    val repeticiones: Int? = null,
    val tiempo: Int? = null, // minutos
    val intensidad: String? = null, // "baja" | "moderada" | "alta"
    val frecuencia_semanal: Int? = null,
    val progreso_positivo: Boolean? = null,
    val imc: String? = null,
    val porcentaje_grasa_corporal: String? = null,
    val created_at: String? = null
)
