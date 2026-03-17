package com.smartpantry.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.smartpantry.R
import com.smartpantry.ui.MainActivity

object NotificationHelper {

    private const val CHANNEL_EXPIRY = "expiry_alerts"
    private const val CHANNEL_LOW_STOCK = "low_stock_alerts"

    fun createNotificationChannels(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val expiryChannel = NotificationChannel(
            CHANNEL_EXPIRY,
            "Expiry Alerts",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Alerts for items nearing or past expiry date"
        }

        val lowStockChannel = NotificationChannel(
            CHANNEL_LOW_STOCK,
            "Low Stock Alerts",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Alerts for items running low on stock"
        }

        notificationManager.createNotificationChannel(expiryChannel)
        notificationManager.createNotificationChannel(lowStockChannel)
    }

    fun showExpiryNotification(context: Context, title: String, message: String, notificationId: Int) {
        showNotification(context, CHANNEL_EXPIRY, title, message, notificationId)
    }

    fun showLowStockNotification(context: Context, title: String, message: String, notificationId: Int) {
        showNotification(context, CHANNEL_LOW_STOCK, title, message, notificationId)
    }

    private fun showNotification(
        context: Context,
        channelId: String,
        title: String,
        message: String,
        notificationId: Int
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notification)
    }
}
