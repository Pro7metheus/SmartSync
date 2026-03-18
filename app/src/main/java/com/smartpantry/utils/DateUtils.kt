package com.smartpantry.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object DateUtils {
    private val displayFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    fun formatDate(timestamp: Long): String {
        return displayFormat.format(Date(timestamp))
    }

    fun getDaysUntilExpiry(expiryDate: Long): Long {
        val now = System.currentTimeMillis()
        val diff = expiryDate - now
        return TimeUnit.MILLISECONDS.toDays(diff)
    }

    fun isExpired(expiryDate: Long): Boolean {
        return expiryDate > 0 && expiryDate <= System.currentTimeMillis()
    }

    fun isExpiringSoon(expiryDate: Long, daysThreshold: Int = 3): Boolean {
        if (expiryDate <= 0) return false
        val days = getDaysUntilExpiry(expiryDate)
        return days in 0..daysThreshold
    }

    fun getThresholdDate(daysFromNow: Int = 3): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, daysFromNow)
        return calendar.timeInMillis
    }

    fun getStartOfDay(daysOffset: Int = 0): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, daysOffset)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}
