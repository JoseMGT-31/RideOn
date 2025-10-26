package com.example.rideon.view.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rideon.model.CartItem
import com.example.rideon.viewmodel.CartViewModel
import androidx.compose.ui.tooling.preview.Preview
import com.example.rideon.model.CartUiState

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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = "Carrito vacío",
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Tu carrito está vacío",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "¡Añade productos para verlos aquí!",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // Sombra sutil
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            val resId = item.imageUrl.toIntOrNull()
            if (resId != null) {
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = item.name,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Fallback si no se puede parsear el id
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {}
            }


            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$${item.price.toInt()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Cantidad: ${item.quantity}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Botón para eliminar
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar producto",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}


@Composable
fun CartBottomBar(total: Double, onConfirm: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Total", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = "$${total.toInt()}",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            Button(onClick = onConfirm,
            modifier = Modifier,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = MaterialTheme.colorScheme.onPrimary  // Color del texto dentro del botón
                )
            ) {
                Text("Confirmar Pedido")
            }
        }
    }
}


@Preview(name = "Carrito Con Items", showBackground = true)
@Composable

fun CartScreenPreview() {
    CartScreen()
}
