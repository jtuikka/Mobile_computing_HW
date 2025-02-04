package com.example.homework1.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userTable")
data class User (
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "userName") val userName: String
)