package com.smartpantry.auth

import android.content.Context
import android.content.SharedPreferences
import java.security.MessageDigest

class AuthManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("smart_pantry_auth", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USERNAME = "username"
        private const val KEY_DAILY_CALORIE_GOAL = "daily_calorie_goal"
        private const val DEFAULT_CALORIE_GOAL = 2000
    }

    val isLoggedIn: Boolean
        get() = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    val currentUserId: Long
        get() = prefs.getLong(KEY_USER_ID, -1)

    val currentUsername: String
        get() = prefs.getString(KEY_USERNAME, "") ?: ""

    var dailyCalorieGoal: Int
        get() = prefs.getInt(KEY_DAILY_CALORIE_GOAL, DEFAULT_CALORIE_GOAL)
        set(value) = prefs.edit().putInt(KEY_DAILY_CALORIE_GOAL, value).apply()

    fun login(userId: Long, username: String) {
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putLong(KEY_USER_ID, userId)
            .putString(KEY_USERNAME, username)
            .apply()
    }

    fun logout() {
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, false)
            .putLong(KEY_USER_ID, -1)
            .putString(KEY_USERNAME, "")
            .apply()
    }

    fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }
}
