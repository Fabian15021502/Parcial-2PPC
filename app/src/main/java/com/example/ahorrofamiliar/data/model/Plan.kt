package com.example.ahorrofamiliar.data.model

import Member
import com.google.gson.annotations.SerializedName

data class Plan(
    @SerializedName("_id")
    val id: String? = null,

    @SerializedName("name")  // Cambiado de "nombre" a "name"
    val nombre: String? = null,

    @SerializedName("targetAmount")  // Cambiado de "meta" a "targetAmount"
    val meta: Double? = null,

    @SerializedName("months")  // Cambiado de "meses" a "months"
    val meses: Int? = null,

    @SerializedName("motive")  // Cambiado de "motivo" a "motive"
    val motivo: String? = null,

    @SerializedName("createdAt")  // Cambiado de "fechaInicio" a "createdAt"
    val fechaInicio: String? = null,

    @SerializedName("integrantes")
    val integrantes: List<Member>? = null
)