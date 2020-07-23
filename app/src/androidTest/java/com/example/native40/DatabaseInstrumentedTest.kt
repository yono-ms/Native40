/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.native40.db.AppDatabase
import com.example.native40.db.User
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class DatabaseInstrumentedTest {

    private lateinit var db: AppDatabase

    @Before
    fun before() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "test_database"
        ).build()
    }

    @Test
    fun insertOne() = runBlocking {
        val loginName = "test1"
        assertNotNull(db)
        db.userDao().clear()
        db.userDao().getAll().also {
            assertEquals(it.size, 0)
        }
        db.userDao().insertAll(User(0, loginName, Date()))
        db.userDao().getAll().also {
            assertEquals(it.size, 1)
        }
        val user = db.userDao().findByLogin(loginName)
        assertNotNull(user)
        user?.let {
            assertEquals(it.id, 0)
            assertEquals(it.login, loginName)
            db.userDao().delete(it)
        }
        db.userDao().getAll().also {
            assertEquals(it.size, 0)
        }
        Unit
    }

    @Test
    fun history() = runBlocking {
        val loginName1 = "test1"
        val loginName2 = "test2"
        assertNotNull(db)
        db.userDao().clear()
        db.userDao().getAll().also {
            assertEquals(0, it.size)
        }
        db.userDao().insertAll(User(0, loginName1, Date()))
        db.userDao().getAll().also {
            assertEquals(1, it.size)
        }
        db.userDao().insertAll(User(0, loginName2, Date()))
        db.userDao().getAll().also {
            assertEquals(2, it.size)
        }
        db.userDao().getAllHistory().also {
            assertEquals(2, it.size)
            assertEquals(loginName2, it[0].login)
            assertEquals(loginName1, it[1].login)
            db.userDao().deleteAll(it)
        }
        db.userDao().getAll().also {
            assertEquals(0, it.size)
        }
        Unit
    }
}