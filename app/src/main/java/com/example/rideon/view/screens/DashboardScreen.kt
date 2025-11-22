package com.example.rideon.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rideon.R
import com.example.rideon.viewmodel.Auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    vm: Auth = viewModel(),
    onNavigateTo: (String) -> Unit // Callback para navegar a otras rutas
){
    // Obtiene el nombre y el estado de administrador del ViewModel de autenticación
    val userName by vm.userRoleFlow().collectAsState(initial = null)
    val isAdmin by vm.isAdminFlow().collectAsState(initial = false)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("rideON • Principal") },
                actions = {
                    // Botón para cerrar sesión
                    IconButton(onClick = { vm.logout { onNavigateTo("home") } }) {
                        Icon(Icons.Filled.Logout, contentDescription = "Cerrar sesión")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            // Sección de Bienvenida
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "¡Bienvenido, ${userName ?: "Usuario"}!",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    if (isAdmin) {
                        Text(
                            text = "(Administrador)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Encuentra la moto de tus sueños.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Fila de opciones principales
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    NavCard(
                        title = "Catálogo",
                        icon = Icons.Filled.Store,
                        destination = "catalogo",
                        onNavigate = onNavigateTo,
                        modifier = Modifier.weight(1f)
                    )
                    NavCard(
                        title = "Mi Perfil",
                        icon = Icons.Filled.AccountCircle,
                        destination = "profile",
                        onNavigate = onNavigateTo,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Fila de opciones secundarias
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    NavCard(
                        title = "Mi Carrito",
                        icon = Icons.Filled.ShoppingCart,
                        destination = "cart",
                        onNavigate = onNavigateTo,
                        modifier = Modifier.weight(1f)
                    )
                    NavCard(
                        title = "Mis Órdenes",
                        icon = Icons.Filled.ListAlt,
                        destination = "history",
                        onNavigate = onNavigateTo,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (isAdmin) {
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { onNavigateTo("inventarioForm") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)) // Un color morado para admin
                    ) {
                        Text("Gestión de Inventario")
                    }
                }
            }
        }
    }
}



@Composable
fun NavCard(
    title: String,
    icon: ImageVector,
    destination: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(120.dp)
            .clickable { onNavigate(destination) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}