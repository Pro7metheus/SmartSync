package com.smartpantry.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.smartpantry.R
import com.smartpantry.SmartPantryApp
import com.smartpantry.databinding.ActivityMainBinding
import com.smartpantry.workers.ExpiryCheckWorker
import com.smartpantry.workers.LowStockCheckWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)

        // Hide bottom nav on auth screens
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.registerFragment -> {
                    binding.bottomNav.visibility = View.GONE
                }
                else -> {
                    binding.bottomNav.visibility = View.VISIBLE
                }
            }
        }

        // Check if user is already logged in
        val app = application as SmartPantryApp
        if (app.authManager.isLoggedIn) {
            navController.navigate(R.id.action_login_to_dashboard)
        }

        // Schedule background workers
        scheduleWorkers()
    }

    private fun scheduleWorkers() {
        val expiryWork = PeriodicWorkRequestBuilder<ExpiryCheckWorker>(
            12, TimeUnit.HOURS
        ).build()

        val lowStockWork = PeriodicWorkRequestBuilder<LowStockCheckWorker>(
            12, TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(this).apply {
            enqueueUniquePeriodicWork(
                "expiry_check",
                ExistingPeriodicWorkPolicy.KEEP,
                expiryWork
            )
            enqueueUniquePeriodicWork(
                "low_stock_check",
                ExistingPeriodicWorkPolicy.KEEP,
                lowStockWork
            )
        }
    }
}
