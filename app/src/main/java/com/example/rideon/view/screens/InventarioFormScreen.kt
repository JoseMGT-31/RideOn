package com.example.rideon.view.screens

import com.example.rideon.model.ProductoUi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rideon.viewmodel.InventarioViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventarioFormScreen(
    vm: InventarioViewModel,
    defaultImageRes: Int,
    onSaved: (ProductoUi) -> Unit,
    onBack: () -> Unit
) {
    // vm.form es un var backed by mutableStateOf -> leerlo así recompone correctamente
    val form = vm.form

    @Composable
    fun Field(
        label: String, value: String, key: String,
        kb: KeyboardOptions = KeyboardOptions.Default
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { vm.onChange(key, it) },
            label = { Text(label) },
            isError = form.errors[key] != null,
            supportingText = { form.errors[key]?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = kb
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (form.id == null) "Nuevo producto" else "Editar producto") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver") }
                }
            )
        }
    ) { p ->
        Column(
            Modifier.padding(p).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Field("Marca", form.brand, "brand")
            Field("Modelo", form.model, "model")
            Field("Año (2020–2025)", form.year, "year",
                KeyboardOptions(keyboardType = KeyboardType.Number))
            Field("Precio CLP", form.price, "price",
                KeyboardOptions(keyboardType = KeyboardType.Number))
            Field("Stock", form.stock, "stock",
                KeyboardOptions(keyboardType = KeyboardType.Number))
            Field("Cilindrada (ej. 649 cc)", form.engine, "engine")
            Field("Potencia (hp)", form.power, "power",
                KeyboardOptions(keyboardType = KeyboardType.Number))

            OutlinedTextField(
                value = form.description,
                onValueChange = { vm.onChange("description", it) },
                label = { Text("Descripción (mín. 30 caracteres)") },
                isError = form.errors["description"] != null,
                supportingText = { form.errors["description"]?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = form.abs, onCheckedChange = { vm.setAbs(it) })
                Text("ABS")
            }

            Button(
                onClick = { vm.submit(defaultImageRes, onSaved) },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Guardar") }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InventarioFormScreenPreview() {
    // VM “dummy”
    val vm = com.example.rideon.viewmodel.InventarioViewModel()

    // Producto de ejemplo para prellenar (modo editar) — opcional
    val sample = com.example.rideon.model.ProductoUi(
        id = 99,
        brand = "Yamaha",
        model = "MT-07",
        year = 2022,
        priceClp = 7_890_000,
        stock = 5,
        imageRes = android.R.drawable.ic_menu_report_image, // reemplaza por un drawable tuyo si quieres
        description = "Hyper naked compacta, torque amigable y bajo peso. Excelente para ciudad y paseos.",
        engine = "689 cc",
        powerHp = 73,
        abs = true
    )
    // Pre-cargar el formulario en modo “editar”
    vm.loadForEdit(sample)

    // Envuelve con tu tema si lo usas
    com.example.rideon.ui.theme.RideOnTheme {
        InventarioFormScreen(
            vm = vm,
            defaultImageRes = sample.imageRes, // usado si estuvieras en modo “nuevo”
            onSaved = { /* no-op en preview */ },
            onBack = { /* no-op en preview */ }
        )
    }
}
