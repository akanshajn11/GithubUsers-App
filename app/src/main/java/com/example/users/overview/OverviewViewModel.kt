package com.example.users.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.users.network.ItemResponse
import com.example.users.network.RetrofitApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OverviewViewModel() : ViewModel() {

    //Internally mutable LiveData is used, because list of items can be updated with new values
    private val _response = MutableLiveData<ItemResponse>()

    //The external LiveData interface to the property is immutable, so only this class can modify
    val response: LiveData<ItemResponse>
        get() = _response

    init {
        getUserProperties()
    }

    private fun getUserProperties() {

        //start network request on background thread
        RetrofitApi.retrofitService.getUsers().enqueue(object : Callback<ItemResponse> {

            override fun onResponse(
                call: Call<ItemResponse>,
                response: Response<ItemResponse>
            ) {
                _response.value = response.body()
            }

            override fun onFailure(call: Call<ItemResponse>, t: Throwable) {

            }

        })

    }
}