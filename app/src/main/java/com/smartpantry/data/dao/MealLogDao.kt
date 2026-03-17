package com.smartpantry.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.smartpantry.data.model.MealLog

@Dao
interface MealLogDao {
    @Insert
    suspend fun insert(mealLog: MealLog): Long

    @Delete
    suspend fun delete(mealLog: MealLog)

    @Query("SELECT * FROM meal_logs WHERE timestamp >= :startOfDay AND timestamp < :endOfDay AND userId = :userId ORDER BY timestamp DESC")
    fun getLogsForDate(startOfDay: Long, endOfDay: Long, userId: Long): LiveData<List<MealLog>>

    @Query("SELECT COALESCE(SUM(caloriesConsumed), 0) FROM meal_logs WHERE timestamp >= :startOfDay AND timestamp < :endOfDay AND userId = :userId")
    fun getTotalCaloriesForDate(startOfDay: Long, endOfDay: Long, userId: Long): LiveData<Int>

    @Query("SELECT COALESCE(SUM(caloriesConsumed), 0) FROM meal_logs WHERE timestamp >= :startOfDay AND timestamp < :endOfDay AND userId = :userId")
    suspend fun getTotalCaloriesForDateSync(startOfDay: Long, endOfDay: Long, userId: Long): Int

    @Query("SELECT * FROM meal_logs WHERE timestamp >= :startDate AND timestamp < :endDate AND userId = :userId ORDER BY timestamp ASC")
    suspend fun getLogsForDateRange(startDate: Long, endDate: Long, userId: Long): List<MealLog>
}
