package com.example.users.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


//A class that would know how to create Overview view model
class OverviewViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OverviewViewModel::class.java)) {
            return OverviewViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}