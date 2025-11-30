package com.example.rideon.view.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rideon.R
import com.example.rideon.viewmodel.Auth
import kotlinx.coroutines.launch
import androidx.compose.ui.text.TextStyle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    vm: Auth = viewModel(),
    onLoggedIn: () -> Unit = {},
    onGoRegister: () -> Unit = {}
) {
    val state by vm.loginState.collectAsState()
    val errors by vm.loginErrors.collectAsState()

    val snackHost = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // ----------- UI -----------
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
                    .graphicsLayer { /* espacio para animaciones si luego quieres */ }
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
                // Email
                OutlinedTextField(
                    value = state.email,
                    onValueChange = vm::onLoginEmailChange,
                    label = { Text("Email", color = Color.White) },
                    placeholder = { Text("tu@correo.com", color = Color.White) },
                    textStyle = TextStyle(color = Color.White),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                    isError = errors.email != null,
                    supportingText = { errors.email?.let { Text(it) } },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(40.dp),
                )

                // Password
                var showPassword by remember { mutableStateOf(false) }
                OutlinedTextField(
                    value = state.password,
                    onValueChange = vm::onLoginPasswordChange,
                    label = { Text("Contraseña", color = Color.White) },
                    textStyle = TextStyle(color = Color.White),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = null
                            )
                        }
                    },
                    isError = errors.password != null,
                    supportingText = { errors.password?.let { Text(it) } },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    shape = RoundedCornerShape(40.dp),
                )

                // Botón Entrar
                Button(
                    onClick = {
                        vm.submitLogin(
                            onSuccess = onLoggedIn,
                            onFailure = { msg ->
                                scope.launch { snackHost.showSnackbar(message = msg) }
                            }
                        )
                    },
                    enabled = state.submitEnabled && !state.isSubmitting,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(top = 16.dp)
                ) {
                    if (state.isSubmitting) {
                        CircularProgressIndicator(strokeWidth = 2.dp, color = Color.White)
                    } else {
                        Text("Iniciar sesión")
                    }
                }

                // Link a registro
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "¿No tienes cuenta? ", color = Color.White, fontSize = 14.sp)
                    Text(
                        text = "Regístrate",
                        color = Color.Cyan,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { onGoRegister() }
                    )
                }
            }
        }
    }
}