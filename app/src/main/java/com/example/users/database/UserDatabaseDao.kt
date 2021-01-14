package com.example.users.database

import androidx.room.*

@Dao
interface UserDatabaseDao {

    @Insert()
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Query("SELECT *  from favourite_users_table where login= :login")
    fun get(login: String): User

    @Query("DELETE from favourite_users_table")
    fun clear()

    @Query("SELECT * from favourite_users_table ORDER BY userId DESC")
    fun getAllUsers(): List<User>?

    @Query("SELECT * from favourite_users_table ORDER BY userId DESC LIMIT 1")
    fun getLatestUser(): User?

}