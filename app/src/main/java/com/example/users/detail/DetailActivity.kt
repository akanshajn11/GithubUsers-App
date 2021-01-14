package com.example.users.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.users.R
import com.example.users.database.User
import com.example.users.database.UserDatabase
import com.example.users.database.UserViewModel
import com.example.users.database.UserViewModelFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var databaseViewModel: UserViewModel
    private lateinit var login: String
    private lateinit var avatar: String
    private lateinit var html: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail)

        login = intent.extras?.getString("login") ?: ""
        val username: TextView = findViewById(R.id.username)
        username.text = login

        html = intent.extras?.getString("htmlUrl") ?: ""
        val htmlUrl: TextView = findViewById(R.id.link)
        htmlUrl.text = html

        avatar = (intent.extras?.getString("avatarUrl")) ?: ""
        val userImage: ImageView = findViewById(R.id.userImageHeader)
        Glide.with(this)
            .load(intent.extras?.getString("avatarUrl"))
            .into(userImage)

        val databaseViewModelFactory = UserViewModelFactory(
            UserDatabase.getInstance(requireNotNull(this).application).userDatabaseDao,
            requireNotNull(this).application
        )
        databaseViewModel =
            ViewModelProvider(this, databaseViewModelFactory).get(UserViewModel::class.java)

        databaseViewModel.checkIfFavUser(login)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        databaseViewModel.user.observe(this) { user ->
            if (user != null) {
                var item: MenuItem? = menu?.findItem(R.id.favBtn)
                if (item != null)
                    item.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_24)
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_24)
        Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()
        databaseViewModel.addToFavorites(User(login = login, avatarUrl = avatar, htmlUrl = html))
        return super.onOptionsItemSelected(item)
    }
}