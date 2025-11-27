package com.example.ahorrofamiliar.data.model

data class CreatePlanRequest(
    val name: String,
    val motive: String,
    val targetAmount: Double,
    val months: Int
)