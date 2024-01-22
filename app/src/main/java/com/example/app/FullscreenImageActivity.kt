package com.example.app

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

// 3rd party library Picasso for loading images
import com.squareup.picasso.Picasso

class FullscreenImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen_image)

        // Find the fullscreenImageView and get the image-url that we passed through when we clicked on the image
        val imageView = findViewById<ImageView>(R.id.fullscreenImageView)
        val imageUrl = intent.getStringExtra("IMAGE_URL")

        // Load the image using Picasso
        Picasso.get().load(imageUrl).into(imageView)

        // Clicking the image again closes it
        imageView.setOnClickListener {
            finish()
        }
    }
}