package dev.muffar.moneyfikasi.utils.extensions

import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.endOfBudgetPeriod
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.endOfDay
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.startOfBudgetPeriod
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.startOfDay
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.toMilliseconds
import dev.muffar.moneyfikasi.utils.extensions.LongExt.toLocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

class LocalDateTimeExtTest {

    private fun dt(year: Int, month: Int, day: Int, hour: Int = 12, minute: Int = 0): LocalDateTime {
        return LocalDateTime.of(year, month, day, hour, minute, 0, 0)
    }

    private fun assertDate(millis: Long, expectedYear: Int, expectedMonth: Int, expectedDay: Int, expectedHour: Int = 0, expectedMinute: Int = 0, expectedSecond: Int = 0) {
        // Compare via millis to avoid hour shift due to nano truncation, use same zone logic
        val expectedMillis = if (expectedHour == 23) {
            LocalDateTime.of(expectedYear, expectedMonth, expectedDay, 23, 59, 59, 999999999).toMilliseconds()
        } else {
            LocalDateTime.of(expectedYear, expectedMonth, expectedDay, expectedHour, expectedMinute, expectedSecond, 0).toMilliseconds()
        }
        assertEquals("millis mismatch for $expectedYear-$expectedMonth-$expectedDay $expectedHour:$expectedMinute:$expectedSecond", expectedMillis, millis)
    }

    // --- cutoff 1 : classic month ---
    @Test
    fun cutoff1_inOctober_periodIsFullMonth() {
        val now = dt(2026, 10, 15)
        val start = now.startOfBudgetPeriod(1)
        val end = now.endOfBudgetPeriod(1)
        assertDate(start, 2026, 10, 1, 0, 0, 0)
        assertDate(end, 2026, 10, 31, 23, 59, 59)
    }

    @Test
    fun cutoff1_firstDay_startsSameDay() {
        val now = dt(2026, 10, 1)
        val start = now.startOfBudgetPeriod(1)
        val end = now.endOfBudgetPeriod(1)
        assertDate(start, 2026, 10, 1)
        assertDate(end, 2026, 10, 31, 23, 59, 59)
    }

    // --- cutoff 25 : gajian case from feedback ---
    @Test
    fun cutoff25_earlyOctober_startsPreviousMonth25() {
        // Oct 3 <25 => start Sep 25, end Oct 24
        val now = dt(2026, 10, 3)
        val start = now.startOfBudgetPeriod(25)
        val end = now.endOfBudgetPeriod(25)
        assertDate(start, 2026, 9, 25, 0, 0, 0)
        assertDate(end, 2026, 10, 24, 23, 59, 59)
    }

    @Test
    fun cutoff25_onCutoffDay_startsSameDay() {
        val now = dt(2026, 10, 25)
        val start = now.startOfBudgetPeriod(25)
        val end = now.endOfBudgetPeriod(25)
        assertDate(start, 2026, 10, 25)
        assertDate(end, 2026, 11, 24, 23, 59, 59)
    }

    @Test
    fun cutoff25_afterCutoff_startsThisMonth25() {
        val now = dt(2026, 10, 26)
        val start = now.startOfBudgetPeriod(25)
        val end = now.endOfBudgetPeriod(25)
        assertDate(start, 2026, 10, 25)
        assertDate(end, 2026, 11, 24, 23, 59, 59)
    }

    @Test
    fun cutoff25_februaryBoundary() {
        // Mar 5 <25 => start Feb 25, end Mar 24
        val now = dt(2026, 3, 5)
        val start = now.startOfBudgetPeriod(25)
        val end = now.endOfBudgetPeriod(25)
        assertDate(start, 2026, 2, 25)
        assertDate(end, 2026, 3, 24, 23, 59, 59)
    }

    // --- cutoff 31 clamp tests ---
    @Test
    fun cutoff31_april30Days_clampsTo30() {
        // Apr 15 <31 => prev Mar has 31 => start Mar31
        val now = dt(2026, 4, 15)
        val start = now.startOfBudgetPeriod(31)
        val end = now.endOfBudgetPeriod(31)
        assertDate(start, 2026, 3, 31)
        // Mar31 +1 month = Apr30 -1 day = Apr29
        assertDate(end, 2026, 4, 29, 23, 59, 59)
    }

    @Test
    fun cutoff31_may15_clampsPrevTo30() {
        // May 15 <31 => prev Apr 30 => start Apr30
        val now = dt(2026, 5, 15)
        val start = now.startOfBudgetPeriod(31)
        assertDate(start, 2026, 4, 30)
        // Apr30 +1 month = May30 -1 = May29
        val end = now.endOfBudgetPeriod(31)
        assertDate(end, 2026, 5, 29, 23, 59, 59)
    }

    @Test
    fun cutoff31_februaryNonLeap_clamps() {
        // 2023 Feb non-leap 28 days
        val now = dt(2023, 2, 15)
        val start = now.startOfBudgetPeriod(31)
        val end = now.endOfBudgetPeriod(31)
        assertDate(start, 2023, 1, 31)
        // Jan31 +1 month = Feb28 -1 = Feb27
        assertDate(end, 2023, 2, 27, 23, 59, 59)
        // next period would be Feb28 - Mar27 (Feb28+1 month=Mar28-1=Mar27)
        val next = dt(2023, 2, 28)
        assertDate(next.startOfBudgetPeriod(31), 2023, 2, 28)
        assertDate(next.endOfBudgetPeriod(31), 2023, 3, 27, 23, 59, 59)
    }

    @Test
    fun cutoff31_februaryLeap_clampsTo29() {
        // 2024 leap Feb 29 days
        val now = dt(2024, 2, 15)
        val start = now.startOfBudgetPeriod(31)
        val end = now.endOfBudgetPeriod(31)
        assertDate(start, 2024, 1, 31)
        // Jan31 +1 month = Feb29 -1 = Feb28 (leap)
        assertDate(end, 2024, 2, 28, 23, 59, 59)
    }

    @Test
    fun cutoff28_february() {
        val now = dt(2026, 2, 15)
        val start = now.startOfBudgetPeriod(28)
        val end = now.endOfBudgetPeriod(28)
        // 15 <28 => prev Jan28
        assertDate(start, 2026, 1, 28)
        assertDate(end, 2026, 2, 27, 23, 59, 59)
    }

    // --- startOfDay / endOfDay ---
    @Test
    fun startOfDay_and_endOfDay_coverFullDay() {
        val now = dt(2026, 10, 1, 15, 30)
        val startMillis = now.startOfDay()
        val endMillis = now.endOfDay()
        val start = startMillis.toLocalDateTime()
        val end = endMillis.toLocalDateTime()
        assertEquals(0, start.hour)
        assertEquals(0, start.minute)
        assertEquals(23, end.hour)
        assertEquals(59, end.minute)
        assertEquals(59, end.second)
        // toMilliseconds truncates nanos to millis, so 999999999 -> 999000000
        assertEquals(999000000, end.nano)
        // start < end and same date
        assertTrue(startMillis < endMillis)
        assertEquals(start.dayOfMonth, end.dayOfMonth)
    }

    // --- CustomDateSheet fix simulation ---
    @Test
    fun customDateSheet_normalizesToStartAndEndOfDay() {
        // Simulate picker returning UTC midnight 29 Sep and 1 Oct
        // In real device, picker returns millis at UTC 00:00; after toLocalDateTime + startOfDay/endOfDay should be 00:00 and 23:59 local
        // We simulate by creating LocalDateTime at 00:00 UTC equivalent, but using system zone
        // Use arbitrary millis: take 29 Sep 07:00 local (which is 29 Sep 00:00 UTC+7) -> after normalization should be 29 Sep 00:00
        val pickerStart = dt(2026, 9, 29, 7, 0).toMilliseconds() // pretend UTC midnight shifted
        val pickerEnd = dt(2026, 10, 1, 7, 0).toMilliseconds()

        val normalizedStart = pickerStart.toLocalDateTime().startOfDay()
        val normalizedEnd = pickerEnd.toLocalDateTime().endOfDay()

        val startLdt = normalizedStart.toLocalDateTime()
        val endLdt = normalizedEnd.toLocalDateTime()

        assertEquals(29, startLdt.dayOfMonth)
        assertEquals(9, startLdt.monthValue)
        assertEquals(0, startLdt.hour)

        assertEquals(1, endLdt.dayOfMonth)
        assertEquals(10, endLdt.monthValue)
        assertEquals(23, endLdt.hour)
        assertEquals(59, endLdt.minute)

        // Ensure 1 Oct 10:00 transaction would be inside range
        val transactionAtOct1_10am = dt(2026, 10, 1, 10, 0).toMilliseconds()
        assertTrue(transactionAtOct1_10am in normalizedStart..normalizedEnd)
        // 1 Oct 23:59 still inside
        val transactionAtOct1_2359 = dt(2026, 10, 1, 23, 59).toMilliseconds()
        assertTrue(transactionAtOct1_2359 in normalizedStart..normalizedEnd)
        // 2 Oct 00:00 outside
        val nextDay = dt(2026, 10, 2, 0, 0).toMilliseconds()
        assertTrue(nextDay > normalizedEnd)
    }

    @Test
    fun customDateSheet_oldBugWouldExcludeLastDay() {
        // Old code used raw millis: end = 1 Oct 00:00
        val rawStart = dt(2026, 9, 29, 0, 0).toMilliseconds()
        val rawEnd = dt(2026, 10, 1, 0, 0).toMilliseconds()
        val transactionAtOct1_10am = dt(2026, 10, 1, 10, 0).toMilliseconds()
        // raw BETWEEN would exclude 10am because 10am > 00:00
        assertTrue(transactionAtOct1_10am > rawEnd)
        assertTrue(transactionAtOct1_10am !in rawStart..rawEnd)
    }

    // --- Contiguity check ---
    @Test
    fun budgetPeriods_areContiguous_forCutoff25() {
        val cutoff = 25
        val first = dt(2026, 9, 25)
        val second = dt(2026, 10, 25)
        val firstEnd = first.endOfBudgetPeriod(cutoff).toLocalDateTime()
        val secondStart = second.startOfBudgetPeriod(cutoff).toLocalDateTime()
        // first end = Oct24 23:59, second start = Oct25 00:00 -> next day
        assertEquals(24, firstEnd.dayOfMonth)
        assertEquals(10, firstEnd.monthValue)
        assertEquals(25, secondStart.dayOfMonth)
        assertEquals(10, secondStart.monthValue)
        // ensure no gap/overlap: secondStart is firstEnd +1 day 00:00
        val gapMillis = secondStart.toMilliseconds() - firstEnd.toMilliseconds()
        // should be 1 millisecond? Actually 23:59:59.999 to 00:00 next day = 1ms
        assertTrue(gapMillis in 1..1000)
    }
}
