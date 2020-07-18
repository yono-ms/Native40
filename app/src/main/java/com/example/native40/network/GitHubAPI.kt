/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40.network

import org.slf4j.LoggerFactory

class GitHubAPI : GitHubAPICore() {
    companion object {
        // UnitTestでロガーを使用できないのでここで持つ
        private val logger = LoggerFactory.getLogger(GitHubAPI::class.java.simpleName)
    }

    override fun log(text: String) {
        logger.info(text)
    }
}
