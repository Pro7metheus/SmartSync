package com.smartpantry.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.smartpantry.SmartPantryApp
import com.smartpantry.utils.NotificationHelper

class LowStockCheckWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val app = applicationContext as SmartPantryApp
        val userId = app.authManager.currentUserId
        if (userId == -1L) return Result.success()

        val dao = app.database.pantryItemDao()
        val lowStockItems = dao.getLowStockList(userId)

        if (lowStockItems.isNotEmpty()) {
            val names = lowStockItems.take(3).joinToString(", ") { it.name }
            val extra = if (lowStockItems.size > 3) " and ${lowStockItems.size - 3} more" else ""
            NotificationHelper.showLowStockNotification(
                applicationContext,
                "📦 Low Stock Alert",
                "$names$extra are running low. Time to restock!",
                2001
            )
        }

        return Result.success()
    }
}
