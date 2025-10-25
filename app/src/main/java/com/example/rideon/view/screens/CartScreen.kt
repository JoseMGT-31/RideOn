package com.example.rideon.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rideon.model.CartItem
import com.example.rideon.viewmodel.CartViewModel
import androidx.compose.ui.tooling.preview.Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(cartViewModel: CartViewModel = viewModel()) {

    val uiState by cartViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mi Carrito") })
        },
        bottomBar = {

            CartBottomBar(total = uiState.total, onConfirm = { /* Lógica para confirmar pedido */ })
        }
    ) { innerPadding ->
        if (uiState.items.isEmpty()) {

            Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                Text("Tu carrito está vacío")
            }
        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.items) { item ->
                    CartItemRow(item = item, onRemove = { cartViewModel.removeItem(item.id) })
                }
            }
        }
    }
}


@Composable
fun CartItemRow(item: CartItem, onRemove: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(8.dp)) {

            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, style = MaterialTheme.typography.titleMedium)
                Text("Precio: $${item.price}")
                Text("Cantidad: ${item.quantity}")
            }
            IconButton(onClick = onRemove) {

            }
        }
    }
}


@Composable
fun CartBottomBar(total: Double, onConfirm: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Total: $${String.format("%.2f", total)}", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar Pedido")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CartScreenPreview() {
    CartScreen()
}