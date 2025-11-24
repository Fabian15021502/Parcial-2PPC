package com.example.ahorrofamiliar.data.api

import com.example.ahorrofamiliar.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("plans")
    suspend fun getPlans(): Response<List<Plan>>

    @GET("plans/{id}")
    suspend fun getPlanDetail(@Path("id") id: Long): Response<Plan>

    @GET("payments/{planId}")
    suspend fun getPayments(@Path("planId") planId: Long): Response<List<Payment>>

    @POST("payments")
    suspend fun createPayment(@Body req: CreatePaymentRequest): Response<Payment>
}
