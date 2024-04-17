package com.dicoding.asclepius.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.local.History
import com.dicoding.asclepius.data.local.HistoryDao
import com.dicoding.asclepius.data.local.HistoryDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class HistoryRepository(context: Context) {
    private val mHistoryDao: HistoryDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = HistoryDatabase.getDatabase(context)
        mHistoryDao = db.historyDao()
    }

    fun insert(history: History) {
        executorService.execute { mHistoryDao.insert(history) }
    }

    fun delete(history: History) {
        executorService.execute { mHistoryDao.delete(history) }
    }

    fun getAllHistoryUser(): LiveData<List<History>> = mHistoryDao.getAllHistoryUser()
}