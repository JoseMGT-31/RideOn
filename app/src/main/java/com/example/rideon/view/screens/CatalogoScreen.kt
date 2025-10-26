package com.example.rideon.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    productos: List<ProductoUi>,
    onOpen: (ProductoUi) -> Unit,
    onOpenCart: () -> Unit
) {
    val nf = NumberFormat.getCurrencyInstance(Locale("es","CL"))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("rideON • Catálogo") },
                actions = {
                    IconButton(onClick = onOpenCart) {
                        Icon(
                            imageVector = Icons.Filled.ShoppingCart,
                            contentDescription = "Abrir carrito"
                        )
                    }
                }
            )
        }
    ) { p ->
        LazyColumn(
            contentPadding = p,
            modifier = Modifier.fillMaxSize()
        ) {
            items(productos) { pdt ->
                ListItem(
                    headlineContent = {
                        Text("${pdt.brand} ${pdt.model}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
                    },
                    supportingContent = {
                        Text("${pdt.year} • ${nf.format(pdt.priceClp)}")
                    },
                    leadingContent = {
                        Image(
                            painter = painterResource(pdt.imageRes),
                            contentDescription = null,
                            modifier = Modifier.size(72.dp),
                            contentScale = ContentScale.Crop
                        )
                    },
                    trailingContent = {
                        AssistChip(onClick = { onOpen(pdt) }, label = { Text("Ver") })
                    },
                    modifier = Modifier
                        .clickable { onOpen(pdt) }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Divider()
            }
        }
    }
}
