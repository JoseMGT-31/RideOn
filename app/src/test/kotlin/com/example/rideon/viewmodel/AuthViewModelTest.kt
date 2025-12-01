package com.example.rideon.viewmodel

import com.example.rideon.model.UserEntity
import com.example.rideon.repository.UserRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest : StringSpec({
    val testDispatcher = StandardTestDispatcher()

    beforeTest {
        Dispatchers.setMain(testDispatcher)
    }
    afterTest {
        Dispatchers.resetMain()
    }

    "validateLogin enables submit only when fields valid" {
        runTest {
            val repo = mockk<UserRepository>()
            val session = mockk<SessionManagerType>(relaxed = true)
            val vm = Auth(application = mockk(relaxed = true), repoOverride = repo, sessionOverride = session)

            vm.onLoginEmailChange("bad")
            vm.onLoginPasswordChange("123")
            vm.loginErrors.value.email shouldBe "Email inválido"
            vm.loginErrors.value.password shouldBe "Mínimo 6 caracteres"
            vm.loginState.value.submitEnabled shouldBe false

            vm.onLoginEmailChange("ok@example.com")
            vm.onLoginPasswordChange("123456")
            vm.loginErrors.value.email shouldBe null
            vm.loginErrors.value.password shouldBe null
            vm.loginState.value.submitEnabled shouldBe true
        }
    }

    "submitLogin success updates state and calls saveSession" {
        runTest {
            val repo = mockk<UserRepository>()
            val session = mockk<SessionManagerType>(relaxed = true)
            val vm = Auth(application = mockk(relaxed = true), repoOverride = repo, sessionOverride = session)

            val user = UserEntity(id = 1, name = "U", email = "ok@example.com", password = "hash", role = "CLIENT")
            coEvery { repo.login("ok@example.com", "password") } returns Result.success(user)

            vm.onLoginEmailChange("ok@example.com")
            vm.onLoginPasswordChange("password")

            var successCalled = false
            var failureMessage: String? = null

            vm.submitLogin(onSuccess = { successCalled = true }, onFailure = { failureMessage = it })

            // avanzar dispatcher
            testDispatcher.scheduler.advanceUntilIdle()

            successCalled shouldBe true
            failureMessage shouldBe null
            coVerify(exactly = 1) { session.saveSession(user.name, user.email, user.role) }
        }
    }

    "submitLogin failure updates error state" {
        runTest {
            val repo = mockk<UserRepository>()
            val session = mockk<SessionManagerType>(relaxed = true)
            val vm = Auth(application = mockk(relaxed = true), repoOverride = repo, sessionOverride = session)

            // Usar password de tamaño >=6 para pasar la validación y llegar al repo
            coEvery { repo.login("a@b.com", "badpwd") } returns Result.failure(IllegalArgumentException("Contraseña incorrecta"))

            vm.onLoginEmailChange("a@b.com")
            vm.onLoginPasswordChange("badpwd")

            var successCalled = false
            var failureMessage: String? = null

            vm.submitLogin(onSuccess = { successCalled = true }, onFailure = { failureMessage = it })

            testDispatcher.scheduler.advanceUntilIdle()

            successCalled shouldBe false
            failureMessage shouldBe "Contraseña incorrecta"
        }
    }

    "submitRegister success saves session" {
        runTest {
            val repo = mockk<UserRepository>()
            val session = mockk<SessionManagerType>(relaxed = true)
            val vm = Auth(application = mockk(relaxed = true), repoOverride = repo, sessionOverride = session)

            coEvery { repo.register("Name","r@example.com","pass123") } returns Result.success(Unit)

            vm.onRegisterNameChange("Name")
            vm.onRegisterEmailChange("r@example.com")
            vm.onRegisterPasswordChange("pass123")
            vm.onRegisterConfirmChange("pass123")

            var successCalled = false
            vm.submitRegister(onSuccess = { successCalled = true }, onFailure = { })
            testDispatcher.scheduler.advanceUntilIdle()

            successCalled shouldBe true
            coVerify(exactly = 1) { session.saveSession("Name","r@example.com","CLIENT") }
        }
    }

    "logout clears session and calls onDone" {
        runTest {
            val repo = mockk<UserRepository>()
            val session = mockk<SessionManagerType>(relaxed = true)
            val vm = Auth(application = mockk(relaxed = true), repoOverride = repo, sessionOverride = session)

            var done = false
            vm.logout { done = true }
            testDispatcher.scheduler.advanceUntilIdle()

            done shouldBe true
            coVerify(exactly = 1) { session.clearSession() }
        }
    }
})
