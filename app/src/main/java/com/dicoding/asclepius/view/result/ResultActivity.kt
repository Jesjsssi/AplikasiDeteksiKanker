package com.dicoding.asclepius.view.result

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.dicoding.asclepius.data.local.History
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.repository.HistoryRepository
import com.dicoding.asclepius.util.ViewModelFactory

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var historyRepository: HistoryRepository
    private var history: History = History()
    private lateinit var viewModel: ResultViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        historyRepository = HistoryRepository(application)

        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        val result = intent.getStringExtra(EXTRA_RESULT)
        val prediction = intent.getStringExtra(EXTRA_PREDICTION)
        val score = intent.getStringExtra(EXTRA_SCORE)

        imageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.resultImage.setImageURI(it)
        }

        history.image = imageUri.toString()
        history.prediction = prediction.toString()
        history.score = score.toString()

        result?.let {
            binding.resultText.text = it
            val history = History(0, it, imageUri.toString())
            val viewModelFactory = ViewModelFactory.getInstance(historyRepository)
            viewModel = viewModelFactory.create(ResultViewModel::class.java)

        }

        binding.save.setOnClickListener {
            viewModel.insert(history)
            Toast.makeText(this, "Save successfully", Toast.LENGTH_SHORT).show()
        }

    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
        const val EXTRA_PREDICTION= "extra_prediction"
        const val EXTRA_SCORE = "extra_score"
    }
}