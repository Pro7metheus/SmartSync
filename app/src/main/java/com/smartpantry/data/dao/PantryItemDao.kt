package com.smartpantry.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.smartpantry.data.model.PantryItem

@Dao
interface PantryItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: PantryItem): Long

    @Update
    suspend fun update(item: PantryItem)

    @Delete
    suspend fun delete(item: PantryItem)

    @Query("SELECT * FROM pantry_items WHERE userId = :userId ORDER BY name ASC")
    fun getAllItems(userId: Long): LiveData<List<PantryItem>>

    @Query("SELECT * FROM pantry_items WHERE userId = :userId ORDER BY name ASC")
    suspend fun getAllItemsList(userId: Long): List<PantryItem>

    @Query("SELECT * FROM pantry_items WHERE id = :id")
    suspend fun getItemById(id: Long): PantryItem?

    @Query("SELECT * FROM pantry_items WHERE category = :category AND userId = :userId ORDER BY name ASC")
    fun getByCategory(category: String, userId: Long): LiveData<List<PantryItem>>

    @Query("SELECT * FROM pantry_items WHERE name LIKE '%' || :query || '%' AND userId = :userId ORDER BY name ASC")
    fun searchByName(query: String, userId: Long): LiveData<List<PantryItem>>

    @Query("SELECT * FROM pantry_items WHERE expiryDate <= :thresholdDate AND expiryDate > 0 AND userId = :userId ORDER BY expiryDate ASC")
    fun getExpiringSoon(thresholdDate: Long, userId: Long): LiveData<List<PantryItem>>

    @Query("SELECT * FROM pantry_items WHERE expiryDate <= :thresholdDate AND expiryDate > 0 AND userId = :userId ORDER BY expiryDate ASC")
    suspend fun getExpiringSoonList(thresholdDate: Long, userId: Long): List<PantryItem>

    @Query("SELECT * FROM pantry_items WHERE quantity <= minQuantity AND userId = :userId ORDER BY name ASC")
    fun getLowStock(userId: Long): LiveData<List<PantryItem>>

    @Query("SELECT * FROM pantry_items WHERE quantity <= minQuantity AND userId = :userId ORDER BY name ASC")
    suspend fun getLowStockList(userId: Long): List<PantryItem>

    @Query("SELECT COUNT(*) FROM pantry_items WHERE userId = :userId")
    fun getTotalCount(userId: Long): LiveData<Int>

    @Query("SELECT COUNT(*) FROM pantry_items WHERE expiryDate <= :thresholdDate AND expiryDate > 0 AND userId = :userId")
    fun getExpiringSoonCount(thresholdDate: Long, userId: Long): LiveData<Int>

    @Query("SELECT COUNT(*) FROM pantry_items WHERE quantity <= 0 AND userId = :userId")
    fun getOutOfStockCount(userId: Long): LiveData<Int>

    @Query("SELECT COUNT(*) FROM pantry_items WHERE quantity <= minQuantity AND quantity > 0 AND userId = :userId")
    fun getLowStockCount(userId: Long): LiveData<Int>

    @Query("SELECT * FROM pantry_items WHERE expiryDate < :now AND expiryDate > 0 AND userId = :userId")
    suspend fun getExpiredItems(now: Long, userId: Long): List<PantryItem>
}
