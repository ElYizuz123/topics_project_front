package com.example.healtyapp.ui.registros

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healtyapp.data.remote.api.ApiClient
import com.example.healtyapp.data.remote.dto.Registro
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class CrearRegistroUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

class CrearRegistroViewModel : ViewModel() {

    private val _state = MutableStateFlow(CrearRegistroUiState())
    val state: StateFlow<CrearRegistroUiState> = _state

    fun crearRegistro(registro: Registro) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.api.crearRegistro(registro).execute()
                }
                if (response.isSuccessful) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        success = true
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "Error ${response.code()}: ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Error: ${e.message}"
                )
            }
        }
    }
}
