package com.smartpantry.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_logs")
data class MealLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val pantryItemId: Long,
    val itemName: String,
    val caloriesConsumed: Int,
    val quantityConsumed: Double,
    val unit: String,
    val timestamp: Long = System.currentTimeMillis(),
    val userId: Long = 0
)
