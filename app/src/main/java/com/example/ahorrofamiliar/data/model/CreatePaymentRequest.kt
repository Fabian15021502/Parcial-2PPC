package com.example.ahorrofamiliar.data.model
data class CreatePaymentRequest(
    val planId: String,
    val memberId: String,
    val monto: Double,
    val fecha: String
)