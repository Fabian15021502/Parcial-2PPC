package com.example.ahorrofamiliar.viewmodel

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

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun loadPlan(id: Long) {
        viewModelScope.launch {
            repo.getPlanDetail(id).onSuccess {
                _plan.value = it
            }.onFailure {
                _message.value = it.message
            }
        }
    }

    fun loadPayments(id: Long) {
        viewModelScope.launch {
            repo.getPayments(id).onSuccess {
                _payments.value = it
            }.onFailure {
                _message.value = it.message
            }
        }
    }

    fun registerPayment(request: CreatePaymentRequest) {
        viewModelScope.launch {
            repo.createPayment(request).onSuccess {
                _message.value = "Pago registrado"
                loadPayments(request.planId)
            }.onFailure {
                _message.value = it.message
            }
        }
    }
}
