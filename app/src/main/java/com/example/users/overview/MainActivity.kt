package com.example.users.overview

import  android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.users.R
import com.example.users.database.UserDatabase
import com.example.users.database.UserViewModel
import com.example.users.database.UserViewModelFactory
import com.example.users.databinding.ActivityMainBinding
import com.example.users.detail.DetailActivity
import com.example.users.network.Item

class MainActivity() : AppCompatActivity(), OverviewAdapter.OnUserClickListener {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OverviewAdapter
    private lateinit var viewModel: OverviewViewModel
    private lateinit var databaseViewModel: UserViewModel
    private lateinit var mode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val databaseViewModelFactory = UserViewModelFactory(
            UserDatabase.getInstance(requireNotNull(this).application).userDatabaseDao,
            requireNotNull(this).application
        )
        databaseViewModel =
            ViewModelProvider(this, databaseViewModelFactory).get(UserViewModel::class.java)

        binding.databaseViewModel = databaseViewModel
        binding.lifecycleOwner = this

        viewModel =
            ViewModelProvider(this, OverviewViewModelFactory()).get(OverviewViewModel::class.java)

        swipeRefreshLayout = findViewById(R.id.swipeContainer)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchUsers(viewModel)

        swipeRefreshLayout.setOnRefreshListener {
            fetchUsers(viewModel)
            Toast.makeText(applicationContext, "Github users refreshed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchUsers(viewModel: OverviewViewModel) {
        title = "Users"
        mode = "All"
        swipeRefreshLayout.isRefreshing = true

        viewModel.getUserProperties()

        viewModel.items.observe(this) { itemList ->
            adapter = OverviewAdapter(itemList, this)
            recyclerView.adapter = adapter
            swipeRefreshLayout.isRefreshing = false
        }

        viewModel.error.observe(this) { error ->
            Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    //Method from OnUserClickListener interface
    override fun onUserClick(position: Int) {
        val item = viewModel.items.value?.get(position)
        val intent: Intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("login", item?.login)
        intent.putExtra("htmlUrl", item?.htmlUrl)
        intent.putExtra("avatarUrl", item?.avatarUrl)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.overview_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (menu != null) {
            if (mode == "fav") { //Favorite users mode
                menu.findItem(R.id.favList).isVisible = false
                menu.findItem(R.id.allUsers).isVisible = true
            }
            if (mode == "All") { //All users mode
                menu.findItem(R.id.allUsers).isVisible = false
                menu.findItem(R.id.favList).isVisible = true
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.title == "Go to Favorite Users")
            fetchFavoriteUsers()
        else
            fetchUsers(viewModel)
        return super.onOptionsItemSelected(item)
    }

    private fun fetchFavoriteUsers() {
        title = "Favorites"
        mode = "fav"
        swipeRefreshLayout.isRefreshing = true
        databaseViewModel.getAllFavoriteUsers()
        databaseViewModel.users.observe(this) { usersList ->
            val itemList: List<Item> = usersList.map { Item(it.login, it.avatarUrl, it.htmlUrl) }
            adapter = OverviewAdapter(itemList, this)
            recyclerView.adapter = adapter
            swipeRefreshLayout.isRefreshing = false
        }
    }
}

