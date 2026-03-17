package com.smartpantry.data.model.usda

import com.google.gson.annotations.SerializedName

data class UsdaSearchResponse(
    @SerializedName("totalHits") val totalHits: Int,
    @SerializedName("currentPage") val currentPage: Int,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("foods") val foods: List<UsdaFood>
)

data class UsdaFood(
    @SerializedName("fdcId") val fdcId: Int,
    @SerializedName("description") val description: String,
    @SerializedName("brandOwner") val brandOwner: String?,
    @SerializedName("foodNutrients") val foodNutrients: List<UsdaNutrient>? = emptyList(),
    @SerializedName("servingSize") val servingSize: Double?,
    @SerializedName("servingSizeUnit") val servingSizeUnit: String?
) {
    // Helper property to extract energy (calories) specifically, as it is the most common use case.
    val energyKcal: Double
        get() = foodNutrients?.find { it.nutrientName == "Energy" && it.unitName == "KCAL" }?.value ?: 0.0
}

data class UsdaNutrient(
    @SerializedName("nutrientId") val nutrientId: Int,
    @SerializedName("nutrientName") val nutrientName: String,
    @SerializedName("nutrientNumber") val nutrientNumber: String,
    @SerializedName("unitName") val unitName: String,
    @SerializedName("value") val value: Double
)
