/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["login"])])
data class User(
    @PrimaryKey val id: Int,
    val login: String
)
