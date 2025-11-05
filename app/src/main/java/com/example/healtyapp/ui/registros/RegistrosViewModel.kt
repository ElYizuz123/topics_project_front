package com.example.healtyapp.ui.registros

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healtyapp.data.remote.api.ApiClient
import com.example.healtyapp.data.remote.dto.Registro
import com.example.healtyapp.data.remote.dto.PageResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class RegistrosUiState(
    val items: List<Registro> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class RegistrosViewModel : ViewModel() {

    private val _state = MutableStateFlow(RegistrosUiState())
    val state: StateFlow<RegistrosUiState> = _state

    fun cargar(citaId: Int) {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.api.getRegistros(citaId).execute()
                }
                if (response.isSuccessful) {
                    val body = response.body()
                    _state.value = _state.value.copy(
                        items = body?.results ?: emptyList(),
                        isLoading = false
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "Error ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}
