package com.smartpantry.ui.recipes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartpantry.SmartPantryApp
import com.smartpantry.data.model.Recipe
import kotlinx.coroutines.launch

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as SmartPantryApp
    private val pantryRepository = app.pantryRepository
    private val recipeRepository = app.recipeRepository
    private val userId: Long get() = app.authManager.currentUserId

    private val _matchedRecipes = MutableLiveData<List<Pair<Recipe, Int>>>()
    val matchedRecipes: LiveData<List<Pair<Recipe, Int>>> = _matchedRecipes

    fun loadMatchingRecipes() {
        viewModelScope.launch {
            val pantryItems = pantryRepository.getAllItemsList(userId)
            val pantryNames = pantryItems.map { it.name }
            val matches = recipeRepository.getMatchingRecipes(pantryNames)
            _matchedRecipes.postValue(matches)
        }
    }

    fun getRecipeById(recipeId: Int): Recipe? {
        return recipeRepository.getAllRecipes().find { it.id == recipeId }
    }

    fun getPantryItemNames(): List<String> {
        return _matchedRecipes.value?.let { emptyList() } ?: emptyList()
    }

    suspend fun getCurrentPantryNames(): List<String> {
        return pantryRepository.getAllItemsList(userId).map { it.name.lowercase().trim() }
    }
}
