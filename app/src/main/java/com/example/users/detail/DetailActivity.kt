package com.example.users.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.users.R
import com.example.users.database.User
import com.example.users.database.UserDatabase
import com.example.users.database.UserViewModel
import com.example.users.database.UserViewModelFactory
import kotlin.properties.Delegates

class DetailActivity : AppCompatActivity() {

    private lateinit var databaseViewModel: UserViewModel
    private lateinit var login: String
    private lateinit var avatar: String
    private lateinit var html: String
    private var isFavUser by Delegates.notNull<Boolean>()
    private var isMenuCreated: Boolean = false

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

        var favItem: MenuItem? = menu?.findItem(R.id.favFilledBtn)
        var nonFavItem: MenuItem? = menu?.findItem(R.id.favBorderBtn)

        if (!isMenuCreated) {
            databaseViewModel.favUser.observe(this) { user ->
                isFavUser = user != null
                updateMenuItems(favItem, nonFavItem)
                isMenuCreated = true
            }
        } else
            updateMenuItems(favItem, nonFavItem)

        return super.onCreateOptionsMenu(menu)
    }

    private fun updateMenuItems(favItem: MenuItem?, nonFavItem: MenuItem?) {
        if (isFavUser) {

            if (favItem != null) favItem.isVisible = true
            if (nonFavItem != null) nonFavItem.isVisible = false

        } else {
            if (nonFavItem != null) nonFavItem.isVisible = true
            if (favItem != null) favItem.isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (!isFavUser) {
            isFavUser = true
            invalidateOptionsMenu()
            databaseViewModel.addToFavorites(
                User(
                    login = login,
                    avatarUrl = avatar,
                    htmlUrl = html
                )
            )
            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()

        } else {
            isFavUser = false
            invalidateOptionsMenu()
            databaseViewModel.removeFromFav(login)
            Toast.makeText(this, "Removed from favorites", Toast.LENGTH_LONG).show()
        }
        return super.onOptionsItemSelected(item)
    }

}