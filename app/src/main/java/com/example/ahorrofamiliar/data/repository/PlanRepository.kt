package com.example.ahorrofamiliar.data.repository

import Payment
import com.example.ahorrofamiliar.data.api.ApiService
import com.example.ahorrofamiliar.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlanRepository(private val api: ApiService) {

    suspend fun createPayment(request: CreatePaymentRequest): Result<Payment> = safeCall {
        api.createPayment(request)
    }

    suspend fun getPlans(): Result<List<Plan>> = safeCall {
        println("DEBUG - Calling: GET /api/plans")
        api.getPlans()
    }

    suspend fun getPlanDetail(id: String): Result<Plan> = safeCall {
        println("DEBUG - Calling: GET /api/plans/$id")
        api.getPlanDetail(id)
    }

    suspend fun getPayments(planId: String): Result<List<Payment>> = safeCall {
        println("DEBUG - Calling: GET /api/payments/plan/$planId")
        api.getPayments(planId)
    }
    private suspend fun <T> safeCall(call: suspend () -> retrofit2.Response<T>): Result<T> =
        withContext(Dispatchers.IO) {
            try {
                val res = call()
                if (res.isSuccessful && res.body() != null)
                    Result.success(res.body()!!)
                else Result.failure(Exception("Error: ${res.code()}"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
