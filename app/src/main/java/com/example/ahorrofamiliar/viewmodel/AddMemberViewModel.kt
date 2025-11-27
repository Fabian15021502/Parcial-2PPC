package com.example.ahorrofamiliar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ahorrofamiliar.data.model.CreateMemberRequest
import com.example.ahorrofamiliar.data.model.Member
import com.example.ahorrofamiliar.data.repository.PlanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddMemberViewModel(private val repo: PlanRepository) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    /**
     * Agrega un miembro usando CreateMemberRequest (lo que el backend espera).
     */
    fun addMember(name: String, planId: String, amount: Double) {
        if (name.isBlank() || planId.isBlank() || amount <= 0.0) {
            _message.value = "Todos los campos son obligatorios y el aporte debe ser mayor a 0"
            return
        }

        viewModelScope.launch {
            _loading.value = true
            _message.value = ""

            val req = CreateMemberRequest(
                name = name.trim(),
                planId = planId,
                contributionPerMonth = amount
            )

            val result = repo.createMember(req)

            _loading.value = false

            result.onSuccess { created: Member ->
                _message.value = "Miembro agregado correctamente"
                _success.value = true
            }.onFailure { err ->
                _message.value = err.message ?: "Error agregando miembro"
            }
        }
    }

    fun clearMessage() {
        _message.value = ""
    }
}
