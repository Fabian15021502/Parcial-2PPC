package com.example.ahorrofamiliar.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ahorrofamiliar.data.model.*
import com.example.ahorrofamiliar.data.repository.PlanRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlanDetailViewModel(private val repo: PlanRepository) : ViewModel() {

    private val _plan = MutableStateFlow<Plan?>(null)
    val plan: StateFlow<Plan?> = _plan.asStateFlow()

    private val _payments = MutableStateFlow<List<Payment>>(emptyList())
    val payments: StateFlow<List<Payment>> = _payments.asStateFlow()

    private val _members = MutableStateFlow<List<Member>>(emptyList())
    val members: StateFlow<List<Member>> = _members.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()

    fun loadPlan(planId: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val result = repo.getPlanDetail(planId)
                result.onSuccess { planData ->
                    _plan.value = planData
                }.onFailure { exception ->
                    _message.value = "Error al cargar plan: ${exception.message}"
                }
            } catch (e: Exception) {
                _message.value = "Error: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadPayments(planId: String) {
        viewModelScope.launch {
            try {
                val result = repo.getPayments(planId)
                result.onSuccess { paymentsData ->
                    _payments.value = paymentsData
                }.onFailure { exception ->
                    _message.value = "Error al cargar pagos: ${exception.message}"
                }
            } catch (e: Exception) {
                _message.value = "Error: ${e.message}"
            }
        }
    }

    fun loadMembers(planId: String) {
        viewModelScope.launch {
            try {
                val result = repo.getMembersByPlan(planId)
                result.onSuccess { membersData ->
                    _members.value = membersData
                }.onFailure { exception ->
                    _message.value = "Error al cargar miembros: ${exception.message}"
                }
            } catch (e: Exception) {
                _message.value = "Error: ${e.message}"
            }
        }
    }

    fun loadPlanDetails(planId: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                // Cargar plan, pagos y miembros en paralelo
                val planDeferred = async { repo.getPlanDetail(planId) }
                val paymentsDeferred = async { repo.getPayments(planId) }
                val membersDeferred = async { repo.getMembersByPlan(planId) }

                // Esperar todos los resultados
                val planResult = planDeferred.await()
                val paymentsResult = paymentsDeferred.await()
                val membersResult = membersDeferred.await()

                // Procesar resultados usando onSuccess/onFailure
                planResult.onSuccess { planData -> _plan.value = planData }
                    .onFailure { exception -> _message.value = "Error al cargar plan: ${exception.message}" }

                paymentsResult.onSuccess { paymentsData -> _payments.value = paymentsData }
                    .onFailure { exception -> _message.value = "Error al cargar pagos: ${exception.message}" }

                membersResult.onSuccess { membersData -> _members.value = membersData }
                    .onFailure { exception -> _message.value = "Error al cargar miembros: ${exception.message}" }

            } catch (e: Exception) {
                _message.value = "Error: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun registerPayment(request: CreatePaymentRequest) {
        viewModelScope.launch {
            try {
                val result = repo.createPayment(request)
                result.onSuccess { payment ->
                    _message.value = "Pago registrado exitosamente"
                    request.planId?.let { loadPayments(it) } // actualizar lista de pagos
                }.onFailure { exception ->
                    _message.value = "Error al registrar pago: ${exception.message}"
                }
            } catch (e: Exception) {
                _message.value = "Error: ${e.message}"
            }
        }
    }

}