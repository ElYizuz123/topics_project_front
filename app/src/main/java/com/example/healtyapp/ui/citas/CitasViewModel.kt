package com.example.healtyapp.ui.citas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healtyapp.data.remote.api.ApiClient
import com.example.healtyapp.data.remote.dto.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CitasUiState(
    val items: List<Appointment> = emptyList(),
    val isLoading: Boolean = false,
    val page: Int = 1,
    val hasNext: Boolean = true,
    val error: String? = null
)

class CitasViewModel : ViewModel() {
    private val _state = MutableStateFlow(CitasUiState())
    val state: StateFlow<CitasUiState> = _state
    var pacienteId: Int = -1

    fun load(paciente: Int, page: Int = 1) {
        if (_state.value.isLoading) return
        pacienteId = paciente
        _state.value = _state.value.copy(isLoading = true)

        viewModelScope.launch {
            try {
                val response: PageResponse<Appointment> = ApiClient.api.getCitas(page = page, pacienteId = pacienteId)
                _state.value = _state.value.copy(
                    items = if (page == 1) response.results else _state.value.items + response.results,
                    page = page,
                    hasNext = response.next != null,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun crearCita(nueva: Appointment, onOk: () -> Unit) {
        viewModelScope.launch {
            try {
                val response: Appointment = ApiClient.api.crearCita(nueva)
                onOk()
                load(pacienteId, 1)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }
}
