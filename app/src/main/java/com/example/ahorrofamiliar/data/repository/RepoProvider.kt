package com.example.ahorrofamiliar.data.repository

import com.example.ahorrofamiliar.data.remote.RetrofitClient
import com.example.ahorrofamiliar.data.api.ApiService

object RepoProvider {
    val planRepo: PlanRepository by lazy {
        val api = RetrofitClient.instance.create(ApiService::class.java)
        PlanRepository(api)
    }
}