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

    // En ApiService
    @GET("miembros/plan/{planId}")
    suspend fun getMembersByPlan(@Path("planId") planId: String): Response<List<Member>>

    // VERIFICA ESTE ENDPOINT - debe coincidir con tu backend
    @GET("pagos/plan/{planId}")  // O el endpoint correcto según tu backend
    suspend fun getPayments(@Path("planId") planId: String): Response<List<Payment>>

    @POST("pagos")
    suspend fun createPayment(@Body request: CreatePaymentRequest): Response<Payment>
}
