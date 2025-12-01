package com.example.rideon.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rideon.model.CartItem
import com.example.rideon.viewmodel.CartViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel = viewModel()
) {
    // Paleta compartida
    val redPrimary = Color(0xFFD32F2F)
    val redDark = Color(0xFFB71C1C)
    val darkGray = Color(0xFF121212)
    val cardGray = Color(0xFF1F1F1F)
    val onDark = Color(0xFFFFFFFF)

    val uiState by cartViewModel.uiState.collectAsState()
    val snackbarHostState = androidx.compose.runtime.remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito", color = onDark) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = redDark,
                    titleContentColor = onDark,
                    actionIconContentColor = onDark
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (uiState.items.isNotEmpty()) {
                CartBottomBar(
                    total = uiState.total,
                    onConfirm = {
                        cartViewModel.confirmOrder(
                            onSuccess = {
                                scope.launch {
                                    snackbarHostState.showSnackbar("¡Pedido confirmado!")
                                    navController.navigate("history")
                                }
                            }
                        )
                    },
                    redPrimary = redPrimary,
                    cardGray = cardGray,
                    onDark = onDark
                )
            }
        }
    ) { innerPadding ->
        if (uiState.items.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding).background(darkGray), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.ShoppingCart, null, Modifier.size(80.dp), tint = Color.Gray)
                    Text("Carrito vacío", color = onDark)
                    Button(onClick = { navController.navigate("catalogo") }, colors = ButtonDefaults.buttonColors(containerColor = redPrimary, contentColor = onDark)) { Text("Ir al Catálogo") }
                }
            }
        } else {
            LazyColumn(modifier = Modifier.padding(innerPadding).padding(16.dp).background(darkGray), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(uiState.items) { item ->
                    CartItemRow(item, onRemove = { cartViewModel.removeItem(item.id) }, cardGray = cardGray, onDark = onDark, redPrimary = redPrimary)
                }
            }
        }
    }
}


@Composable
fun CartItemRow(item: CartItem, onRemove: () -> Unit, cardGray: Color = Color(0xFF1F1F1F), onDark: Color = Color.White, redPrimary: Color = Color(0xFFD32F2F)) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = cardGray)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            val resId = item.imageUrl.toIntOrNull()
            if (resId != null) {
                Image(painter = painterResource(id = resId), contentDescription = null, modifier = Modifier.size(80.dp).clip(MaterialTheme.shapes.medium), contentScale = ContentScale.Crop)
            } else {
                Surface(Modifier.size(80.dp), color = Color.LightGray, shape = MaterialTheme.shapes.medium) {}
            }
            Column(Modifier.weight(1f).padding(horizontal = 16.dp)) {
                Text(item.name, style = MaterialTheme.typography.titleMedium, color = onDark)
                Text("$ ${item.price.toInt()}", color = redPrimary)
                Text("Cant: ${item.quantity}", style = MaterialTheme.typography.bodySmall, color = onDark)
            }
            IconButton(onClick = onRemove) { Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error) }
        }
    }
}

@Composable
fun CartBottomBar(total: Double, onConfirm: () -> Unit, redPrimary: Color = Color(0xFFD32F2F), cardGray: Color = Color(0xFF1F1F1F), onDark: Color = Color.White) {
    Surface(shadowElevation = 8.dp, color = cardGray) {
        Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text("Total", style = MaterialTheme.typography.bodySmall, color = onDark)
                Text("$ ${total.toInt()}", style = MaterialTheme.typography.headlineSmall, color = onDark)
            }
            Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors(containerColor = redPrimary, contentColor = onDark)) {
                Text("Confirmar Pedido")
            }
        }
    }
}