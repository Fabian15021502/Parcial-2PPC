package com.example.ahorrofamiliar.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ahorrofamiliar.data.model.CreatePaymentRequest
import com.example.ahorrofamiliar.data.model.Payment
import com.example.ahorrofamiliar.data.repository.PlanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class PaymentViewModel(private val repository: PlanRepository) : ViewModel() {

    // Estado de registro de pago
    private val _paymentState = MutableStateFlow<Result<Payment>?>(null)
    val paymentState: StateFlow<Result<Payment>?> = _paymentState

    // Lista de pagos del plan
    private val _payments = MutableStateFlow<List<Payment>>(emptyList())
    val payments: StateFlow<List<Payment>> = _payments

    // Función para registrar pago y actualizar la lista automáticamente
    fun registerPayment(planId: String, memberId: String, monto: Double) {
        viewModelScope.launch {
            val request = CreatePaymentRequest(
                planId = planId,
                memberId = memberId,
                amount = monto,
                date = LocalDate.now().toString()
            )

            val result = repository.createPayment(request)
            _paymentState.value = result

            if (result.isSuccess) {
                // Recargar la lista de pagos del plan
                val paymentsResult = repository.getPayments(planId)
                _payments.value = paymentsResult.getOrDefault(emptyList())
            }
        }
    }

    // Función para cargar pagos manualmente si se necesita
    fun loadPayments(planId: String) {
        viewModelScope.launch {
            val paymentsResult = repository.getPayments(planId)
            _payments.value = paymentsResult.getOrDefault(emptyList())
        }
    }
}
