package com.example.healtyapp.ui.patients
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healtyapp.data.remote.api.ApiClient
import com.example.healtyapp.data.remote.dto.Patient
import com.example.healtyapp.data.remote.dto.PageResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class PatientsUiState(
    val items: List<Patient> = emptyList(),
    val isLoading: Boolean = false,
    val page: Int = 1,
    val hasNext: Boolean = true,
    val error: String? = null
)

class PatientsViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(PatientsUiState())
    val state: StateFlow<PatientsUiState> = _state

    fun loadPage(page: Int = 1) {
        if (_state.value.isLoading) return

        _state.value = _state.value.copy(isLoading = true)

        viewModelScope.launch {
            try {
                val response: PageResponse<Patient> = ApiClient.api.getPatients(page, 10)
                _state.value = _state.value.copy(
                    items = if (page == 1)
                        response.results
                    else
                        _state.value.items + response.results,
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
}
