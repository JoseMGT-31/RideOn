package com.example.rideon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rideon.ui.theme.RideOnTheme
import com.example.rideon.view.screens.HomeAuthScreen
import com.example.rideon.view.screens.LoginScreen
import com.example.rideon.view.screens.RegisterScreen
import com.example.rideon.view.screens.CatalogoScreen
import com.example.rideon.data.Catalogo
import com.example.rideon.model.CartItem
import com.example.rideon.view.screens.CartScreen
import com.example.rideon.view.screens.ProductoScreen
import com.example.rideon.viewmodel.CartViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNav()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNav() {
    RideOnTheme {
        val navController = rememberNavController()
        val cartViewModel: CartViewModel = viewModel()

        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            // Home (auth landing)
            composable("home") {
                HomeAuthScreen(
                    onLogin = { navController.navigate("login") },
                    onRegister = { navController.navigate("register") }
                )
            }

            // Login -> al loguear, ir a catalogo y limpiar back stack
            composable("login") {
                LoginScreen(
                    onLoggedIn = {
                        navController.navigate("catalogo") {
                            popUpTo("home") { inclusive = true } // quita home del back stack
                            launchSingleTop = true
                        }
                    }
                )
            }

            // Register -> al registrar, podrías volver o ir a login/catálogo (a elección)
            composable("register") {
                RegisterScreen(
                    onRegistered = {
                        navController.popBackStack()
                    }
                )
            }

            // Catálogo
            composable("catalogo") {
                CatalogoScreen(
                    productos = Catalogo.productos,
                    onOpen = { pdt ->
                        navController.navigate("detalle/${pdt.id}")
                    },
                    onOpenCart = {
                        navController.navigate("cart")
                    }
                )
            }


            composable(
                route = "detalle/{id}",
                arguments = listOf(
                    navArgument("id") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id")
                val producto = Catalogo.productos.firstOrNull { it.id == id }

                if (producto != null) {
                    ProductoScreen(
                        producto = producto,
                        onBack = { navController.popBackStack() },
                        onAddToCart = { qty ->
                            cartViewModel.addItem(
                                CartItem(
                                    id = producto.id.toString(),
                                    name = "${producto.brand} ${producto.model}",
                                    price = producto.priceClp.toDouble(),
                                    quantity = qty,
                                    imageUrl = producto.imageRes.toString()
                                )
                            )
                        }
                    )
                } else {
                    Scaffold(topBar = { TopAppBar(title = { Text("Producto") }) }) { p ->
                        Text(
                            "Producto no encontrado",
                            modifier = Modifier.padding(p).padding(16.dp)
                        )
                    }
                }
            }

            composable("cart") {
                CartScreen(cartViewModel = cartViewModel)
            }
        }
    }
}
