package com.example.ahorrofamiliar.viewmodel

import Payment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ahorrofamiliar.data.model.*
import com.example.ahorrofamiliar.data.repository.PlanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlanDetailViewModel(
    private val repo: PlanRepository
) : ViewModel() {

    private val _plan = MutableStateFlow<Plan?>(null)
    val plan: StateFlow<Plan?> = _plan

    private val _payments = MutableStateFlow<List<Payment>>(emptyList())
    val payments: StateFlow<List<Payment>> = _payments

    // ✅ CAMBIO: Ya no es nullable
    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun loadPlan(id: String) {
        viewModelScope.launch {
            _loading.value = true
            repo.getPlanDetail(id).onSuccess {
                _plan.value = it
                _loading.value = false
            }.onFailure {
                _message.value = it.message ?: "Error al cargar el plan"
                _loading.value = false
            }
        }
    }

    fun loadPayments(id: String) {
        viewModelScope.launch {
            repo.getPayments(id).onSuccess {
                _payments.value = it
            }.onFailure {
                _message.value = it.message ?: "Error al cargar pagos"
            }
        }
    }

    fun registerPayment(request: CreatePaymentRequest) {
        viewModelScope.launch {
            repo.createPayment(request).onSuccess {
                _message.value = "Pago registrado exitosamente"
                loadPayments(request.planId)
            }.onFailure {
                _message.value = it.message ?: "Error al registrar pago"
            }
        }
    }

    fun clearMessage() {
        _message.value = ""
    }
}
