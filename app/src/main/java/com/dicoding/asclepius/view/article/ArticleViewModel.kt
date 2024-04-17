package com.dicoding.asclepius.view.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.Result
import com.dicoding.asclepius.data.remote.ArticlesItem
import com.dicoding.asclepius.repository.ArticleRepository

class ArticleViewModel(private val repository: ArticleRepository) : ViewModel() {
    fun getAllArticles(): LiveData<Result<List<ArticlesItem>>> = repository.getSearchNews()
}
