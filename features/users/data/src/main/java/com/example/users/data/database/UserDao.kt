package com.example.users.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM user WHERE id=:userId LIMIT 1")
    fun getUser(userId: Int): Flow<UserEntity>

    @Transaction
    suspend fun updateUsers(users: List<UserEntity>) {
        clearUsers()
        saveUsers(users)
    }

    @Transaction
    suspend fun updateUser(user: UserEntity) {
        deleteUserById(user.id)
        saveUser(user)
    }

    @Query("DELETE FROM user WHERE id=:userId")
    suspend fun deleteUserById(userId: Int)

    @Query("DELETE FROM user")
    suspend fun clearUsers()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUsers(users: List<UserEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: UserEntity)
}
