package com.example.healtyapp.ui.citas

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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CitasActivity : ComponentActivity() {

    private val vm: CitasViewModel by viewModels()
    private val adapter = CitasAdapter { cita ->
        // Navegación a detalles de la cita
        val intent = Intent(this, CitaDetalleActivity::class.java)
        intent.putExtra("cita", Gson().toJson(cita))
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

        btnNueva.setOnClickListener { 
            // Abrir actividad de crear cita
            val intent = Intent(this, CrearCitaActivity::class.java)
            intent.putExtra("paciente_id", pacienteId)
            startActivity(intent)
        }

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
    
    override fun onResume() {
        super.onResume()
        // Recargar las citas cuando se regrese a esta actividad
        val pacienteId = intent.getIntExtra("paciente_id", -1)
        if (pacienteId != -1) {
            vm.load(pacienteId, 1)
        }
    }

}

