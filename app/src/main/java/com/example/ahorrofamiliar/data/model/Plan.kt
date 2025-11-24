package com.example.ahorrofamiliar.data.model

data class Plan(
    val id: Long,
    val nombre: String,
    val meta: Double,
    val meses: Int,
    val motivo: String,
    val fechaInicio: String,
    val integrantes: List<Member>
)