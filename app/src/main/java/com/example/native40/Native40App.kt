/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import android.app.Application
import androidx.room.Room
import com.example.native40.db.AppDatabase
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Native40App : Application() {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(Native40App::class.java.simpleName)
        lateinit var db: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        logger.info("onCreate")
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "native40_database"
        ).build()
    }
}