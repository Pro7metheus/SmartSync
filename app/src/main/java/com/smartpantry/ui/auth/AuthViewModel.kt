package com.smartpantry.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartpantry.SmartPantryApp
import com.smartpantry.data.model.User
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as SmartPantryApp
    private val userDao = app.database.userDao()
    private val authManager = app.authManager

    private val _authResult = MutableLiveData<AuthResult>()
    val authResult: LiveData<AuthResult> = _authResult

    fun login(username: String, password: String) {
        if (username.isBlank()) {
            _authResult.value = AuthResult.Error("Username is required")
            return
        }
        if (password.isBlank()) {
            _authResult.value = AuthResult.Error("Password is required")
            return
        }

        viewModelScope.launch {
            val user = userDao.getByUsername(username.trim())
            if (user == null) {
                _authResult.value = AuthResult.Error("User not found")
                return@launch
            }
            val hashedPassword = authManager.hashPassword(password)
            if (user.passwordHash != hashedPassword) {
                _authResult.value = AuthResult.Error("Incorrect password")
                return@launch
            }
            authManager.login(user.id, user.username)
            _authResult.value = AuthResult.Success
        }
    }

    fun register(username: String, password: String, confirmPassword: String) {
        if (username.isBlank()) {
            _authResult.value = AuthResult.Error("Username is required")
            return
        }
        if (username.trim().length < 3) {
            _authResult.value = AuthResult.Error("Username must be at least 3 characters")
            return
        }
        if (password.isBlank()) {
            _authResult.value = AuthResult.Error("Password is required")
            return
        }
        if (password.length < 4) {
            _authResult.value = AuthResult.Error("Password must be at least 4 characters")
            return
        }
        if (password != confirmPassword) {
            _authResult.value = AuthResult.Error("Passwords do not match")
            return
        }

        viewModelScope.launch {
            val existing = userDao.getByUsername(username.trim())
            if (existing != null) {
                _authResult.value = AuthResult.Error("Username already taken")
                return@launch
            }
            val hashedPassword = authManager.hashPassword(password)
            val user = User(username = username.trim(), passwordHash = hashedPassword)
            val userId = userDao.insert(user)
            authManager.login(userId, username.trim())
            _authResult.value = AuthResult.Success
        }
    }

    sealed class AuthResult {
        object Success : AuthResult()
        data class Error(val message: String) : AuthResult()
    }
}
