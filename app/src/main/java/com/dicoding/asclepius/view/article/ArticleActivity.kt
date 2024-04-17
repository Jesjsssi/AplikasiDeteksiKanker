package com.dicoding.asclepius.view.article

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapter.ArticleAdapter
import com.dicoding.asclepius.data.Result
import com.dicoding.asclepius.databinding.ActivityArticleBinding

class ArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticleBinding
    private lateinit var articleViewModel: ArticleViewModel
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = ""

        articleAdapter = ArticleAdapter()

        val factory = ArticleViewModelFactory.getInstance()
        articleViewModel = ViewModelProvider(this, factory)[ArticleViewModel::class.java]

        binding.rvArticle.setHasFixedSize(true)

        binding.rvArticle. layoutManager = LinearLayoutManager(this)
        binding.rvArticle.adapter = articleAdapter

        articleViewModel.getAllArticles().observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    val articles = result.data
                    articleAdapter.submitList(articles)
                    binding.progressBar.isVisible = false
                }
                is Result.Error -> {
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                    binding.progressBar.isVisible = false
                }
                is Result.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }
    }
}