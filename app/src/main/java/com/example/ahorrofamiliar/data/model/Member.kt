package com.example.ahorrofamiliar.data.model

import com.google.gson.annotations.SerializedName

data class Member(
    @SerializedName("_id")
    val id: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("planId")
    val planId: String? = null,

    @SerializedName("contributionPerMonth")
    val contributionPerMonth: Double? = null,

    @SerializedName("joinedAt")
    val joinedAt: String? = null
)
