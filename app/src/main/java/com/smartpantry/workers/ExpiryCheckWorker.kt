package com.smartpantry.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.smartpantry.SmartPantryApp
import com.smartpantry.utils.DateUtils
import com.smartpantry.utils.NotificationHelper

class ExpiryCheckWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val app = applicationContext as SmartPantryApp
        val userId = app.authManager.currentUserId
        if (userId == -1L) return Result.success()

        val dao = app.database.pantryItemDao()

        // Check expired items
        val now = System.currentTimeMillis()
        val expiredItems = dao.getExpiredItems(now, userId)
        if (expiredItems.isNotEmpty()) {
            val names = expiredItems.take(3).joinToString(", ") { it.name }
            val extra = if (expiredItems.size > 3) " and ${expiredItems.size - 3} more" else ""
            NotificationHelper.showExpiryNotification(
                applicationContext,
                "⚠️ Items Expired!",
                "$names$extra have expired. Check your pantry!",
                1001
            )
        }

        // Check expiring soon (within 3 days)
        val thresholdDate = DateUtils.getThresholdDate(3)
        val expiringSoon = dao.getExpiringSoonList(thresholdDate, userId)
            .filter { !DateUtils.isExpired(it.expiryDate) }
        if (expiringSoon.isNotEmpty()) {
            val names = expiringSoon.take(3).joinToString(", ") { it.name }
            val extra = if (expiringSoon.size > 3) " and ${expiringSoon.size - 3} more" else ""
            NotificationHelper.showExpiryNotification(
                applicationContext,
                "🕐 Items Expiring Soon",
                "$names$extra will expire within 3 days.",
                1002
            )
        }

        return Result.success()
    }
}
