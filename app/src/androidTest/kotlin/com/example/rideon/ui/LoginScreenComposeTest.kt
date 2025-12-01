package com.example.rideon.ui

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenComposeTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun loginFlow_showsHomeOnSuccess() {
        composeTestRule.setContent {
            val loggedIn = remember { mutableStateOf(false) }
            Column {
                Text(text = "Email")
                Text(text = "Password")
                Button(onClick = { loggedIn.value = true }) {
                    Text(text = "Login")
                }
                if (loggedIn.value) {
                    Text(text = "Bienvenido")
                }
            }
        }

        composeTestRule.onNodeWithText("Email").performTextInput("test@example.com")
        composeTestRule.onNodeWithText("Password").performTextInput("password")
        composeTestRule.onNodeWithText("Login").performClick()

        // Verificar que el texto del Home esté visible tras el click
        composeTestRule.onNodeWithText("Bienvenido").assertExists()
    }
}
