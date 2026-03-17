package com.smartpantry.utils

import com.smartpantry.data.model.UnitType
import java.util.regex.Pattern

data class NlpResult(
    val quantity: Double?,
    val unit: String?,
    val foodName: String
)

object NlpParser {

    fun parseInput(input: String): NlpResult {
        var str = input.trim()
        if (str.isEmpty()) return NlpResult(null, null, "")

        // Check if starts with a number (e.g., "100", "2.5", ".5")
        val numberRegex = "^(\\d*\\.?\\d+)(.*)$".toRegex()
        val matchResult = numberRegex.find(str)

        var quantity: Double? = null
        var unit: String? = null

        if (matchResult != null) {
            val numStr = matchResult.groupValues[1]
            quantity = numStr.toDoubleOrNull()
            
            // The rest of the string
            str = matchResult.groupValues[2].trim()

            // See if the next token is a unit
            val parts = str.split("\\s+".toRegex())
            if (parts.isNotEmpty()) {
                val potentialUnit = parts[0].lowercase()
                val mappedUnit = getMappedUnitOrNull(potentialUnit)
                if (mappedUnit != null) {
                    unit = mappedUnit
                    // Remove the unit from the string
                    str = str.substring(potentialUnit.length).trim()
                } else if (isAttachedUnit(numStr, input)) {
                     // In case like "100g", regex matcher might not split 100 and g properly if we don't handle it
                     // Actually, the above numberRegex "^(\\d*\\.?\\d+)(.*)$" WILL correctly split "100g" into "100" and "g".
                     // potentialUnit would be "g".
                }
            }
        }

        // Remove leading "of " if present
        if (str.lowercase().startsWith("of ")) {
            str = str.substring(3).trim()
        }

        val foodName = str.trim()

        return NlpResult(quantity, unit, foodName)
    }

    // Returns the unit if recognized, else null
    private fun getMappedUnitOrNull(rawUnit: String): String? {
        return when (rawUnit.lowercase()) {
            "g", "gr", "gram", "grams" -> "g"
            "kg", "kilo", "kilogram", "kilograms" -> "kg"
            "ml", "milliliter", "milliliters" -> "ml"
            "l", "liter", "liters" -> "L"
            "oz", "ounce", "ounces" -> "oz" // not in default list, but still mapped
            "lb", "lbs", "pound", "pounds" -> "lbs" // not in default list, but still mapped
            "cup", "cups" -> "cups"
            "piece", "pieces", "pcs", "pc" -> "pieces"
            "tbsp", "tablespoon", "tablespoons" -> "pieces" // fallback pieces
            "tsp", "teaspoon", "teaspoons" -> "pieces" // fallback pieces
            else -> null
        }
    }

    private fun isAttachedUnit(numStr: String, original: String): Boolean {
        return false // Handled by regex separating numbers and letters
    }
}
