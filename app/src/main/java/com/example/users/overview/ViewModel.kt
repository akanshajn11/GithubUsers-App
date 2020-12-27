package com.example.users.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.users.network.RetrofitApi
import com.example.users.network.UserProperty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModel {

    //Internally mutable LiveData is used, because list of User Property will be updated with new values
    private val _response = MutableLiveData<String>()

    //The external LiveData interface to the property is immutable, so only this class can modify
    val response: LiveData<String>
        get() = _response

    init {
        getUserProperties()
    }

    private fun getUserProperties() {

        //start network request on background thread
        RetrofitApi.retrofitService.getUsers().enqueue(object : Callback<List<UserProperty>> {

            override fun onResponse(
                call: Call<List<UserProperty>>,
                response: Response<List<UserProperty>>
            ) {
                _response.value = "Success: ${response.body()?.size} User properties retrieved"
            }

            override fun onFailure(call: Call<List<UserProperty>>, t: Throwable) {
                _response.value = "Failure: " + t.message
            }

        })

    }
}