package com.example.ahorrofamiliar.data.repository

import com.example.ahorrofamiliar.data.api.ApiService
import com.example.ahorrofamiliar.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlanRepository(private val api: ApiService) {

    suspend fun getPlans(): Result<List<Plan>> = safeCall {
        api.getPlans()
    }

    suspend fun getPlanDetail(id: Long): Result<Plan> = safeCall {
        api.getPlanDetail(id)
    }

    suspend fun getPayments(planId: Long): Result<List<Payment>> = safeCall {
        api.getPayments(planId)
    }

    suspend fun createPayment(request: CreatePaymentRequest): Result<Payment> = safeCall {
        api.createPayment(request)
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
