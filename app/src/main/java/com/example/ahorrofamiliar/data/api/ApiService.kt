package com.example.ahorrofamiliar.data.api

import Member
import Payment
import com.example.ahorrofamiliar.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("plans")
    suspend fun getPlans(): Response<List<Plan>>

    @GET("plans/{id}")
    suspend fun getPlanDetail(@Path("id") id: String): Response<Plan>

    // VERIFICA ESTOS ENDPOINTS - pueden variar según tu backend
    @GET("payments/plan/{planId}")
    suspend fun getPaymentsByPlan(@Path("planId") planId: String): Response<List<Payment>>

    @GET("members/plan/{planId}")
    suspend fun getMembersByPlan(@Path("planId") planId: String): Response<List<Member>>

    @POST("payments")
    suspend fun createPayment(@Body request: CreatePaymentRequest): Response<Payment>
}