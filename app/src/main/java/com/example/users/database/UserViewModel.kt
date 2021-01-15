package com.example.users.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

class UserViewModel(database: UserDatabaseDao, application: Application) :
    AndroidViewModel(application) {

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val db = database

    private var _users = MutableLiveData<List<User>>()

    val users: LiveData<List<User>>
        get() = _users

    private var _favUser = MutableLiveData<User>()

    val favUser: LiveData<User>
        get() = _favUser

    //Add user to table
    fun addToFavorites(user: User) {
        uiScope.launch {
            addUserToDb(user)
        }
    }

    private suspend fun addUserToDb(user: User) {
        withContext(Dispatchers.IO) {
            db.insert(user)
        }
    }

    //Get the latest user from table
    fun getLatestUser() {
        uiScope.launch {
            getLatestUserFromDb()
        }
    }

    private suspend fun getLatestUserFromDb(): User? {
        return withContext(Dispatchers.IO) {
            val user: User? = db.getLatestUser()
            user
        }
    }

    //Get list of all favorite users
    fun getAllFavoriteUsers() {
        uiScope.launch {
            _users.value = getAllFavoriteUsersFromDb()
        }
    }

    private suspend fun getAllFavoriteUsersFromDb(): List<User>? {
        return withContext(Dispatchers.IO) {
            val usersList = db.getAllUsers()
            usersList
        }
    }

    fun checkIfFavUser(login: String) {
        uiScope.launch {
            _favUser.value = checkUserInDb(login)
        }
    }

    private suspend fun checkUserInDb(login: String): User {
        return withContext(Dispatchers.IO) {
            val user = db.get(login)
            user
        }
    }

    fun removeFromFav(login: String) {
        uiScope.launch {
            removeUserFromDb(login)
        }
    }

    private suspend fun removeUserFromDb(login: String) {
        withContext(Dispatchers.IO) {
            db.deleteUser(login)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel() // Job would cancel all coroutines
    }
}
