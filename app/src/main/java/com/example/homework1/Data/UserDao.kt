package com.example.homework1.Data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM userTable ORDER BY userName ASC")
    fun readAllData(): LiveData<List<User>>

    @Query("SELECT * FROM userTable WHERE userName LIKE :username LIMIT 1")
    fun findByName(username: String): User

    @Query("UPDATE userTable SET userName = :newUsername WHERE userName = :oldUsername")
    fun updateName(oldUsername: String, newUsername: String): Int
}