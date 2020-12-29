package com.example.users.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.users.network.Item
import com.example.users.network.ItemResponse
import com.example.users.network.RetrofitApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OverviewViewModel() : ViewModel() {

    //Internally mutable LiveData is used, because list of items can be updated with new values
    private val _items = MutableLiveData<List<Item>>()

    private val _error = MutableLiveData<String>()

    //The external LiveData interface to the property is immutable, so only this class can modify
    val items: LiveData<List<Item>>
        get() = _items

    val error: LiveData<String>
        get() = _error

    fun getUserProperties() {

        //start network request on background thread
        RetrofitApi.retrofitService.getUsers().enqueue(object : Callback<ItemResponse> {

            override fun onResponse(
                call: Call<ItemResponse>,
                response: Response<ItemResponse>
            ) {
                _items.value = response.body()?.items
            }

            override fun onFailure(call: Call<ItemResponse>, t: Throwable) {
                _error.value = "Failure: " + t.message
            }

        })

    }
}