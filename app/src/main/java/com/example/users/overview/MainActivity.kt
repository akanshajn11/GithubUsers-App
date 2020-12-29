package com.example.users.overview

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.users.R

class MainActivity() : AppCompatActivity() {

    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: OverviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModelFactory = OverviewViewModelFactory()
        val viewModel by lazy {
            ViewModelProvider(this, viewModelFactory).get(OverviewViewModel::class.java)
        }

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
            adapter = OverviewAdapter(itemList)
            recyclerView.adapter = adapter
            swipeRefreshLayout.isRefreshing = false
        }

        viewModel.error.observe(this) { error ->
            Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
            swipeRefreshLayout.isRefreshing = false

        }
    }
}

