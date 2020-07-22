/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40.db

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    suspend fun getAll(): List<User>

    @Query("SELECT * FROM user ORDER BY time_stamp DESC")
    suspend fun getAllHistory(): List<User>

    @Query("SELECT * FROM user WHERE login = :login LIMIT 1")
    suspend fun findByLogin(login: String): User?

    @Query("DELETE FROM user")
    suspend fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg user: User)

    @Update
    suspend fun updateAll(vararg user: User)

    @Delete
    suspend fun delete(user: User)

    @Transaction
    suspend fun deleteAll(users: List<User>) {
        users.forEach {
            delete(it)
        }
    }
}