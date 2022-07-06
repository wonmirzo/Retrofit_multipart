package com.wonmirzo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.wonmirzo.api.ApiClient
import com.wonmirzo.api.ApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class MainActivity : AppCompatActivity() {
    private val PICK_IMAGE_CODE = 1
    private lateinit var apiService: ApiService
    private var catPhoto: File = File("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        apiService = ApiClient.createServiceWithAuth(ApiService::class.java)

        setupUI()
    }

    private fun setupUI() {
        val pickImage = findViewById<Button>(R.id.btnPickImage)
        val uploadImage = findViewById<Button>(R.id.btnUploadImage)

        pickImage.setOnClickListener {
        }

        uploadImage.setOnClickListener {
            val builder: MultipartBody.Builder = MultipartBody.Builder()
            builder.setType(MultipartBody.FORM)

            if (catPhoto.length() > 0) {
                builder.addFormDataPart(
                    "file",
                    catPhoto.name,
                    catPhoto.asRequestBody("image/jpg".toMediaType())
                )
                builder.addFormDataPart("sub_id", "something")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_CODE) {
            val uri = data?.data ?: return
            val path = PathUtil.getPath(this, uri)
            catPhoto = File(path!!)
        }
    }
}