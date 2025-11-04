package com.example.healtyapp.ui.registros

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healtyapp.data.remote.api.ApiClient
import com.example.healtyapp.data.remote.dto.Registro
import com.example.healtyapp.data.remote.dto.PageResponseRegistro
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

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
                val res = ApiClient.api.getRegistros(citaId).awaitResponse()
                if (res.isSuccessful) {
                    _state.value = _state.value.copy(
                        items = res.body()!!.results,
                        isLoading = false
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "Error ${res.code()}"
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
