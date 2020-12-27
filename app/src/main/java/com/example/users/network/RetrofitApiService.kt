package com.example.users.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

/*This class holds the network layer for the app. This is the API which our ViewModel will use
to communicate with the web service. In this class Retrofit service API is implemented*/

//Base URL for the Web Service
private const val BASE_URL = "https://api.github.com"

//Creating Retrofit object using Retrofit builder
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

/* Retrofit needs at least two things available to it to build a web services API
1.Base URI for the web service
2. Converter Factory - tells Retrofit what do with the data it gets back from the web service
In this case, you want Retrofit to fetch a JSON response from the web service, and return it as a String

Retrofit has a ScalarsConverter that supports strings and other primitive types,
so you call addConverterFactory() on the builder with an instance of ScalarsConverterFactory
*/


//Below interface defines how Retrofit talks to the web server using HTTP requests
interface RetrofitApiService {
    @GET("/search/users?q=language:android+location:barcelona")
    fun getUsers():
            Call<String>
}
/*
@GET : We use this annotation and specify path or end point for the web service method
getUsers() : When this method is invoked, Retrofit appends the given end point to base url and creates call object
Call : This object starts the request
 */

//Below public object will initialize the Retrofit service
object RetrofitApi {
    val retrofitService: RetrofitApiService by lazy {
        retrofit.create(RetrofitApiService::class.java)
    }
}
/*
create() : Creates Retrofit service with RetrofitApiService interface
RetrofitApi : Public object to expose the service to rest of the app. Inside this object, we lazily initialize Retrofit service

So when app calls RetrofitApi.retrofitService, it will get singleton Retrofit object that implements RetrofitApiService
 */