package com.example.rideon.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rideon.data.RideOnDatabase
import com.example.rideon.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

class Auth(application: Application) : AndroidViewModel(application) {

    private val repo: UserRepository by lazy {
        val db = RideOnDatabase.getDatabase(application)
        UserRepository(db.userDao())
    }

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
            result.onSuccess {
                onSuccess()
            }.onFailure { e ->
                _loginState.update { it.copy(errorGlobal = e.message ?: "Error al iniciar sesión") }
                onFailure(_loginState.value.errorGlobal!!)
            }
            _loginState.update { it.copy(isSubmitting = false) }
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
                onSuccess()
            }.onFailure { e ->
                _registerState.update { it.copy(errorGlobal = e.message ?: "No se pudo registrar") }
                onFailure(_registerState.value.errorGlobal!!)
            }
            _registerState.update { it.copy(isSubmitting = false) }
        }
    }

    // Utilidad
    private fun isValidEmail(email: String): Boolean {
        val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return email.matches(regex)
    }
}