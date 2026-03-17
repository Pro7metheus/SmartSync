package com.smartpantry.ui.pantry

import android.app.Application
import androidx.lifecycle.*
import com.smartpantry.SmartPantryApp
import com.smartpantry.data.model.PantryItem
import com.smartpantry.data.model.usda.UsdaFood
import com.smartpantry.utils.DateUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PantryViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as SmartPantryApp
    private val repository = app.pantryRepository
    private val userId: Long get() = app.authManager.currentUserId

    private val _searchQuery = MutableLiveData("")
    private val _selectedCategory = MutableLiveData("All")

    // For USDA API Autocomplete
    private val _usdaSearchResults = MutableStateFlow<List<UsdaFood>>(emptyList())
    val usdaSearchResults: StateFlow<List<UsdaFood>> = _usdaSearchResults.asStateFlow()

    private var searchJob: Job? = null

    val allItems: LiveData<List<PantryItem>> get() = repository.getAllItems(userId)

    val filteredItems: LiveData<List<PantryItem>> = MediatorLiveData<List<PantryItem>>().apply {
        addSource(allItems) { filterItems() }
        addSource(_searchQuery) { filterItems() }
        addSource(_selectedCategory) { filterItems() }
    }

    private fun MediatorLiveData<List<PantryItem>>.filterItems() {
        val items = allItems.value ?: return
        val query = _searchQuery.value?.lowercase() ?: ""
        val category = _selectedCategory.value ?: "All"

        val filtered = items.filter { item ->
            val matchesQuery = query.isEmpty() || item.name.lowercase().contains(query)
            val matchesCategory = category == "All" || item.category == category
            matchesQuery && matchesCategory
        }
        value = filtered
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun insertItem(item: PantryItem) {
        viewModelScope.launch { repository.insert(item) }
    }

    fun updateItem(item: PantryItem) {
        viewModelScope.launch { repository.update(item) }
    }

    fun deleteItem(item: PantryItem) {
        viewModelScope.launch { repository.delete(item) }
    }

    suspend fun getItemById(id: Long): PantryItem? = repository.getItemById(id)

    // --- USDA API Search ---

    fun searchUsdaFoods(query: String) {
        // Cancel any pending search to implement debounce
        searchJob?.cancel()

        if (query.length < 3) {
            _usdaSearchResults.value = emptyList()
            return
        }

        searchJob = viewModelScope.launch {
            delay(500) // 500ms debounce
            val results = repository.searchFoodsOnline(query)
            _usdaSearchResults.value = results
        }
    }

    fun clearUsdaSearch() {
        _usdaSearchResults.value = emptyList()
    }
}
