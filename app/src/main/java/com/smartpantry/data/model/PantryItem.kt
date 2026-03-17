package com.smartpantry.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pantry_items")
data class PantryItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: String,
    val quantity: Double,
    val unit: String,
    val minQuantity: Double = 1.0,
    val purchaseDate: Long,
    val expiryDate: Long,
    val caloriesPerUnit: Int = 0,
    val userId: Long = 0
)
