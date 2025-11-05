package com.example.healtyapp.ui.registros

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healtyapp.R
import com.example.healtyapp.data.remote.dto.Registro
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RegistrosActivity : ComponentActivity() {

    private val vm: RegistrosViewModel by viewModels()
    private val adapter = RegistrosAdapter()
    private val scope = MainScope()

    // Launcher para la actividad de crear registro
    private val crearRegistroLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // Recargar la lista después de crear un nuevo registro
            val citaId = intent.getIntExtra("cita_id", -1)
            vm.cargar(citaId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registros)

        val citaId = intent.getIntExtra("cita_id", -1)

        val rv = findViewById<RecyclerView>(R.id.rvRegistros)
        val btnNuevo = findViewById<Button>(R.id.btnNuevo)

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        scope.launch {
            vm.state.collectLatest {
                adapter.submitList(it.items)
                it.error?.let { msg ->
                    Toast.makeText(this@RegistrosActivity, msg, Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnNuevo.setOnClickListener {
            val intent = Intent(this, CrearRegistroActivity::class.java)
            intent.putExtra("cita_id", citaId)
            crearRegistroLauncher.launch(intent)
        }

        vm.cargar(citaId)
    }
}
