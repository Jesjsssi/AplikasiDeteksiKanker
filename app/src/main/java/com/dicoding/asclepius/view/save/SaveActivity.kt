package com.dicoding.asclepius.view.save

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapter.HistoryAdapter
import com.dicoding.asclepius.databinding.ActivitySaveBinding
import com.dicoding.asclepius.repository.HistoryRepository
import com.dicoding.asclepius.util.ViewModelFactory

class SaveActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySaveBinding
    private lateinit var viewModel: SaveViewModel
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val historyRepository = HistoryRepository(this)
        val factory = ViewModelFactory.getInstance(historyRepository)
        viewModel = ViewModelProvider(this, factory)[SaveViewModel::class.java]

        adapter = HistoryAdapter()
        val layoutManager = LinearLayoutManager(this)
        binding.rvSave.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvSave.addItemDecoration(itemDecoration)
        binding.rvSave.adapter = adapter

        adapter.onClick = { history ->
            viewModel.delete(history)
        }

        viewModel.getAllHistoryUser().observe(this, Observer { list ->
            if (list != null) {
                adapter.submitList(list)
                if (list.isEmpty()) {
                    binding.rvSave.visibility = View.GONE
                } else {
                    binding.rvSave.visibility = View.VISIBLE
                }
            }
        })
    }
}