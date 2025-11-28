package com.example.healtyapp.ui.citas

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.example.healtyapp.R
import com.example.healtyapp.data.remote.dto.Appointment
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

class CrearCitaActivity : ComponentActivity() {

    private val vm: CitasViewModel by viewModels()
    private val scope = MainScope()
    
    private lateinit var btnSeleccionarFecha: Button
    private lateinit var tvFechaSeleccionada: TextView
    private lateinit var btnSeleccionarHora: Button
    private lateinit var tvHoraSeleccionada: TextView
    private lateinit var etMotivo: EditText
    private lateinit var spTipo: Spinner
    private lateinit var spEstado: Spinner
    private lateinit var etTipoEjercicio: EditText
    private lateinit var etRepeticiones: EditText
    private lateinit var etTiempo: EditText
    private lateinit var spIntensidad: Spinner
    private lateinit var etFrecuenciaSemanal: EditText
    private lateinit var chkProgresoPositivo: CheckBox
    private lateinit var etIMC: EditText
    private lateinit var etPorcentajeGrasa: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button
    private lateinit var progressBar: ProgressBar
    
    private var fechaSeleccionada: String? = null
    private var horaSeleccionada: String? = null
    private var pacienteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cita)

        pacienteId = intent.getIntExtra("paciente_id", -1)
        require(pacienteId != -1) { "Se requiere paciente_id" }

        inicializarVistas()
        configurarSpinners()
        configurarEventos()
        
        scope.launch {
            vm.state.collectLatest { state ->
                state.error?.let {
                    Toast.makeText(this@CrearCitaActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun inicializarVistas() {
        btnSeleccionarFecha = findViewById(R.id.btnSeleccionarFecha)
        tvFechaSeleccionada = findViewById(R.id.tvFechaSeleccionada)
        btnSeleccionarHora = findViewById(R.id.btnSeleccionarHora)
        tvHoraSeleccionada = findViewById(R.id.tvHoraSeleccionada)
        etMotivo = findViewById(R.id.etMotivo)
        spTipo = findViewById(R.id.spTipo)
        spEstado = findViewById(R.id.spEstado)
        etTipoEjercicio = findViewById(R.id.etTipoEjercicio)
        etRepeticiones = findViewById(R.id.etRepeticiones)
        etTiempo = findViewById(R.id.etTiempo)
        spIntensidad = findViewById(R.id.spIntensidad)
        etFrecuenciaSemanal = findViewById(R.id.etFrecuenciaSemanal)
        chkProgresoPositivo = findViewById(R.id.chkProgresoPositivo)
        etIMC = findViewById(R.id.etIMC)
        etPorcentajeGrasa = findViewById(R.id.etPorcentajeGrasa)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnCancelar = findViewById(R.id.btnCancelar)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun configurarSpinners() {
        // Spinner de Tipo
        val tiposAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listOf("primera", "seguimiento")
        )
        tiposAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spTipo.adapter = tiposAdapter

        // Spinner de Estado
        val estadosAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listOf("pendiente", "asistida", "cancelada")
        )
        estadosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spEstado.adapter = estadosAdapter

        // Spinner de Intensidad
        val intensidadAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listOf("Seleccionar...", "baja", "moderada", "alta")
        )
        intensidadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spIntensidad.adapter = intensidadAdapter
    }

    private fun configurarEventos() {
        btnSeleccionarFecha.setOnClickListener {
            mostrarDatePicker()
        }

        btnSeleccionarHora.setOnClickListener {
            mostrarTimePicker()
        }

        btnCancelar.setOnClickListener {
            finish()
        }

        btnGuardar.setOnClickListener {
            guardarCita()
        }
    }

    private fun mostrarDatePicker() {
        val cal = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, day ->
                fechaSeleccionada = String.format("%04d-%02d-%02d", year, month + 1, day)
                tvFechaSeleccionada.text = fechaSeleccionada
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun mostrarTimePicker() {
        val cal = Calendar.getInstance()
        TimePickerDialog(
            this,
            { _, hour, minute ->
                horaSeleccionada = String.format("%02d:%02d:00", hour, minute)
                tvHoraSeleccionada.text = horaSeleccionada
            },
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun guardarCita() {
        // Validaciones
        if (fechaSeleccionada == null) {
            Toast.makeText(this, "Selecciona una fecha", Toast.LENGTH_SHORT).show()
            return
        }
        if (horaSeleccionada == null) {
            Toast.makeText(this, "Selecciona una hora", Toast.LENGTH_SHORT).show()
            return
        }
        if (etMotivo.text.isNullOrBlank()) {
            Toast.makeText(this, "Ingresa un motivo", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE
        btnGuardar.isEnabled = false

        // Construir objeto Appointment
        val nuevaCita = Appointment(
            id = 0, // El backend asignará el ID
            paciente = pacienteId,
            fecha = fechaSeleccionada!!,
            hora = horaSeleccionada!!,
            motivo = etMotivo.text.toString(),
            tipo = spTipo.selectedItem.toString(),
            estado = spEstado.selectedItem.toString(),
            tipo_ejercicio = etTipoEjercicio.text.toString().takeIf { it.isNotBlank() },
            repeticiones = etRepeticiones.text.toString().toIntOrNull(),
            tiempo = etTiempo.text.toString().toIntOrNull(),
            intensidad = spIntensidad.selectedItem.toString().takeIf { it != "Seleccionar..." },
            frecuencia_semanal = etFrecuenciaSemanal.text.toString().toIntOrNull(),
            progreso_positivo = if (chkProgresoPositivo.isChecked) true else null,
            imc = etIMC.text.toString().takeIf { it.isNotBlank() },
            porcentaje_grasa_corporal = etPorcentajeGrasa.text.toString().takeIf { it.isNotBlank() }
        )

        vm.crearCita(nuevaCita) {
            Toast.makeText(this, "Cita creada exitosamente", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
