package com.example.homework1.Data

import androidx.lifecycle.LiveData

class UserRepository(private val userDao) {

    val readAllData: LiveData<List<User>> = userDao.readAllData()

    suspend fun addUser(user: User) {
        userDao.addUser(user)
    }
}