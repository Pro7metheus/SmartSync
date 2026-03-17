package com.smartpantry.data.model

data class Recipe(
    val id: Int,
    val name: String,
    val category: String,
    val ingredients: List<String>,
    val instructions: String,
    val estimatedCalories: Int = 0
)
