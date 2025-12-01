package com.example.rideon.view.screens

import com.example.rideon.model.ProductoUi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    val nf = NumberFormat.getCurrencyInstance(Locale.Builder().setLanguage("es").setRegion("CL").build())

    // Paleta
    val redPrimary = Color(0xFFD32F2F)
    val redDark = Color(0xFFB71C1C)
    val darkGray = Color(0xFF121212)
    val cardGray = Color(0xFF1F1F1F)
    val onDark = Color(0xFFFFFFFF)
    val secondaryText = Color(0xFFBDBDBD) // Gris claro para contraste

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("rideON • Catálogo", color = onDark) },
                actions = {
                    IconButton(onClick = onOpenCart) {
                        Icon(
                            imageVector = Icons.Filled.ShoppingCart,
                            contentDescription = "Abrir carrito",
                            tint = onDark
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = redDark,
                    titleContentColor = onDark,
                    actionIconContentColor = onDark
                )
            )
        },

        containerColor = darkGray
    ) { p ->
        LazyColumn(
            contentPadding = p,
            modifier = Modifier.fillMaxSize()
        ) {
            items(productos) { pdt ->
                ListItem(
                    headlineContent = {
                        Text(
                            "${pdt.brand} ${pdt.model}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = onDark // Texto principal blanco
                        )
                    },
                    supportingContent = {
                        Text(
                            "${pdt.year} • ${nf.format(pdt.priceClp)}",
                            color = secondaryText // Texto secundario gris claro para legibilidad
                        )
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
                        AssistChip(
                            onClick = { onOpen(pdt) },
                            label = { Text("Ver") },
                            colors = AssistChipDefaults.assistChipColors(containerColor = redPrimary, labelColor = onDark)
                        )
                    },

                    colors = ListItemDefaults.colors(containerColor = cardGray),
                    modifier = Modifier
                        .clickable { onOpen(pdt) }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )

                HorizontalDivider(color = cardGray)
            }
        }
    }
}