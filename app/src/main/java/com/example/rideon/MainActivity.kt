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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
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
import com.example.rideon.view.screens.DashboardScreen
import com.example.rideon.view.screens.ProfileScreen
import com.example.rideon.ui.screens.OrderHistoryScreen
import com.example.rideon.view.screens.InventarioFormScreen
import com.example.rideon.viewmodel.InventarioViewModel
import com.example.rideon.viewmodel.Auth

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
        // Observa el estado de sesión para redirigir automáticamente si ya está logueado
        val auth: Auth = viewModel()
        val isLoggedIn by auth.isLoggedInFlow().collectAsState(initial = false)

        // Si el usuario ya está logueado, navega a dashboard al iniciar la composición
        LaunchedEffect(key1 = isLoggedIn) {
            if (isLoggedIn) {
                navController.navigate("dashboard") {
                    popUpTo("home") { inclusive = true }
                    launchSingleTop = true
                }
            }
        }

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


            composable("login") {
                LoginScreen(
                    onLoggedIn = {
                        navController.navigate("dashboard") { // CAMBIO: Navega a dashboard
                            popUpTo("home") { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onGoRegister = { navController.navigate("register") }
                )
            }


            composable("register") {
                RegisterScreen(
                    onRegistered = {
                        navController.navigate("dashboard") { // CAMBIO: Navega a dashboard
                            popUpTo("home") { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onGoLogin = { navController.navigate("login") }
                )
            }

            // Dashboard (Nueva pantalla principal después del login)
            composable("dashboard") {
                DashboardScreen(
                    onNavigateTo = { route -> navController.navigate(route) }
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

            // Detalle del producto
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

            // Carrito
            composable("cart") {
                CartScreen(
                    navController = navController,
                    cartViewModel = cartViewModel
                )
            }

            // Perfil
            composable("profile") {
                ProfileScreen()
            }

            // Historial de Órdenes
            composable("history") {
                OrderHistoryScreen()
            }

            // Gestión de Inventario (Ruta para Administradores)
            composable("inventarioForm") {
                InventarioFormScreen(
                    vm = viewModel<InventarioViewModel>(),
                    defaultImageRes = R.drawable.z900, // Usando la imagen de la Z900 como ejemplo de imagen por defecto
                    onSaved = { navController.popBackStack() },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
