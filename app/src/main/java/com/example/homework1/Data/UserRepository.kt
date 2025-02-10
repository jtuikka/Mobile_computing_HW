package com.example.homework1.Data

import android.content.Context

class UserRepository(context: Context) {

    private val userDao: UserDao = UserDatabase.getDatabase(context).userDao()

    fun addUser(user: User) {
        userDao.addUser(user)
    }

    fun updateUsername(newUsername: String, uid: Int): Int {
        return userDao.updateName(newUsername, uid)
    }

    fun getUsername(uid: Int): String {
        return userDao.getUsername(uid)
    }

    fun getProfilePicture(uid: Int): String {
        return userDao.getProfilePicture(uid)
    }

    fun updatePicture(newPicture: String, uid: Int): Int {
        return userDao.updateProfilePicture(newPicture, uid)
    }
}