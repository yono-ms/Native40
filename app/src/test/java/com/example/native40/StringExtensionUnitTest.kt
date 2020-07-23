/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import com.example.native40.extension.toDisplayDateFromISO
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.util.*

class StringExtensionUnitTest {
    @Test
    fun isoToDisplay() {
        val text = "2018-06-24T02:00:58Z"
        val result = text.toDisplayDateFromISO()
        when (Locale.getDefault()) {
            Locale.JAPAN -> assertEquals("2018/06/24 11:00", result)
            else -> assertNotEquals("", result)
        }
    }
}