package com.example.rideon.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rideon.R
import java.text.NumberFormat
import java.util.Locale

// --------------------
// DATOS TEMPORALES
// --------------------
data class ProductoUi(
    val id: Int,
    val brand: String,
    val model: String,
    val year: Int,
    val priceClp: Int,
    val stock: Int,
    val imageRes: Int,
    val description: String,
    val engine: String,
    val powerHp: Int,
    val abs: Boolean
)

// --------------------
// STEP CANTIDAD
// --------------------
@Composable
private fun QuantityStepper(
    value: Int,
    onValueChange: (Int) -> Unit,
    enabled: Boolean = true
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedButton(
            onClick = { onValueChange((value - 1).coerceAtLeast(1)) },
            enabled = enabled && value > 1
        ) { Text("-") }

        Spacer(Modifier.width(12.dp))
        Text("$value", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.width(12.dp))

        OutlinedButton(
            onClick = { onValueChange(value + 1) },
            enabled = enabled
        ) { Text("+") }
    }
}

// --------------------
// PANTALLA PRINCIPAL
// --------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoScreen(
    producto: ProductoUi,
    onBack: () -> Unit,
    onAddToCart: (Int) -> Unit
) {
    val currency = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")) }
    var qty by remember { mutableStateOf(1) }
    val canBuy = producto.stock > 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${producto.brand} ${producto.model}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { p ->
        Column(
            modifier = Modifier
                .padding(p)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(id = producto.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                text = "${producto.brand} ${producto.model} ${producto.year}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = currency.format(producto.priceClp),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${producto.engine} • ${producto.powerHp} hp • ABS ${if (producto.abs) "Sí" else "No"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = producto.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Divider()
            Text("Stock disponible: ${producto.stock}")

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                QuantityStepper(
                    value = qty,
                    onValueChange = { qty = it.coerceAtMost(maxOf(1, producto.stock)) },
                    enabled = canBuy
                )
                Button(
                    onClick = { onAddToCart(qty) },
                    enabled = canBuy,
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    Text(if (canBuy) "Agregar al carrito" else "Sin stock")
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

// --------------------
// PREVIEW
// --------------------
@Preview(showBackground = true)
@Composable
private fun ProductoPreview() {
    val fake = ProductoUi(
        id = 1,
        brand = "Kawasaki",
        model = "Ninja 650",
        year = 2022,
        priceClp = 7_990_000,
        stock = 4,
        imageRes = android.R.drawable.ic_menu_report_image,
        description = "Carenada ágil y versátil, ideal para uso diario y paseos. Chasis ligero y postura cómoda.",
        engine = "649 cc",
        powerHp = 68,
        abs = true
    )
    ProductoScreen(
        producto = fake,
        onBack = {},
        onAddToCart = {}
    )
}
