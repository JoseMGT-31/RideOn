package com.example.rideon.repository

import com.example.rideon.data.UserDao
import com.example.rideon.model.UserEntity

class UserRepository(private val dao: UserDao) {

    suspend fun register(name: String, email: String, password: String): Result<Unit> {
        // Revisar si ya existe el correo
        val existing = dao.getUserByEmail(email)
        if (existing != null) {
            return Result.failure(IllegalStateException("El email ya está registrado"))
        }

        // Insertar datos
        dao.insertUser(UserEntity(name = name, email = email, password = password))
        return Result.success(Unit)
    }

    suspend fun login(email: String, password: String): Result<UserEntity> {
        val user = dao.getUserByEmail(email)
        return when {
            user == null -> Result.failure(IllegalArgumentException("Usuario no encontrado"))
            user.password != password -> Result.failure(IllegalArgumentException("Contraseña incorrecta"))
            else -> Result.success(user)
        }
    }
}