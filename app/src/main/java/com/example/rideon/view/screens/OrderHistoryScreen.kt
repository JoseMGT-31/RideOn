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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(viewModel: OrderHistoryViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mi Historial de Compras") }) }
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.orders.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Aún no has realizado ninguna compra.")
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.orders) { order ->
                        OrderRow(order = order)
                    }
                }
            }
        }
    }
}

@Composable
fun OrderRow(order: Order) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Pedido #${order.id}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Fecha: ${order.date.toFormattedString()}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Total: $${order.total.toInt()}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "${order.itemCount} productos",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

fun Date.toFormattedString(): String {
    val formatter = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("es", "ES"))
    return formatter.format(this)
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Historial Lleno")
@Composable
fun OrderHistoryScreenPreview() {
    val previewOrders = listOf(
        Order(id = "ABC-123", date = Date(), total = 189990.0, itemCount = 1),
        Order(id = "DEF-456", date = Date(), total = 45980.0, itemCount = 2)
    )
    val previewState = OrderHistoryUiState(orders = previewOrders, isLoading = false)


    Scaffold(
        topBar = { TopAppBar(title = { Text("Mi Historial de Compras") }) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(previewState.orders) { order ->
                OrderRow(order = order)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Historial Vacío")
@Composable
fun OrderHistoryScreenEmptyPreview() {
    val previewState = OrderHistoryUiState(orders = emptyList(), isLoading = false)

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mi Historial de Compras") }) }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("Aún no has realizado ninguna compra.")
        }
    }
}