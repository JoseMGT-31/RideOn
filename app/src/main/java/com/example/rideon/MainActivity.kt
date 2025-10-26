package com.example.rideon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rideon.ui.theme.RideOnTheme
import com.example.rideon.view.screens.HomeAuthScreen
import com.example.rideon.view.screens.LoginScreen
import com.example.rideon.view.screens.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RideOnTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        HomeAuthScreen(
                            onLogin = {
                                navController.navigate("login")
                            },
                            onRegister = {
                                navController.navigate("register")
                            }
                        )
                    }
                    composable("login") {
                        LoginScreen(
                            onLoggedIn = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable("register") {
                        RegisterScreen(
                            onRegistered = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}