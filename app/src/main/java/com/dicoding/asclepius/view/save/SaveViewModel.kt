package com.dicoding.asclepius.view.save

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.local.History
import com.dicoding.asclepius.repository.HistoryRepository

class SaveViewModel(private val historyRepository: HistoryRepository) : ViewModel() {
    fun delete(history: History) {
        historyRepository.delete(history)
    }

    fun getAllHistoryUser(): LiveData<List<History>> = historyRepository.getAllHistoryUser()
}