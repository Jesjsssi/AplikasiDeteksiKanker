package com.dicoding.asclepius.view.main

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.view.article.ArticleActivity
import com.dicoding.asclepius.view.result.ResultActivity
import com.dicoding.asclepius.view.save.SaveActivity
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    private var result:String? = null
    private var currentImageUri: Uri? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = Intent(this, SaveActivity::class.java)

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let {
                analyzeImage(data)
            } ?: run {
                showToast(getString(R.string.empty_image_warning))
            }
        }

        binding.article.setOnClickListener{
            val intent = Intent(this, ArticleActivity::class.java)
            startActivity(intent)
        }

        binding.history.setOnClickListener{
            startActivity(data)
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            showToast("No Image is Selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            launchCropActivity(it)
            Log.d("Image URI", "showImage: $it")
        }
    }

    private var cropActivityResultLauncher = registerForActivityResult(CropImageContract()
    ) { cropResult: CropImageView.CropResult ->
        if (cropResult.isSuccessful) {
            val croppedBitmap =
                BitmapFactory.decodeFile(cropResult.getUriFilePath(applicationContext, true))
            binding.previewImageView.setImageBitmap(croppedBitmap)
            currentImageUri = cropResult.uriContent
        }
    }

    private fun launchCropActivity(uri: Uri) {
        cropActivityResultLauncher.launch(
            CropImageContractOptions(
                uri = uri,
                cropImageOptions = CropImageOptions(
                    guidelines = CropImageView.Guidelines.ON
                )
            )
        )
    }

    private fun analyzeImage(intent: Intent) {
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    runOnUiThread {
                        results?.let { it ->
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                println(it)
                                val sortedCategories =
                                    it[0].categories.sortedByDescending { it?.score }
                                val displayResult =
                                    sortedCategories.joinToString("\n") {
                                        "${it.label} " + NumberFormat.getPercentInstance()
                                            .format(it.score).trim()
                                    }
                                val score = NumberFormat.getPercentInstance().format(sortedCategories[0].score)
                                val prediction = sortedCategories[0].label
                                moveToResult(displayResult, prediction, score)
                            } else {
                                showToast(getString(R.string.no_result_found))
                            }
                        }
                    }
                }
            }
        )
        currentImageUri?.let { this.imageClassifierHelper.classifyStaticImage(it) }
        intent.putExtra(ResultActivity.EXTRA_RESULT, result)
    }


    private fun moveToResult(result: String, prediction: String, score : String) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_RESULT, result)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, currentImageUri.toString())
        intent.putExtra(ResultActivity.EXTRA_PREDICTION, prediction)
        intent.putExtra(ResultActivity.EXTRA_SCORE, score)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}