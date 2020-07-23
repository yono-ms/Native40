/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40.db

import androidx.room.TypeConverter
import java.util.*

class AppConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}