package com.smartpantry.data.repository

import androidx.lifecycle.LiveData
import com.smartpantry.data.dao.PantryItemDao
import com.smartpantry.BuildConfig
import com.smartpantry.data.api.RetrofitClient
import com.smartpantry.data.model.PantryItem
import com.smartpantry.data.model.usda.UsdaFood
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository class that acts as the single source of truth for Pantry data.
 * 
 * In the MVVM architecture, the Repository abstracts the data sources from the rest of the app.
 * It decides whether to fetch data from the local database (Room) or from a remote network (API).
 * 
 * - For local data: It delegates fast, offline queries to `PantryItemDao`.
 * - For remote data: It uses `RetrofitClient` to call the USDA API for nutritional search.
 * 
 * Flow of data: UI -> ViewModel -> Repository -> (Room Database OR Retrofit API)
 */
class PantryRepository(private val pantryItemDao: PantryItemDao) {

    fun getAllItems(userId: Long): LiveData<List<PantryItem>> =
        pantryItemDao.getAllItems(userId)

    suspend fun getAllItemsList(userId: Long): List<PantryItem> =
        pantryItemDao.getAllItemsList(userId)

    suspend fun getItemById(id: Long): PantryItem? =
        pantryItemDao.getItemById(id)

    fun getByCategory(category: String, userId: Long): LiveData<List<PantryItem>> =
        pantryItemDao.getByCategory(category, userId)

    fun searchByName(query: String, userId: Long): LiveData<List<PantryItem>> =
        pantryItemDao.searchByName(query, userId)

    fun getExpiringSoon(thresholdDate: Long, userId: Long): LiveData<List<PantryItem>> =
        pantryItemDao.getExpiringSoon(thresholdDate, userId)

    fun getLowStock(userId: Long): LiveData<List<PantryItem>> =
        pantryItemDao.getLowStock(userId)

    fun getTotalCount(userId: Long): LiveData<Int> =
        pantryItemDao.getTotalCount(userId)

    fun getExpiringSoonCount(thresholdDate: Long, userId: Long): LiveData<Int> =
        pantryItemDao.getExpiringSoonCount(thresholdDate, userId)

    fun getOutOfStockCount(userId: Long): LiveData<Int> =
        pantryItemDao.getOutOfStockCount(userId)

    fun getLowStockCount(userId: Long): LiveData<Int> =
        pantryItemDao.getLowStockCount(userId)

    suspend fun insert(item: PantryItem): Long =
        pantryItemDao.insert(item)

    suspend fun update(item: PantryItem) =
        pantryItemDao.update(item)

    suspend fun delete(item: PantryItem) =
        pantryItemDao.delete(item)

    // --- Remote API calls ---

    suspend fun searchFoodsOnline(query: String): List<UsdaFood> {
        return withContext(Dispatchers.IO) {
            try {
                // Ensure query is not blank
                if (query.isBlank()) return@withContext emptyList()
                
                // Fetch the API Key from BuildConfig
                val apiKey = BuildConfig.USDA_API_KEY
                if (apiKey.isEmpty() || apiKey == "\"\"") {
                    throw Exception("API Key is missing or invalid")
                }

                // Call the API via the Retrofit client
                val response = RetrofitClient.usdaApi.searchFoods(
                    apiKey = apiKey,
                    query = query
                )
                
                if (response.isSuccessful) {
                    response.body()?.foods ?: emptyList()
                } else {
                    // Handle non-2xx errors gracefully by returning an empty list (or throwing)
                    emptyList()
                }
            } catch (e: Exception) {
                // In a production app, handle exceptions (e.g., network timeout) properly
                e.printStackTrace()
                emptyList()
            }
        }
    }
}
