package com.example.homework1.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userTable")
data class User {

    @PrimaryKey(autoGenerate = true)
    val name: String,
    val picture: String
}