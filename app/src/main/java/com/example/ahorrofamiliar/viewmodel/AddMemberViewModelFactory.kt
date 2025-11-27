package com.example.ahorrofamiliar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ahorrofamiliar.data.repository.PlanRepository

class AddMemberViewModelFactory(
    private val repo: PlanRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddMemberViewModel(repo) as T
    }
}
