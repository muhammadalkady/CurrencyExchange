package com.kady.muhammad.core.presentation

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Converts a timestamp in milliseconds to a formatted date-time string.
 *
 * @param timestampMillis The timestamp in milliseconds.
 * @param pattern The desired format for the date-time string (default: "yyyy-MM-dd HH:mm:ss").
 * @param zoneId The time zone to use for formatting (default: system's default time zone).
 * @return A formatted date-time string.
 */
fun formatTimestampMillis(
    timestampMillis: Long,
    pattern: String = "yyyy-MM-dd HH:mm:ss",
    zoneId: ZoneId = ZoneId.systemDefault()
): String {
    val formatter = DateTimeFormatter.ofPattern(pattern).withZone(zoneId)
    val instant = Instant.ofEpochMilli(timestampMillis)
    return formatter.format(instant)
}

/**
 * Groups a list of objects by the day based on a timestamp property in milliseconds.
 *
 * @param items A list of objects to be grouped by day.
 * @param timestampExtractor A lambda that extracts the timestamp in milliseconds from each object.
 * @param zoneId The time zone to use for computing days (default: system's default time zone).
 * @return A map where the key is the day (formatted as "yyyy-MM-dd"),
 *         and the value is a list of objects belonging to that day.
 */
fun <T> groupByDay(
    items: List<T>,
    timestampExtractor: (T) -> Long,
    zoneId: ZoneId = ZoneId.systemDefault()
): Map<String, List<T>> {
    // Formatter to extract the day in "yyyy-MM-dd" format
    val dayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(zoneId)
    return items.groupBy { item ->
        val timestamp = timestampExtractor(item)
        val instant = Instant.ofEpochMilli(timestamp)
        dayFormatter.format(instant)
    }
}