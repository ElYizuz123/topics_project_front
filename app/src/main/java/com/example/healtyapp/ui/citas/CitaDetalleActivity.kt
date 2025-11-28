package com.example.healtyapp.ui.citas

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.cardview.widget.CardView
import com.example.healtyapp.R
import com.example.healtyapp.data.remote.dto.Appointment
import com.google.gson.Gson

class CitaDetalleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cita_detalle)

        val citaJson = intent.getStringExtra("cita")
        require(citaJson != null) { "Se requiere el objeto cita" }
        
        val cita = Gson().fromJson(citaJson, Appointment::class.java)
        
        mostrarDatos(cita)
        
        findViewById<Button>(R.id.btnVolver).setOnClickListener {
            finish()
        }
    }
    
    private fun mostrarDatos(cita: Appointment) {
        // Información básica
        findViewById<TextView>(R.id.tvFecha).text = cita.fecha
        findViewById<TextView>(R.id.tvHora).text = cita.hora
        findViewById<TextView>(R.id.tvMotivo).text = cita.motivo
        findViewById<TextView>(R.id.tvTipo).text = cita.tipo.capitalize()
        findViewById<TextView>(R.id.tvEstado).text = cita.estado.capitalize()
        
        // Información de ejercicio
        val cardEjercicio = findViewById<CardView>(R.id.cardEjercicio)
        if (cita.tipo_ejercicio != null) {
            cardEjercicio.visibility = View.VISIBLE
            findViewById<TextView>(R.id.tvTipoEjercicio).text = cita.tipo_ejercicio
            findViewById<TextView>(R.id.tvRepeticiones).text = cita.repeticiones?.toString() ?: "N/A"
            findViewById<TextView>(R.id.tvTiempo).text = cita.tiempo?.toString() ?: "N/A"
            findViewById<TextView>(R.id.tvIntensidad).text = cita.intensidad?.capitalize() ?: "N/A"
            findViewById<TextView>(R.id.tvFrecuencia).text = 
                if (cita.frecuencia_semanal != null) "${cita.frecuencia_semanal} días/semana" else "N/A"
            
            val progresoText = when (cita.progreso_positivo) {
                true -> "✅ Positivo"
                false -> "⚠️ Requiere atención"
                null -> "N/A"
            }
            findViewById<TextView>(R.id.tvProgreso).text = progresoText
        } else {
            cardEjercicio.visibility = View.GONE
        }
        
        // Métricas corporales
        val cardMetricas = findViewById<CardView>(R.id.cardMetricas)
        if (cita.imc != null || cita.porcentaje_grasa_corporal != null) {
            cardMetricas.visibility = View.VISIBLE
            findViewById<TextView>(R.id.tvIMC).text = cita.imc ?: "N/A"
            findViewById<TextView>(R.id.tvGrasaCorporal).text = 
                if (cita.porcentaje_grasa_corporal != null) "${cita.porcentaje_grasa_corporal}%" else "N/A"
        } else {
            cardMetricas.visibility = View.GONE
        }
    }
}
