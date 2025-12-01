package com.example.rideon.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rideon.data.CatalogoStore
import com.example.rideon.model.ProductoUi
import com.example.rideon.viewmodel.InventarioViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductosScreen(
    navController: NavController,
    inventarioVm: InventarioViewModel = viewModel()
) {
    val redDark = Color(0xFFB71C1C)
    val darkGray = Color(0xFF121212)
    val onDark = Color(0xFFFFFFFF)

    val productos = CatalogoStore.productos

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var toDeleteId by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel Admin - Productos", color = onDark) },
                actions = {
                    IconButton(onClick = {
                        // limpiar form y navegar a formulario nuevo
                        inventarioVm.clearForm()
                        navController.navigate("inventarioForm")
                    }) {
                        Icon(Icons.Filled.Add, contentDescription = "Agregar", tint = onDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = redDark, titleContentColor = onDark)
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(darkGray)
            .padding(12.dp)) {

            if (productos.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay productos", color = onDark)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(productos) { producto ->
                        ProductoAdminRow(producto = producto,
                            onEdit = {
                                inventarioVm.loadForEdit(producto)
                                navController.navigate("inventarioForm")
                            },
                            onDelete = {
                                // marcar para confirmar
                                toDeleteId = producto.id
                            }
                        )
                    }
                }
            }
        }

        // Dialogo de confirmación de eliminación
        if (toDeleteId != null) {
            AlertDialog(
                onDismissRequest = { toDeleteId = null },
                title = { Text("Eliminar producto") },
                text = { Text("¿Estás seguro que deseas eliminar este producto?") },
                confirmButton = {
                    TextButton(onClick = {
                        val id = toDeleteId!!
                        CatalogoStore.delete(id)
                        toDeleteId = null
                        scope.launch { snackbarHostState.showSnackbar("Producto eliminado") }
                    }) { Text("Eliminar") }
                },
                dismissButton = {
                    TextButton(onClick = { toDeleteId = null }) { Text("Cancelar") }
                }
            )
        }
    }
}

@Composable
fun ProductoAdminRow(
    producto: ProductoUi,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val cardGray = Color(0xFF1F1F1F)
    val onDark = Color.White
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp),
        colors = CardDefaults.cardColors(containerColor = cardGray),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(Modifier.fillMaxSize().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("${producto.brand} ${producto.model}", color = onDark, style = MaterialTheme.typography.titleMedium)
                Text("$ ${producto.priceClp}", color = onDark.copy(alpha = 0.9f))
            }
            IconButton(onClick = onEdit) { Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = onDark) }
            IconButton(onClick = onDelete) { Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = Color.Red) }
        }
    }
}
