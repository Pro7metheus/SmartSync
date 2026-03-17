package com.smartpantry.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.smartpantry.data.model.Recipe

class RecipeRepository(private val context: Context) {

    private var cachedRecipes: List<Recipe>? = null

    fun getAllRecipes(): List<Recipe> {
        if (cachedRecipes == null) {
            cachedRecipes = loadRecipesFromAssets()
        }
        return cachedRecipes ?: emptyList()
    }

    fun getMatchingRecipes(pantryItemNames: List<String>): List<Pair<Recipe, Int>> {
        val normalizedPantry = pantryItemNames.map { it.lowercase().trim() }
        val recipes = getAllRecipes()

        return recipes.map { recipe ->
            val matchCount = recipe.ingredients.count { ingredient ->
                normalizedPantry.any { pantryItem ->
                    pantryItem.contains(ingredient.lowercase().trim()) ||
                    ingredient.lowercase().trim().contains(pantryItem)
                }
            }
            val matchPercent = if (recipe.ingredients.isNotEmpty()) {
                (matchCount * 100) / recipe.ingredients.size
            } else 0
            Pair(recipe, matchPercent)
        }.filter { it.second > 0 }
         .sortedByDescending { it.second }
    }

    private fun loadRecipesFromAssets(): List<Recipe> {
        return try {
            val json = context.assets.open("recipes.json")
                .bufferedReader()
                .use { it.readText() }
            val type = object : TypeToken<List<Recipe>>() {}.type
            Gson().fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
