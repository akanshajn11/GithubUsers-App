package com.example.users.overview

import android.content.Intent
import android.os.Bundle
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

class MainActivity() : AppCompatActivity(), OverviewAdapter.OnUserClickListener {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OverviewAdapter
    private lateinit var viewModel: OverviewViewModel
    private lateinit var databaseViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val application = requireNotNull(this).application
        val dataSource = UserDatabase.getInstance(application).userDatabaseDao
        val databaseViewModelFactory = UserViewModelFactory(dataSource, application)
        databaseViewModel =
            ViewModelProvider(this, databaseViewModelFactory).get(UserViewModel::class.java)
        binding.databaseViewModel = databaseViewModel

        binding.lifecycleOwner = this

        val viewModelFactory = OverviewViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(OverviewViewModel::class.java)

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
}

