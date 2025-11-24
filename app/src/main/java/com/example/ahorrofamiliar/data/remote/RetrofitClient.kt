package com.example.ahorrofamiliar.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // Cambia esta IP por la correcta de tu servidor
    private const val BASE_URL = "http://10.0.2.2:4000/api/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(30, TimeUnit.SECONDS) // Timeout de conexión
        .readTimeout(30, TimeUnit.SECONDS)    // Timeout de lectura
        .writeTimeout(30, TimeUnit.SECONDS)   // Timeout de escritura
        .retryOnConnectionFailure(true)       // Reintentar en fallos de conexión
        .build()

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    // Servicio de API
    val apiService: ApiService by lazy {
        instance.create(ApiService::class.java)
    }
}

// Interface para los endpoints de la API
interface ApiService {
    // Aquí agregarás tus endpoints específicos
    // Por ejemplo:
    // @GET("plans")
    // suspend fun getPlans(): Response<List<Plan>>

    // Endpoint de prueba
    @GET("test")
    suspend fun testConnection(): retrofit2.Response<Map<String, String>>
}