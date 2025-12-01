package com.example.rideon.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.*
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.example.rideon.model.ProductoUi



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoScreen(
    producto: ProductoUi,
    onBack: () -> Unit,
    onAddToCart: (Int) -> Unit
) {
    // Paleta
    val redPrimary = Color(0xFFD32F2F)
    val redDark = Color(0xFFB71C1C)
    val darkGray = Color(0xFF121212)
    val cardGray = Color(0xFF1F1F1F)
    val onDark = Color(0xFFFFFFFF)

    val currency = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")) }
    var qty by remember { mutableStateOf(1) }
    val canBuy = producto.stock > 0

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "${producto.brand} ${producto.model}",
                        fontWeight = FontWeight.Bold,
                        color = onDark
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = onDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = redDark, titleContentColor = onDark, navigationIconContentColor = onDark)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(darkGray)
        ) {
            // Imagen principal con degradado inferior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                Image(
                    painter = painterResource(id = producto.imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f)),
                                startY = 150f
                            )
                        )
                )
                Text(
                    text = "${producto.brand} ${producto.model}",
                    color = onDark,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                )
            }

            // Contenido inferior
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Precio destacado
                Text(
                    text = currency.format(producto.priceClp),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = onDark
                )

                // Año y motor
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "${producto.year} • ${producto.engine}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                        ),
                        color = onDark.copy(alpha = 0.9f)
                    )
                    Text(
                        "${producto.powerHp} HP",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = onDark
                    )
                }

                Divider(thickness = 1.dp, color = cardGray)

                // Descripción
                Text(
                    text = producto.description,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 20.sp,
                    color = onDark.copy(alpha = 0.95f)
                )

                Spacer(Modifier.height(8.dp))

                // Stock
                val stockColor = if (producto.stock > 0) Color(0xFF2E7D32) else Color(0xFFC62828)
                Text(
                    text = if (producto.stock > 0)
                        "Stock disponible: ${producto.stock}"
                    else
                        "Sin stock disponible",
                    color = stockColor,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                )

                Spacer(Modifier.height(8.dp))

                // Cantidad y botón
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    QuantityStepper(
                        value = qty,
                        onValueChange = { qty = it.coerceAtMost(producto.stock) },
                        enabled = canBuy,
                        redPrimary = redPrimary,
                        onDark = onDark
                    )

                    Button(
                        onClick = {
                            onAddToCart(qty)
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Agregaste $qty ${producto.model} al carrito",
                                    withDismissAction = true
                                )
                            }
                        },
                        enabled = canBuy,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .height(48.dp)
                            .fillMaxWidth(0.5f),
                        colors = ButtonDefaults.buttonColors(containerColor = redPrimary, contentColor = onDark)
                    ) {
                        Text(if (canBuy) "Agregar al carrito" else "Sin stock")
                    }
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

// Reutilizamos el stepper, más pequeño y limpio
@Composable
private fun QuantityStepper(
    value: Int,
    onValueChange: (Int) -> Unit,
    enabled: Boolean,
    redPrimary: Color = Color(0xFFD32F2F),
    onDark: Color = Color.White
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedButton(
            onClick = { onValueChange((value - 1).coerceAtLeast(1)) },
            enabled = enabled && value > 1,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = onDark)
        ) { Text("-", color = onDark) }

        Spacer(Modifier.width(8.dp))
        Text(
            "$value",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center,
            modifier = Modifier.width(32.dp),
            color = onDark
        )
        Spacer(Modifier.width(8.dp))

        OutlinedButton(
            onClick = { onValueChange(value + 1) },
            enabled = enabled,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = onDark)
        ) { Text("+", color = onDark) }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewProductoScreen() {
    ProductoScreen(
        producto = ProductoUi(
            id = 9,
            brand = "Kawasaki",
            model = "Ninja H2R",
            year = 2023,
            priceClp = 59_990_000,
            stock = 1,
            imageRes = android.R.drawable.ic_menu_report_image,
            description = "Superbike exclusiva con motor sobrealimentado de 998 cc que entrega 310 hp. " +
                    "Diseñada para circuito, con aerodinámica de fibra de carbono y rendimiento extremo.",
            engine = "998 cc sobrealimentado",
            powerHp = 310,
            abs = true
        ),
        onBack = {},
        onAddToCart = {}
    )
}
