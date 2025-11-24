package com.example.ahorrofamiliar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ahorrofamiliar.data.repository.PlanRepository

class PlansViewModelFactory(
    private val repo: PlanRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlansViewModel::class.java)) {
            return PlansViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
