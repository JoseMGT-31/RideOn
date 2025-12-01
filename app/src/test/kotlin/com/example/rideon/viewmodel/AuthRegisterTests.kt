package com.example.rideon.viewmodel

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
class AuthRegisterTests : StringSpec({
    val testDispatcher = StandardTestDispatcher()

    beforeTest {
        Dispatchers.setMain(testDispatcher)
    }
    afterTest {
        Dispatchers.resetMain()
    }

    "validateRegister disables submit when invalid" {
        runTest {
            val repo = mockk<UserRepository>()
            val session = mockk<SessionManagerType>(relaxed = true)
            val vm = Auth(application = mockk(relaxed = true), repoOverride = repo, sessionOverride = session)

            vm.onRegisterNameChange("A")
            vm.onRegisterEmailChange("bad")
            vm.onRegisterPasswordChange("123")
            vm.onRegisterConfirmChange("321")

            vm.registerErrors.value.name shouldBe "Nombre muy corto"
            vm.registerErrors.value.email shouldBe "Email inválido"
            vm.registerErrors.value.password shouldBe "Mínimo 6 caracteres"
            vm.registerErrors.value.confirm shouldBe "No coincide con la contraseña"
            vm.registerState.value.submitEnabled shouldBe false
        }
    }

    "submitRegister failure surfaces error" {
        runTest {
            val repo = mockk<UserRepository>()
            val session = mockk<SessionManagerType>(relaxed = true)
            val vm = Auth(application = mockk(relaxed = true), repoOverride = repo, sessionOverride = session)

            coEvery { repo.register("Name","r@example.com","pass123") } returns Result.failure(IllegalStateException("DB error"))

            vm.onRegisterNameChange("Name")
            vm.onRegisterEmailChange("r@example.com")
            vm.onRegisterPasswordChange("pass123")
            vm.onRegisterConfirmChange("pass123")

            var failedMessage: String? = null
            var success = false
            vm.submitRegister(onSuccess = { success = true }, onFailure = { failedMessage = it })

            testDispatcher.scheduler.advanceUntilIdle()

            success shouldBe false
            failedMessage shouldBe "DB error"
            coVerify(exactly = 1) { repo.register("Name","r@example.com","pass123") }
        }
    }
})

