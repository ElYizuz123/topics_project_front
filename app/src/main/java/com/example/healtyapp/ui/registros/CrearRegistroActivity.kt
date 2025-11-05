package com.example.healtyapp.ui.registros

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.healtyapp.R
import com.example.healtyapp.data.remote.dto.Registro
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CrearRegistroActivity : ComponentActivity() {

    private val vm: CrearRegistroViewModel by viewModels()
    private var citaId: Int = -1
    private var fechaSeleccionada: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_registro)

        citaId = intent.getIntExtra("cita_id", -1)
        if (citaId == -1) {
            Toast.makeText(this, "Error: No se recibió el ID de la cita", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val btnSeleccionarFecha = findViewById<Button>(R.id.btnSeleccionarFecha)
        val tvFechaSeleccionada = findViewById<TextView>(R.id.tvFechaSeleccionada)
        val chkCumplio = findViewById<CheckBox>(R.id.chkCumplio)
        val etObservaciones = findViewById<EditText>(R.id.etObservaciones)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        // Configurar fecha actual por defecto
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        fechaSeleccionada = dateFormat.format(calendar.time)
        tvFechaSeleccionada.text = "Fecha: $fechaSeleccionada"

        // Selector de fecha
        btnSeleccionarFecha.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                fechaSeleccionada = dateFormat.format(calendar.time)
                tvFechaSeleccionada.text = "Fecha: $fechaSeleccionada"
            }, year, month, day).show()
        }

        // Botón cancelar
        btnCancelar.setOnClickListener {
            finish()
        }

        // Botón guardar
        btnGuardar.setOnClickListener {
            val fecha = fechaSeleccionada
            val cumplio = chkCumplio.isChecked
            val observaciones = etObservaciones.text.toString().trim()

            if (fecha == null) {
                Toast.makeText(this, "Por favor selecciona una fecha", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nuevoRegistro = Registro(
                id = 0, // El backend asignará el ID
                cita = citaId,
                fecha = fecha,
                cumplio = cumplio,
                observaciones = observaciones.ifEmpty { null }
            )

            vm.crearRegistro(nuevoRegistro)
        }

        // Observar estado del ViewModel
        lifecycleScope.launch {
            vm.state.collectLatest { state ->
                progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                btnGuardar.isEnabled = !state.isLoading
                btnCancelar.isEnabled = !state.isLoading

                if (state.success) {
                    Toast.makeText(
                        this@CrearRegistroActivity,
                        "Registro creado exitosamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    setResult(RESULT_OK)
                    finish()
                }

                state.error?.let { msg ->
                    Toast.makeText(this@CrearRegistroActivity, msg, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
