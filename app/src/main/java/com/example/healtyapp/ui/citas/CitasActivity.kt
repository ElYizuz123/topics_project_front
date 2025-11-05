package com.example.healtyapp.ui.citas

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healtyapp.R
import com.example.healtyapp.data.remote.dto.Appointment
import com.example.healtyapp.ui.registros.RegistrosActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

class CitasActivity : ComponentActivity() {

    private val vm: CitasViewModel by viewModels()
    private val adapter = CitasAdapter { cita ->
        // Navegación a registros cuando se hace clic en una cita
        val intent = Intent(this, RegistrosActivity::class.java)
        intent.putExtra("cita_id", cita.id)
        startActivity(intent)
    }
    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_citas)

        val pacienteId = intent.getIntExtra("paciente_id", -1)
        require(pacienteId != -1)

        val rv = findViewById<RecyclerView>(R.id.rvCitas)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val btnNueva = findViewById<FloatingActionButton>(R.id.btnNueva)

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                val lm = rv.layoutManager as LinearLayoutManager
                val visible = lm.childCount
                val total = lm.itemCount
                val first = lm.findFirstVisibleItemPosition()
                if (dy > 0 && first + visible >= total - 4 && vm.state.value.hasNext) {
                    vm.load(pacienteId, vm.state.value.page + 1)
                }
            }
        })

        btnNueva.setOnClickListener { mostrarDialogoNueva(pacienteId) }

        scope.launch {
            vm.state.collectLatest { s ->
                progress.visibility = if (s.isLoading && s.items.isEmpty()) View.VISIBLE
                else View.GONE
                adapter.submitList(s.items)
                s.error?.let {
                    Toast.makeText(this@CitasActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
        }

        vm.load(pacienteId, 1)
    }

    private fun mostrarDialogoNueva(pacienteId: Int) {
        // Diálogo simple con DatePicker y TimePicker
        val cal = Calendar.getInstance()

        DatePickerDialog(this, { _, y, m, d ->
            val fecha = String.format("%04d-%02d-%02d", y, m + 1, d)
            TimePickerDialog(this, { _, hh, mm ->
                val hora = String.format("%02d:%02d:00", hh, mm)
                // Datos básicos para crear cita
                val motivo = "Consulta" // puedes reemplazar por un EditText en un AlertDialog personalizado
                val nueva = Appointment(
                    id = 0,
                    paciente = pacienteId,
                    fecha = fecha,
                    hora = hora,
                    motivo = motivo,
                    tipo = "primera",
                    estado = "pendiente"
                )
                vm.crearCita(nueva) {
                    Toast.makeText(this, "Cita creada", Toast.LENGTH_SHORT).show()
                }
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
    }

}
