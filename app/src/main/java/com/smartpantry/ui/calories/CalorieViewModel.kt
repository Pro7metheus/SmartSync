package com.smartpantry.ui.calories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartpantry.SmartPantryApp
import com.smartpantry.data.model.MealLog
import com.smartpantry.data.model.PantryItem
import com.smartpantry.data.model.usda.UsdaFood
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CalorieViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as SmartPantryApp
    private val calorieRepository = app.calorieRepository
    private val pantryRepository = app.pantryRepository
    private val authManager = app.authManager
    private val userId: Long get() = authManager.currentUserId

    val todayLogs: LiveData<List<MealLog>> get() = calorieRepository.getTodayLogs(userId)
    val todayTotal: LiveData<Int> get() = calorieRepository.getTodayTotalCalories(userId)
    val dailyGoal: Int get() = authManager.dailyCalorieGoal

    private val _pantryItems = MutableLiveData<List<PantryItem>>()
    val pantryItems: LiveData<List<PantryItem>> = _pantryItems

    private val _weeklyData = MutableLiveData<List<Pair<String, Int>>>()
    val weeklyData: LiveData<List<Pair<String, Int>>> = _weeklyData

    // For USDA API Autocomplete
    private val _usdaSearchResults = MutableStateFlow<List<UsdaFood>>(emptyList())
    val usdaSearchResults: StateFlow<List<UsdaFood>> = _usdaSearchResults.asStateFlow()

    private var searchJob: Job? = null

    fun loadPantryItems() {
        viewModelScope.launch {
            val items = pantryRepository.getAllItemsList(userId)
            _pantryItems.postValue(items)
        }
    }

    fun logMeal(pantryItem: PantryItem, quantityConsumed: Double) {
        val caloriesConsumed = (pantryItem.caloriesPerUnit * quantityConsumed).toInt()
        val mealLog = MealLog(
            pantryItemId = pantryItem.id,
            itemName = pantryItem.name,
            caloriesConsumed = caloriesConsumed,
            quantityConsumed = quantityConsumed,
            unit = pantryItem.unit,
            userId = userId
        )
        viewModelScope.launch {
            calorieRepository.logMeal(mealLog)
        }
    }

    fun logCustomMeal(name: String, caloriesConsumed: Int, quantityConsumed: Double, unit: String) {
        val mealLog = MealLog(
            pantryItemId = -1L,
            itemName = name,
            caloriesConsumed = caloriesConsumed,
            quantityConsumed = quantityConsumed,
            unit = unit,
            userId = userId
        )
        viewModelScope.launch {
            calorieRepository.logMeal(mealLog)
        }
    }

    fun deleteMealLog(mealLog: MealLog) {
        viewModelScope.launch {
            calorieRepository.deleteMealLog(mealLog)
        }
    }

    fun loadWeeklyData() {
        viewModelScope.launch {
            val data = calorieRepository.getWeeklyCalories(userId)
            _weeklyData.postValue(data)
        }
    }

    fun setDailyGoal(goal: Int) {
        authManager.dailyCalorieGoal = goal
    }

    // --- USDA API Search ---

    fun searchUsdaFoods(query: String) {
        searchJob?.cancel()
        if (query.length < 3) {
            _usdaSearchResults.value = emptyList()
            return
        }
        searchJob = viewModelScope.launch {
            delay(500)
            val results = pantryRepository.searchFoodsOnline(query)
            _usdaSearchResults.value = results
        }
    }

    fun clearUsdaSearch() {
        searchJob?.cancel()
        _usdaSearchResults.value = emptyList()
    }
}
