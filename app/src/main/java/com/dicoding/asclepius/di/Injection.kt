package com.dicoding.asclepius.di

import com.dicoding.asclepius.data.remote.ApiConfig
import com.dicoding.asclepius.repository.ArticleRepository

object Injection {
    fun provideRepository(): ArticleRepository{
        val apiService = ApiConfig.getApiConfig()

        return ArticleRepository(apiService)
    }
}