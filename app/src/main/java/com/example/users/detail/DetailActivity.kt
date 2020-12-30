package com.example.users.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.users.R

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val username: TextView = findViewById(R.id.username)
        username.text = intent.extras?.getString("login")

        val htmlUrl: TextView = findViewById(R.id.githubLink1)
        htmlUrl.text = intent.extras?.getString("htmlUrl")

        val userImage: ImageView = findViewById(R.id.userImageHeader)

        Glide.with(this)
            .load(intent.extras?.getString("avatarUrl"))
            .into(userImage)

    }
}