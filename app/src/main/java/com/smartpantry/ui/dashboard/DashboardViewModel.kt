package com.smartpantry.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartpantry.SmartPantryApp
import com.smartpantry.utils.DateUtils
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as SmartPantryApp
    private val pantryRepository = app.pantryRepository
    private val calorieRepository = app.calorieRepository
    private val authManager = app.authManager

    private val userId: Long get() = authManager.currentUserId

    val totalItemCount: LiveData<Int> get() = pantryRepository.getTotalCount(userId)
    val expiringSoonCount: LiveData<Int> get() = pantryRepository.getExpiringSoonCount(
        DateUtils.getThresholdDate(3), userId
    )
    val lowStockCount: LiveData<Int> get() = pantryRepository.getLowStockCount(userId)
    val outOfStockCount: LiveData<Int> get() = pantryRepository.getOutOfStockCount(userId)
    val dailyCalorieTotal: LiveData<Int> get() = calorieRepository.getTodayTotalCalories(userId)

    val dailyCalorieGoal: Int get() = authManager.dailyCalorieGoal

    private val _categoryData = MutableLiveData<Map<String, Int>>()
    val categoryData: LiveData<Map<String, Int>> = _categoryData

    fun loadCategoryData() {
        viewModelScope.launch {
            val items = pantryRepository.getAllItemsList(userId)
            val grouped = items.groupBy { it.category }.mapValues { it.value.size }
            _categoryData.postValue(grouped)
        }
    }
}
