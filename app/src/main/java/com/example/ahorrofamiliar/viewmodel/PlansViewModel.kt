package com.example.ahorrofamiliar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ahorrofamiliar.data.model.Plan
import com.example.ahorrofamiliar.data.repository.PlanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlansViewModel(
    private val repo: PlanRepository
) : ViewModel() {

    private val _plans = MutableStateFlow<List<Plan>>(emptyList())
    val plans: StateFlow<List<Plan>> = _plans

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    // ✅ CAMBIO: Ya no es nullable
    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    fun loadPlans() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = "" // Limpiar error previo

            val result = repo.getPlans()
            _loading.value = false

            result.onSuccess {
                _plans.value = it
            }.onFailure {
                _error.value = it.message ?: "Error al cargar planes"
            }
        }
    }

    fun clearError() {
        _error.value = ""
    }
}