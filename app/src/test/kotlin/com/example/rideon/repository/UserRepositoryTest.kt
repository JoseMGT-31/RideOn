package com.example.rideon.repository

import com.example.rideon.model.UserEntity
import com.example.rideon.data.UserDao
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class UserRepositoryTest : StringSpec({
    val dao = mockk<UserDao>()
    val repo = UserRepository(dao)

    "register hashes password and inserts user" {
        val name = "Test"
        val email = "t@example.com"
        val password = "secret"

        coEvery { dao.getUserByEmail(email) } returns null
        coEvery { dao.insertUser(any()) } returns Unit

        // Act
        val result = runCatching { kotlinx.coroutines.runBlocking { repo.register(name, email, password) } }

        // Assert
        result.isSuccess shouldBe true
        coVerify(exactly = 1) { dao.insertUser(match { it.email == email && it.name == name && it.password != password }) }
    }

    "login succeeds when password matches hashed value" {
        val email = "t2@example.com"
        val plain = "mypassword"
        // Generar hash real usando bcrypt para simular DB
        val hash = at.favre.lib.crypto.bcrypt.BCrypt.withDefaults().hashToString(12, plain.toCharArray())
        val user = UserEntity(id = 1, name = "U", email = email, password = hash)

        coEvery { dao.getUserByEmail(email) } returns user

        val result = kotlinx.coroutines.runBlocking { repo.login(email, plain) }

        result.isSuccess shouldBe true
        result.getOrNull()?.email shouldBe email
        coVerify(exactly = 1) { dao.getUserByEmail(email) }
    }
})

