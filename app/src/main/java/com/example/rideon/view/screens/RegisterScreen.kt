package com.example.rideon.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rideon.R
import com.example.rideon.viewmodel.Auth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    vm: Auth = viewModel(),
    onRegistered: () -> Unit = {},
    onGoLogin: () -> Unit = {}
) {
    val state by vm.registerState.collectAsState()
    val errors by vm.registerErrors.collectAsState()

    val snackHost = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackHost) },
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0)
    ) { inner ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(inner)
        ) {
            // 1) Fondo
            Image(
                painter = painterResource(id = R.drawable.bg_home),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // 2) Overlay oscuro
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f))
            )

            // 3) Logo centrado (un poco más arriba)
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = (-130).dp)
                    .size(350.dp)
                    .graphicsLayer { }
            )

            // 4) Formulario abajo
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 24.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Nombre
                OutlinedTextField(
                    value = state.name,
                    onValueChange = vm::onRegisterNameChange,
                    label = { Text("Nombre", color = Color.White) },
                    leadingIcon = { Icon(Icons.Filled.Badge, contentDescription = null) },
                    singleLine = true,
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(40.dp),
                )

                // Email
                OutlinedTextField(
                    value = state.email,
                    onValueChange = vm::onRegisterEmailChange,
                    label = { Text("Email", color = Color.White) },
                    placeholder = { Text("tu@correo.com", color = Color.White) },
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                    singleLine = true,
                    textStyle = TextStyle(color = Color.White),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    isError = errors.email != null,
                    supportingText = { errors.email?.let { Text(it) } },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    shape = RoundedCornerShape(40.dp),
                )

                // Contraseña
                var showPass by remember { mutableStateOf(false) }
                OutlinedTextField(
                    value = state.password,
                    onValueChange = vm::onRegisterPasswordChange,
                    label = { Text("Contraseña", color = Color.White) },
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { showPass = !showPass }) {
                            Icon(
                                imageVector = if (showPass) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = null
                            )
                        }
                    },
                    singleLine = true,
                    textStyle = TextStyle(color = Color.White),
                    visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                    isError = errors.password != null,
                    supportingText = { errors.password?.let { Text(it) } },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    shape = RoundedCornerShape(40.dp),
                )

                // Confirmar contraseña
                var showConfirm by remember { mutableStateOf(false) }
                OutlinedTextField(
                    value = state.confirm,
                    onValueChange = vm::onRegisterConfirmChange,
                    label = { Text("Repite la contraseña", color = Color.White) },
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { showConfirm = !showConfirm }) {
                            Icon(
                                imageVector = if (showConfirm) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = null
                            )
                        }
                    },
                    singleLine = true,
                    textStyle = TextStyle(color = Color.White),
                    visualTransformation = if (showConfirm) VisualTransformation.None else PasswordVisualTransformation(),
                    isError = errors.confirm != null,
                    supportingText = { errors.confirm?.let { Text(it) } },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    shape = RoundedCornerShape(40.dp),
                )

                // Botón crear cuenta
                Button(
                    onClick = {
                        vm.submitRegister(
                            onSuccess = onRegistered,
                            onFailure = { msg -> scope.launch { snackHost.showSnackbar(msg) } }
                        )
                    },
                    enabled = state.submitEnabled && !state.isSubmitting,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(top = 16.dp)
                ) {
                    if (state.isSubmitting) {
                        CircularProgressIndicator(strokeWidth = 2.dp, color = Color.White)
                    } else {
                        Text("Crear cuenta")
                    }
                }

                TextButton(
                    onClick = onGoLogin,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("¿Ya tienes cuenta? Inicia sesión", color = Color.White)
                }
            }
        }
    }
}