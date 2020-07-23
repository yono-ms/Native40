/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(indices = [Index(value = ["login"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val login: String,
    @ColumnInfo(name = "time_stamp") val timeStamp: Date
)
