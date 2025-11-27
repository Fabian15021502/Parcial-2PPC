package com.example.ahorrofamiliar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ahorrofamiliar.data.model.CreatePlanRequest
import com.example.ahorrofamiliar.data.repository.PlanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreatePlanViewModel(private val repo: PlanRepository) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    fun createPlan(request: CreatePlanRequest) {
        viewModelScope.launch {
            _loading.value = true
            val result = repo.createPlan(request)
            _loading.value = false

            result.onSuccess {
                _message.value = "Plan creado exitosamente"
            }.onFailure {
                _message.value = "Error: ${it.message}"
            }
        }
    }

    //private fun PlanRepository.createPlan(request: CreatePlanRequest) {}
}

class CreatePlanViewModelFactory(
    private val repo: PlanRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CreatePlanViewModel(repo) as T
    }
}
