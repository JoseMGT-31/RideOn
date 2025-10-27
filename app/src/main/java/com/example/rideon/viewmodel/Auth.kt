package com.example.rideon.viewmodel

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rideon.data.RideOnDatabase
import com.example.rideon.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ---------- LOGIN ----------
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isSubmitting: Boolean = false,
    val submitEnabled: Boolean = false,
    val errorGlobal: String? = null
)
data class LoginErrors(
    val email: String? = null,
    val password: String? = null
)

// ---------- REGISTER ----------
data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirm: String = "",
    val isSubmitting: Boolean = false,
    val submitEnabled: Boolean = false,
    val errorGlobal: String? = null
)
data class RegisterErrors(
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val confirm: String? = null
)

// ------------------ SessionManager (DataStore) ------------------
private val Context.dataStore by preferencesDataStore("session_prefs")

class SessionManager(private val context: Context) {
    companion object {
        private val KEY_LOGGED_IN = booleanPreferencesKey("logged_in")
        private val KEY_EMAIL     = stringPreferencesKey("email")
        private val KEY_NAME      = stringPreferencesKey("name")
        private val KEY_ROLE      = stringPreferencesKey("role")
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { it[KEY_LOGGED_IN] ?: false }
    val userEmail: Flow<String?>  = context.dataStore.data.map { it[KEY_EMAIL] }
    val userName: Flow<String?>   = context.dataStore.data.map { it[KEY_NAME] }
    val userRole: Flow<String?>   = context.dataStore.data.map { it[KEY_ROLE] }
    val isAdmin: Flow<Boolean>    = userRole.map { it == "ADMIN" }

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

// ------------------ Auth ViewModel ------------------
class Auth(application: Application) : AndroidViewModel(application) {

    private val repo: UserRepository by lazy {
        val db = RideOnDatabase.getDatabase(application)
        UserRepository(db.userDao())
    }

    private val session by lazy { SessionManager(getApplication()) }

    // LOGIN
    private val _loginState = MutableStateFlow(LoginUiState())
    val loginState: StateFlow<LoginUiState> = _loginState

    private val _loginErrors = MutableStateFlow(LoginErrors())
    val loginErrors: StateFlow<LoginErrors> = _loginErrors

    // REGISTER
    private val _registerState = MutableStateFlow(RegisterUiState())
    val registerState: StateFlow<RegisterUiState> = _registerState

    private val _registerErrors = MutableStateFlow(RegisterErrors())
    val registerErrors: StateFlow<RegisterErrors> = _registerErrors

    // ------- Handlers LOGIN -------
    fun onLoginEmailChange(v: String) {
        _loginState.update { it.copy(email = v) }
        validateLogin()
    }
    fun onLoginPasswordChange(v: String) {
        _loginState.update { it.copy(password = v) }
        validateLogin()
    }
    private fun validateLogin() {
        val s = _loginState.value
        val errEmail = if (!isValidEmail(s.email)) "Email inválido" else null
        val errPass = if (s.password.length < 6) "Mínimo 6 caracteres" else null
        _loginErrors.value = LoginErrors(errEmail, errPass)
        _loginState.update { it.copy(submitEnabled = errEmail == null && errPass == null) }
    }

    fun submitLogin(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val s = _loginState.value
        validateLogin()
        if (!s.submitEnabled) return
        viewModelScope.launch {
            _loginState.update { it.copy(isSubmitting = true, errorGlobal = null) }
            val result = repo.login(s.email, s.password)
            result.onSuccess { user ->
                // ← Guarda sesión con rol
                session.saveSession(user.name, user.email, user.role)
                _loginState.update { it.copy(isSubmitting = false) }
                onSuccess()
            }.onFailure { e ->
                _loginState.update { it.copy(errorGlobal = e.message ?: "Error al iniciar sesión", isSubmitting = false) }
                onFailure(_loginState.value.errorGlobal!!)
            }
        }
    }

    // ------- Handlers REGISTER -------
    fun onRegisterNameChange(v: String) {
        _registerState.update { it.copy(name = v) }
        validateRegister()
    }
    fun onRegisterEmailChange(v: String) {
        _registerState.update { it.copy(email = v) }
        validateRegister()
    }
    fun onRegisterPasswordChange(v: String) {
        _registerState.update { it.copy(password = v) }
        validateRegister()
    }
    fun onRegisterConfirmChange(v: String) {
        _registerState.update { it.copy(confirm = v) }
        validateRegister()
    }

    private fun validateRegister() {
        val s = _registerState.value
        val errName = if (s.name.length < 2) "Nombre muy corto" else null
        val errEmail = if (!isValidEmail(s.email)) "Email inválido" else null
        val errPass = if (s.password.length < 6) "Mínimo 6 caracteres" else null
        val errConfirm = if (s.confirm != s.password) "No coincide con la contraseña" else null
        _registerErrors.value = RegisterErrors(errName, errEmail, errPass, errConfirm)
        _registerState.update {
            it.copy(submitEnabled = listOf(errName, errEmail, errPass, errConfirm).all { e -> e == null })
        }
    }

    fun submitRegister(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val s = _registerState.value
        validateRegister()
        if (!s.submitEnabled) return
        viewModelScope.launch {
            _registerState.update { it.copy(isSubmitting = true, errorGlobal = null) }
            val result = repo.register(s.name, s.email, s.password)
            result.onSuccess {
                // ← Recién registrado: siempre CLIENT
                session.saveSession(s.name, s.email, "CLIENT")
                _registerState.update { it.copy(isSubmitting = false) }
                onSuccess()
            }.onFailure { e ->
                _registerState.update { it.copy(errorGlobal = e.message ?: "No se pudo registrar", isSubmitting = false) }
                onFailure(_registerState.value.errorGlobal!!)
            }
        }
    }

    // ------- Sesión / rol expuesto a UI -------
    fun isLoggedInFlow(): Flow<Boolean> = session.isLoggedIn
    fun isAdminFlow(): Flow<Boolean> = session.isAdmin
    fun userRoleFlow(): Flow<String?> = session.userRole

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            session.clearSession()
            onDone()
        }
    }

    // Utilidad
    private fun isValidEmail(email: String): Boolean {
        val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return email.matches(regex)
    }
}
