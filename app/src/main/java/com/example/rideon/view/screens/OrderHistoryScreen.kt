package com.example.rideon.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rideon.model.Order
import com.example.rideon.viewmodel.OrderHistoryUiState
import com.example.rideon.viewmodel.OrderHistoryViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.graphics.Color // Importar Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(viewModel: OrderHistoryViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    // Paleta
    val redPrimary = Color(0xFFD32F2F)
    val redDark = Color(0xFFB71C1C)
    val darkGray = Color(0xFF121212)
    val cardGray = Color(0xFF1F1F1F)
    val onDark = Color(0xFFFFFFFF)
    val secondaryText = Color(0xFFBDBDBD)


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Historial de Compras", color = onDark) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = redDark, titleContentColor = onDark)
            )
        },
        containerColor = darkGray // Fondo oscuro para la pantalla completa
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = redPrimary) // Color visible para el indicador
                }
            }
            uiState.orders.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Aún no has realizado ninguna compra.", color = onDark) // Texto blanco
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.orders) { order ->
                        OrderRow(order = order, cardColor = cardGray, titleColor = onDark, bodyColor = secondaryText, totalColor = redPrimary)
                    }
                }
            }
        }
    }
}

@Composable
fun OrderRow(
    order: Order,
    cardColor: Color,
    titleColor: Color,
    bodyColor: Color,
    totalColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor) // Color de fondo de la tarjeta
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Pedido #${order.id}",
                style = MaterialTheme.typography.titleMedium,
                color = titleColor // Título blanco
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Fecha: ${order.date.toFormattedString()}",
                style = MaterialTheme.typography.bodySmall,
                color = bodyColor // Texto secundario (gris claro)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Total: $${order.total.toInt()}",
                style = MaterialTheme.typography.bodyLarge,
                color = totalColor // Total en rojo
            )
            Text(
                text = "${order.itemCount} productos",
                style = MaterialTheme.typography.bodyMedium,
                color = bodyColor // Texto secundario (gris claro)
            )
        }
    }
}

fun Date.toFormattedString(): String {
    val formatter = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("es", "ES"))
    return formatter.format(this)
}

// Para que las Previews usen los nuevos colores
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Historial Lleno")
@Composable
fun OrderHistoryScreenPreview() {
    // Paleta de Preview (duplicada para que la preview compile sin el VM)
    val redPrimary = Color(0xFFD32F2F)
    val redDark = Color(0xFFB71C1C)
    val darkGray = Color(0xFF121212)
    val cardGray = Color(0xFF1F1F1F)
    val onDark = Color(0xFFFFFFFF)
    val secondaryText = Color(0xFFBDBDBD)

    val previewOrders = listOf(
        Order(id = "ABC-123", date = Date(), total = 189990.0, itemCount = 1),
        Order(id = "DEF-456", date = Date(), total = 45980.0, itemCount = 2)
    )
    val previewState = OrderHistoryUiState(orders = previewOrders, isLoading = false)


    Scaffold(
        topBar = { TopAppBar(
            title = { Text("Mi Historial de Compras", color = onDark) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = redDark, titleContentColor = onDark)
        ) },
        containerColor = darkGray
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(previewState.orders) { order ->
                // Usar la función modificada con los colores de la paleta
                OrderRow(
                    order = order,
                    cardColor = cardGray,
                    titleColor = onDark,
                    bodyColor = secondaryText,
                    totalColor = redPrimary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Historial Vacío")
@Composable
fun OrderHistoryScreenEmptyPreview() {
    // Paleta de Preview (duplicada para que la preview compile sin el VM)
    val redDark = Color(0xFFB71C1C)
    val darkGray = Color(0xFF121212)
    val onDark = Color(0xFFFFFFFF)

    val previewState = OrderHistoryUiState(orders = emptyList(), isLoading = false)

    Scaffold(
        topBar = { TopAppBar(
            title = { Text("Mi Historial de Compras", color = onDark) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = redDark, titleContentColor = onDark)
        ) },
        containerColor = darkGray
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("Aún no has realizado ninguna compra.", color = onDark)
        }
    }
}