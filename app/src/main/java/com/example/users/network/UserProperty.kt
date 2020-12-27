package com.example.users.network

import com.squareup.moshi.Json

/*Moshi parses the JSON data and converts it into Kotlin objects.
We need a Kotlin data class to store the parsed results
 */

data class UserProperty (
    val login: String,
    @Json(name = "avatar_url") val avatarUrl: String,
    @Json(name= "html_url") val htmlUrl: String
)

/*Each of the variables in the UserProperty class corresponds to a key name in the JSON object.
When Moshi parses the JSON, it matches the keys by name and fills the data objects with appropriate values*/

/*
@Json annotation : To use variable names in your data class that differ from the key names in the JSON response.
Kotlin properties commonly use upper and lowercase letters ("camel case")
 */
