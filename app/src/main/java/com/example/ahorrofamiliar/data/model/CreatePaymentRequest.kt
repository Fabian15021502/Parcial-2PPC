package com.example.ahorrofamiliar.data.model

data class CreatePaymentRequest(
    val planId: Long,
    val memberId: Long,
    val monto: Double,
    val fecha: String
)