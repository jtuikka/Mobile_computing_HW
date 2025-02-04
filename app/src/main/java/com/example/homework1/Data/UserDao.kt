package com.example.homework1.Data

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM userTable ORDER BY name ASC")
    fun readAllData(): LiveData<List<User>>

}