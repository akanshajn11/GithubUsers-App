package com.example.users.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.users.R

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail)

        val username: TextView = findViewById(R.id.username)
        username.text = intent.extras?.getString("login")

        val htmlUrl: TextView = findViewById(R.id.link)
        htmlUrl.text = intent.extras?.getString("htmlUrl")

        val userImage: ImageView = findViewById(R.id.userImageHeader)

        Glide.with(this)
            .load(intent.extras?.getString("avatarUrl"))
            .into(userImage)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        item.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_24)
        Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()
        return super.onOptionsItemSelected(item)
    }

}