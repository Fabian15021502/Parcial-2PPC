package com.example.ahorrofamiliar.data.model

data class Payment(
    val id: Long,
    val planId: Long,
    val memberId: Long,
    val monto: Double,
    val fecha: String
)