package com.example.ahorrofamiliar.data.repository

import com.example.ahorrofamiliar.data.api.ApiService
import com.example.ahorrofamiliar.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlanRepository(private val api: ApiService) {

    suspend fun getPlans(): Result<List<Plan>> = safeCall {
        api.getPlans()
    }

    suspend fun getPlanDetail(id: String): Result<Plan> = safeCall {
        api.getPlanDetail(id)
    }

    suspend fun getPayments(planId: String): Result<List<Payment>> = safeCall {
        api.getPaymentsByPlan(planId)
    }

    suspend fun getMembersByPlan(planId: String): Result<List<Member>> = safeCall {
        api.getMembersByPlan(planId)
    }

    suspend fun createPayment(request: CreatePaymentRequest): Result<Payment> = safeCall {
        api.createPayment(request)
    }

    suspend fun createMember(request: CreateMemberRequest): Result<Member> = safeCall {
        api.createMember(request)
    }

    suspend fun createPlan(request: CreatePlanRequest) =
        safeCall { api.createPlan(request) }


    private suspend fun <T> safeCall(call: suspend () -> retrofit2.Response<T>): Result<T> =
        withContext(Dispatchers.IO) {
            try {
                val res = call()
                if (res.isSuccessful && res.body() != null) {
                    Result.success(res.body()!!)
                } else {
                    Result.failure(Exception("Error: ${res.code()} - ${res.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}