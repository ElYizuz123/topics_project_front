package com.example.healtyapp.ui.patients

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healtyapp.R
import com.example.healtyapp.ui.citas.CitasActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PatientsActivity : ComponentActivity() {

    private val vm: PatientsViewModel by viewModels()
    private val adapter = PatientsAdapter { patient ->
        val i = Intent(this, CitasActivity::class.java)
        i.putExtra("paciente_id", patient.id)
        startActivity(i)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patients)

        val rv = findViewById<RecyclerView>(R.id.rvPatients)
        val progress = findViewById<ProgressBar>(R.id.progress)

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                val lm = rv.layoutManager as LinearLayoutManager
                val visibleItemCount = lm.childCount
                val totalItemCount = lm.itemCount
                val firstVisibleItemPosition = lm.findFirstVisibleItemPosition()

                val state = vm.state.value

                // Verificar si necesitamos cargar más datos
                val shouldLoadMore = !state.isLoading &&
                        state.hasNext &&
                        (firstVisibleItemPosition + visibleItemCount) >= (totalItemCount - 3)

                if (shouldLoadMore) {
                    vm.loadPage(state.page + 1)
                }
            }
        })

        lifecycleScope.launch {
            vm.state.collectLatest { s ->
                progress.visibility =
                    if (s.isLoading && s.items.isEmpty()) View.VISIBLE else View.GONE

                adapter.submitList(s.items)

                if (s.error != null) {
                    Toast.makeText(
                        this@PatientsActivity,
                        s.error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        vm.loadPage(1) // Cargar primera página explícitamente
    }
}