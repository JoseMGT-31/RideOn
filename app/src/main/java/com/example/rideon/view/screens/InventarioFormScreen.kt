package com.example.rideon.view.screens

import androidx.compose.foundation.background
import com.example.rideon.model.ProductoUi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rideon.viewmodel.InventarioViewModel
import com.example.rideon.ui.theme.RideOnTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventarioFormScreen(
    vm: InventarioViewModel,
    defaultImageRes: Int,
    onSaved: (ProductoUi) -> Unit,
    onBack: () -> Unit
) {
    // Paleta
    val redPrimary = Color(0xFFD32F2F)
    val redDark = Color(0xFFB71C1C)
    val darkGray = Color(0xFF121212)
    val cardGray = Color(0xFF1F1F1F)
    val onDark = Color(0xFFFFFFFF)


    val form = vm.form

    @Composable
    fun Field(
        label: String, value: String, key: String,
        kb: KeyboardOptions = KeyboardOptions.Default
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { vm.onChange(key, it) },
            label = { Text(label, color = onDark) },
            isError = form.errors[key] != null,
            supportingText = { form.errors[key]?.let { Text(it, color = onDark.copy(alpha = 0.9f)) } },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = kb,

            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = onDark,
                unfocusedTextColor = onDark,
                cursorColor = redPrimary,
                focusedBorderColor = redPrimary,
                unfocusedBorderColor = onDark.copy(alpha = 0.3f),
                unfocusedContainerColor = cardGray,
                focusedContainerColor = cardGray,
                focusedLabelColor = redPrimary,
                unfocusedLabelColor = onDark,
                unfocusedLeadingIconColor = onDark,
                focusedLeadingIconColor = redPrimary,
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (form.id == null) "Nuevo producto" else "Editar producto", color = onDark) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = onDark) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = redDark, titleContentColor = onDark, navigationIconContentColor = onDark)
            )
        },
        // Añadir el fondo oscuro al contenido del Scaffold
        containerColor = darkGray
    ) { p ->
        Column(
            Modifier.padding(p).padding(16.dp).fillMaxSize(), // Remover el .background(darkGray) de aquí y añadir .fillMaxSize()
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
                label = { Text("Descripción (mín. 30 caracteres)", color = onDark) },
                isError = form.errors["description"] != null,
                supportingText = { form.errors["description"]?.let { Text(it, color = onDark.copy(alpha = 0.9f)) } },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,

                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = onDark,
                    unfocusedTextColor = onDark,
                    cursorColor = redPrimary,
                    focusedBorderColor = redPrimary,
                    unfocusedBorderColor = onDark.copy(alpha = 0.3f),
                    unfocusedContainerColor = cardGray,
                    focusedContainerColor = cardGray,
                    focusedLabelColor = redPrimary,
                    unfocusedLabelColor = onDark,
                )
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = form.abs, onCheckedChange = { vm.setAbs(it) }, colors = CheckboxDefaults.colors(checkedColor = redPrimary))
                Text("ABS", color = onDark)
            }

            Button(
                onClick = { vm.submit(defaultImageRes, onSaved) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = redPrimary, contentColor = onDark)
            ) { Text("Guardar") }
        }
    }
}
