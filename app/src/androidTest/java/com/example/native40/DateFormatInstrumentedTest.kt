/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import android.text.format.DateFormat
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class DateFormatInstrumentedTest {

    lateinit var today: Date

    @Before
    fun before() {
        today = Calendar.getInstance(Locale.JAPAN).apply {
            set(Calendar.YEAR, 2020)
            set(Calendar.MONTH, 8)
            set(Calendar.DAY_OF_MONTH, 6)
            set(Calendar.HOUR, 0)
            set(Calendar.MINUTE, 26)
            set(Calendar.SECOND, 8)
            timeZone = TimeZone.getTimeZone("UTC")
        }.time
    }

    @Test
    fun bestPattern() {
        val cases = listOf(
            TestArgs(Locale.JAPAN, "EEEMMMd", "9月6日(日)"),
            TestArgs(Locale.ENGLISH, "EEEMMMd", "Sun, Sep 6"),
            TestArgs(Locale.JAPAN, "EEEMMMdyyyy", "2020年9月6日(日)"),
            TestArgs(Locale.ENGLISH, "EEEMMMdyyyy", "Sun, Sep 6, 2020"),
            TestArgs(Locale.JAPAN, "EEEMMMdyyyy HH:mm", "2020年9月6日(日) 9:26"),
            TestArgs(Locale.US, "EEEMMMdyyyy HH:mm", "Sun, Sep 6, 2020, 00:26"),
        )
        cases.forEach {
            val result = getFormat(it.locale, it.skeleton, today)
            Assert.assertEquals(it.expected, result)
        }
    }

    private fun getFormat(locale: Locale, skeleton: String, date: Date): String {
        val format = DateFormat.getBestDateTimePattern(locale, skeleton)
        val dateFormat = SimpleDateFormat(format, locale).apply {
            timeZone = when (locale) {
                Locale.JAPAN -> TimeZone.getTimeZone("Asia/Tokyo")
                Locale.ENGLISH -> TimeZone.getTimeZone("Europe/London")
                else -> TimeZone.getTimeZone("UTC")
            }
        }
        return dateFormat.format(date)
    }

    data class TestArgs(val locale: Locale, val skeleton: String, val expected: String)
}