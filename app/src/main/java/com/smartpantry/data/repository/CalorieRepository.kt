package com.smartpantry.data.repository

import androidx.lifecycle.LiveData
import com.smartpantry.data.dao.MealLogDao
import com.smartpantry.data.model.MealLog
import java.util.Calendar

class CalorieRepository(private val mealLogDao: MealLogDao) {

    fun getTodayLogs(userId: Long): LiveData<List<MealLog>> {
        val (start, end) = getTodayRange()
        return mealLogDao.getLogsForDate(start, end, userId)
    }

    fun getTodayTotalCalories(userId: Long): LiveData<Int> {
        val (start, end) = getTodayRange()
        return mealLogDao.getTotalCaloriesForDate(start, end, userId)
    }

    suspend fun getWeeklyCalories(userId: Long): List<Pair<String, Int>> {
        val calendar = Calendar.getInstance()
        val result = mutableListOf<Pair<String, Int>>()
        val dayNames = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

        for (i in 6 downTo 0) {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, -i)
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            val startOfDay = cal.timeInMillis
            cal.add(Calendar.DAY_OF_YEAR, 1)
            val endOfDay = cal.timeInMillis

            val total = mealLogDao.getTotalCaloriesForDateSync(startOfDay, endOfDay, userId)
            val dayName = dayNames[Calendar.getInstance().apply {
                timeInMillis = startOfDay
            }.get(Calendar.DAY_OF_WEEK) - 1]
            result.add(Pair(dayName, total))
        }
        return result
    }

    suspend fun logMeal(mealLog: MealLog): Long =
        mealLogDao.insert(mealLog)

    suspend fun deleteMealLog(mealLog: MealLog) =
        mealLogDao.delete(mealLog)

    private fun getTodayRange(): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val endOfDay = calendar.timeInMillis
        return Pair(startOfDay, endOfDay)
    }
}
