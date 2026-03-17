package com.smartpantry

import android.app.Application
import com.smartpantry.data.database.AppDatabase
import com.smartpantry.data.repository.CalorieRepository
import com.smartpantry.data.repository.PantryRepository
import com.smartpantry.data.repository.RecipeRepository
import com.smartpantry.auth.AuthManager
import com.smartpantry.utils.NotificationHelper

class SmartPantryApp : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }
    val pantryRepository by lazy { PantryRepository(database.pantryItemDao()) }
    val calorieRepository by lazy { CalorieRepository(database.mealLogDao()) }
    val recipeRepository by lazy { RecipeRepository(this) }
    val authManager by lazy { AuthManager(this) }

    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationChannels(this)
    }
}
