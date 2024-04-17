package com.dicoding.asclepius.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.Result
import com.dicoding.asclepius.data.remote.ArticlesItem
import com.dicoding.asclepius.data.remote.ApiService

class ArticleRepository(
    private val apiService: ApiService
) {

    fun getSearchNews(): LiveData<Result<List<ArticlesItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getArticle("cancer", "health", "en", BuildConfig.API_KEY)
            if (response.status == "ok") {
                emit(Result.Success(response.articles?.filterNotNull()?: emptyList()))
            } else {
                emit(Result.Error("error di repository"))
            }
        } catch (e: Exception) {
            Log.d("NewsRepository", "getSearchNews: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }
}