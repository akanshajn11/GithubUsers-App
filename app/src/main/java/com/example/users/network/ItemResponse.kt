package com.example.users.network

import com.google.gson.annotations.SerializedName

data class ItemResponse(

    @SerializedName("items") val items: List<Item> = listOf<Item>()
)