package com.dicoding.asclepius.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.repository.HistoryRepository
import com.dicoding.asclepius.view.result.ResultViewModel
import com.dicoding.asclepius.view.save.SaveViewModel

class ViewModelFactory(private val historyRepository: HistoryRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(SaveViewModel::class.java) -> {
                return SaveViewModel(historyRepository) as T
            }

            modelClass.isAssignableFrom(ResultViewModel::class.java) -> {
                return ResultViewModel(historyRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(historyRepository: HistoryRepository): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(historyRepository).also { INSTANCE = it }
            }
        }
    }
}