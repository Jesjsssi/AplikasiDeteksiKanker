package com.dicoding.asclepius.view.result

import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.local.History
import com.dicoding.asclepius.repository.HistoryRepository

class ResultViewModel(private val historyRepository: HistoryRepository) : ViewModel(){

    fun insert(history: History) {
        historyRepository.insert(history)
    }
}