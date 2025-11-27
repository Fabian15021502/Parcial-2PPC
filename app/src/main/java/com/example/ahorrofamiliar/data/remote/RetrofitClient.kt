package com.example.ahorrofamiliar.data.remote

import android.os.Build
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // Para emulador de Android Studio
    private const val EMULATOR_URL = "http://10.0.2.2:4000/api/"
    // Para dispositivos reales: Pega aquí tu URL de ngrok
    private const val NGROK_URL = "https://overexcitable-coral-orogenetic.ngrok-free.dev/api/"
    // Para producción
    private const val PRODUCTION_URL = "https://tu-api.com/api/"
    private const val IS_PRODUCTION = false

    // Detecta automáticamente si es emulador o dispositivo real
    private val BASE_URL = when {
        IS_PRODUCTION -> PRODUCTION_URL
        isEmulator() -> EMULATOR_URL
        else -> NGROK_URL
    }

    // Detecta si es un emulador
    private fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("google/sdk_gphone")
                || Build.FINGERPRINT.contains("generic")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.BRAND.startsWith("generic"))
    }

    private val logging = HttpLoggingInterceptor().apply {
        level = if (IS_PRODUCTION) {
            HttpLoggingInterceptor.Level.NONE
        } else {
            HttpLoggingInterceptor.Level.BODY
        }
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    private val gson: Gson = GsonBuilder()
        .setLenient()
        .serializeNulls()
        .create()

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    // Útil para debugging
    fun getCurrentBaseUrl(): String = BASE_URL
    fun isRunningOnEmulator(): Boolean = isEmulator()
}