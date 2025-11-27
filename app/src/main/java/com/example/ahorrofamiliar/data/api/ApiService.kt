package com.example.ahorrofamiliar.data.api


import com.example.ahorrofamiliar.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ✔ PLANES
    @GET("plans")
    suspend fun getPlans(): Response<List<Plan>>

    @GET("plans/{id}")
    suspend fun getPlanDetail(@Path("id") id: String): Response<Plan>

    @POST("plans")
    suspend fun createPlan(@Body request: CreatePlanRequest): Response<Plan>


    // ✔ MIEMBROS
    @POST("members")
    suspend fun createMember(@Body request: CreateMemberRequest): Response<Member>

    @GET("members/plan/{planId}")
    suspend fun getMembersByPlan(@Path("planId") planId: String): Response<List<Member>>

    @GET("members")
    suspend fun getAllMembers(): Response<List<Member>>


    // ✔ PAGOS
    @POST("payments")
    suspend fun createPayment(@Body request: CreatePaymentRequest): Response<Payment>

    @GET("payments/plan/{planId}")
    suspend fun getPaymentsByPlan(@Path("planId") planId: String): Response<List<Payment>>

    companion object
}
