package com.smartpantry.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.smartpantry.data.dao.MealLogDao
import com.smartpantry.data.dao.PantryItemDao
import com.smartpantry.data.dao.UserDao
import com.smartpantry.data.model.MealLog
import com.smartpantry.data.model.PantryItem
import com.smartpantry.data.model.User

@Database(
    entities = [PantryItem::class, User::class, MealLog::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun pantryItemDao(): PantryItemDao
    abstract fun userDao(): UserDao
    abstract fun mealLogDao(): MealLogDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smart_pantry_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
