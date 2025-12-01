package com.example.rideon.repository

import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.rideon.data.UserDao
import com.example.rideon.model.UserEntity

class UserRepository(private val dao: UserDao) {

    suspend fun register(name: String, email: String, password: String): Result<Unit> {
        // Revisar si ya existe el correo
        val existing = dao.getUserByEmail(email)
        if (existing != null) {
            return Result.failure(IllegalStateException("El email ya está registrado"))
        }
        // Hashear la contraseña antes de insertar
        val hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray())
        // Insertar datos con la contraseña hasheada
        dao.insertUser(UserEntity(name = name, email = email, password = hashedPassword))
        return Result.success(Unit)
    }

    suspend fun login(email: String, password: String): Result<UserEntity> {
        val user = dao.getUserByEmail(email)
        if (user == null) {
            return Result.failure(IllegalArgumentException("Usuario no encontrado"))
        }

        // Verificar con BCrypt (si la contraseña almacenada está hasheada)
        val isVerifiedByBCrypt = try {
            BCrypt.verifyer().verify(password.toCharArray(), user.password.toCharArray()).verified
        } catch (_: Exception) {
            false
        }

        // Compatibilidad con contraseñas almacenadas en texto plano (legacy)
        val isVerifiedByPlainText = user.password == password

        return when {
            isVerifiedByBCrypt || isVerifiedByPlainText -> Result.success(user)
            else -> Result.failure(IllegalArgumentException("Contraseña incorrecta"))
        }
    }
}