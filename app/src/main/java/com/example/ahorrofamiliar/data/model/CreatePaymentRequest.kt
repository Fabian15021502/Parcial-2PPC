package com.example.ahorrofamiliar.data.model

import com.google.gson.annotations.SerializedName

data class CreatePaymentRequest(
    @SerializedName("planId")
    val planId: String,

    @SerializedName("memberId")
    val memberId: String,

    @SerializedName("amount")
    val amount: Double,

    @SerializedName("date")
    val date: String
)
