/*
 * Copyright (c) 2020. yono-ms
 */

import com.example.native40.network.GitHubAPICore
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class GitHubAPIUnitTest {
    class GitHubAPILocal : GitHubAPICore() {
        override fun log(text: String) {
            println(text)
        }
    }

    @Test
    fun getUsers() = runBlocking {
        val login = "kittinunf"
        kotlin.runCatching {
            val api = GitHubAPILocal()
            api.getUsers(login)
        }.onSuccess {
            assertEquals(it.login, login)
        }.onFailure {
            println(it.message)
            assert(false)
        }
        Unit
    }

    @Test
    fun getRepos() = runBlocking {
        val login = "kittinunf"
        kotlin.runCatching {
            val api = GitHubAPILocal()
            api.getRepos(login)
        }.onSuccess {
            assertNotEquals(it.jsonArray.size, 0)
        }.onFailure {
            println(it.message)
            assert(false)
        }
        Unit
    }
}
