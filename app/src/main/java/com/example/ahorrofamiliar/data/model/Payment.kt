package com.example.ahorrofamiliar.data.model

import com.google.gson.annotations.SerializedName

data class Payment(
    @SerializedName("_id")
    val id: String? = null,

    @SerializedName("planId")
    val planId: String? = null,

    @SerializedName("memberId")
    val memberId: String? = null,

    @SerializedName("amount")
    val amount: Double? = null,

    @SerializedName("date")
    val date: String? = null
)
