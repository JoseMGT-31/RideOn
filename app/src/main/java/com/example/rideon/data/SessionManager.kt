package com.example.rideon.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("session_prefs")

class SessionManager(private val context: Context) {
    companion object {
        private val KEY_LOGGED_IN = booleanPreferencesKey("logged_in")
        private val KEY_EMAIL     = stringPreferencesKey("email")
        private val KEY_NAME      = stringPreferencesKey("name")
        private val KEY_ROLE      = stringPreferencesKey("role")
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { it[KEY_LOGGED_IN] ?: false }
    val userEmail: Flow<String?> = context.dataStore.data.map { it[KEY_EMAIL] }
    val userName: Flow<String?> = context.dataStore.data.map { it[KEY_NAME] }
    val userRole: Flow<String?> = context.dataStore.data.map { it[KEY_ROLE] }
    val isAdmin: Flow<Boolean>   = userRole.map { it == "ADMIN" }

    suspend fun saveSession(name: String, email: String, role: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_LOGGED_IN] = true
            prefs[KEY_EMAIL] = email
            prefs[KEY_NAME] = name
            prefs[KEY_ROLE] = role
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}