package com.example.rideon.view.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rideon.viewmodel.Auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    vm: Auth = viewModel(),
    onNavigateTo: (String) -> Unit
){
    // Obtiene el nombre y el estado de administrador del ViewModel de autenticación
    val userName by vm.userRoleFlow().collectAsState(initial = null)
    val isAdmin by vm.isAdminFlow().collectAsState(initial = false)

    // Paleta personalizada: rojo principal, gris oscuro y negro
    val redPrimary = Color(0xFFD32F2F)
    val redDark = Color(0xFFB71C1C)
    val darkGray = Color(0xFF121212)
    val cardGray = Color(0xFF1F1F1F)
    val onDark = Color(0xFFFFFFFF)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("rideON • Principal", color = onDark) },
                actions = {
                    // Botón para cerrar sesión
                    IconButton(onClick = { vm.logout { onNavigateTo("home") } }) {
                        Icon(Icons.Filled.Logout, contentDescription = "Cerrar sesión", tint = onDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = redDark,
                    titleContentColor = onDark,
                    actionIconContentColor = onDark
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(darkGray)
        ) {
            // Sección de Bienvenida
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(redPrimary),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "¡Bienvenido, ${userName ?: "Usuario"}!",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = onDark
                    )
                    if (isAdmin) {
                        Text(
                            text = "(Administrador)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = onDark.copy(alpha = 0.9f)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Encuentra la moto de tus sueños.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = onDark
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
                        modifier = Modifier.weight(1f),
                        cardBackground = cardGray,
                        iconTint = redPrimary,
                        titleColor = onDark
                    )
                    NavCard(
                        title = "Mi Perfil",
                        icon = Icons.Filled.AccountCircle,
                        destination = "profile",
                        onNavigate = onNavigateTo,
                        modifier = Modifier.weight(1f),
                        cardBackground = cardGray,
                        iconTint = redPrimary,
                        titleColor = onDark
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
                        modifier = Modifier.weight(1f),
                        cardBackground = cardGray,
                        iconTint = redPrimary,
                        titleColor = onDark
                    )
                    NavCard(
                        title = "Mis Órdenes",
                        icon = Icons.Filled.ListAlt,
                        destination = "history",
                        onNavigate = onNavigateTo,
                        modifier = Modifier.weight(1f),
                        cardBackground = cardGray,
                        iconTint = redPrimary,
                        titleColor = onDark
                    )
                }
                if (isAdmin) {
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { onNavigateTo("admin/productos") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = redDark, contentColor = onDark)
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
    modifier: Modifier = Modifier,
    cardBackground: Color = Color(0xFF1F1F1F),
    iconTint: Color = Color(0xFFD32F2F),
    titleColor: Color = Color.White
) {
    Card(
        modifier = modifier
            .height(120.dp)
            .clickable { onNavigate(destination) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground)
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
                tint = iconTint
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = titleColor
            )
        }
    }
}