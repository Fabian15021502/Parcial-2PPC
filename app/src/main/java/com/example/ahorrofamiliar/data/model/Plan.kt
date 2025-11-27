package com.example.ahorrofamiliar.data.model

import com.google.gson.annotations.SerializedName

data class Plan(
    @SerializedName("_id")
    val id: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("motive")
    val motive: String? = null,

    @SerializedName("targetAmount")
    val targetAmount: Double? = null,

    @SerializedName("months")
    val months: Int? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null
)
