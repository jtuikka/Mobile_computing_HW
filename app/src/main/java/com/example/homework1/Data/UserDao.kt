package com.example.homework1.Data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUser(user: User)

    @Query("SELECT * FROM userTable ORDER BY userName ASC")
    fun readAllData(): LiveData<List<User>>

    @Query("SELECT * FROM userTable WHERE userName LIKE :username LIMIT 1")
    fun findByName(username: String): User

    @Query("UPDATE userTable SET userName = :newUsername WHERE uid = :uid")
    fun updateName(newUsername: String, uid: Int): Int

    @Query("SELECT * FROM userTable")
    fun getAll(): List<User>

    @Query("SELECT userName FROM userTable WHERE uid = :uid")
    fun getUsername(uid: Int): String

    @Query("SELECT profilePicture FROM userTable WHERE uid = :uid")
    fun getProfilePicture(uid: Int): String

    @Query("UPDATE userTable SET profilePicture = :newProfilePicture WHERE uid = :uid")
    fun updateProfilePicture(newProfilePicture: String, uid: Int): Int
}