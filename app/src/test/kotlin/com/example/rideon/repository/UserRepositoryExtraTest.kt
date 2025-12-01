package com.example.rideon.repository

import com.example.rideon.data.UserDao
import com.example.rideon.model.UserEntity
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking

class UserRepositoryExtraTest : StringSpec({

    "register returns failure when email already exists" {
        val dao = mockk<UserDao>()
        val repo = UserRepository(dao)

        val existing = UserEntity(id = 1, name = "A", email = "a@b.com", password = "x")
        coEvery { dao.getUserByEmail("a@b.com") } returns existing

        val result = runBlocking { repo.register("New","a@b.com","pwd") }
        result.isFailure shouldBe true
        result.exceptionOrNull()?.message shouldBe "El email ya está registrado"
        coVerify(exactly = 0) { dao.insertUser(any()) }
    }

    "login returns failure when user not found" {
        val dao = mockk<UserDao>()
        val repo = UserRepository(dao)

        coEvery { dao.getUserByEmail("no@one.com") } returns null

        val result = runBlocking { repo.login("no@one.com","p") }
        result.isFailure shouldBe true
        result.exceptionOrNull()?.message shouldBe "Usuario no encontrado"
    }

    "login accepts plain-text password if stored as plain" {
        val dao = mockk<UserDao>()
        val repo = UserRepository(dao)
        val plain = "secret"
        val user = UserEntity(id = 2, name = "U", email = "u@u.com", password = plain)
        coEvery { dao.getUserByEmail("u@u.com") } returns user

        val result = runBlocking { repo.login("u@u.com", plain) }
        result.isSuccess shouldBe true
        result.getOrNull()?.email shouldBe "u@u.com"
    }

    "login fails with incorrect password" {
        val dao = mockk<UserDao>()
        val repo = UserRepository(dao)
        val hash = at.favre.lib.crypto.bcrypt.BCrypt.withDefaults().hashToString(12, "right".toCharArray())
        val user = UserEntity(id = 3, name = "U", email = "x@x.com", password = hash)
        coEvery { dao.getUserByEmail("x@x.com") } returns user

        val result = runBlocking { repo.login("x@x.com", "wrong") }
        result.isFailure shouldBe true
        result.exceptionOrNull()?.message shouldBe "Contraseña incorrecta"
    }
})

