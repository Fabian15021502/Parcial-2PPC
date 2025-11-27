package com.example.ahorrofamiliar.data.model

data class CreateMemberRequest(
    val name: String,
    val planId: String,
    val contributionPerMonth: Double
)
