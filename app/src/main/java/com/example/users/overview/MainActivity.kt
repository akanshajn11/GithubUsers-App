package com.example.users.overview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil

import androidx.lifecycle.ViewModelProvider
import com.example.users.R
import com.example.users.databinding.ActivityMainBinding

class MainActivity() : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        val viewModelFactory: OverviewViewModelFactory = OverviewViewModelFactory()

        val viewModel by lazy {
            ViewModelProvider(this, viewModelFactory).get(OverviewViewModel::class.java)
        }

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

    }
}


