package com.example.users.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDatabaseDao {

    @Insert()
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Query("SELECT *  from users_detail_table where userId= :key")
    fun get(key: Long): User

    @Query("DELETE from users_detail_table")
    fun clear()

    @Query("SELECT * from users_detail_table ORDER BY userId DESC")
    fun getAllUsers(): LiveData<List<User>> //Live data is updated whenever the database is updated

    @Query("SELECT * from users_detail_table ORDER BY userId DESC LIMIT 1")
    fun getLatestUser(): User?

}