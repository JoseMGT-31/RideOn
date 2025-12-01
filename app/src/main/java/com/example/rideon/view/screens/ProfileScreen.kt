package com.example.rideon.view.screens

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.rideon.viewmodel.ProfileViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {

    // Paleta
    val redPrimary = Color(0xFFD32F2F)
    val redDark = Color(0xFFB71C1C)
    val darkGray = Color(0xFF121212)
    val cardGray = Color(0xFF1F1F1F)
    val onDark = Color(0xFFFFFFFF)

    val imageUri by viewModel.profileImageUri.collectAsState()
    val context = LocalContext.current


    var tempUri by remember { mutableStateOf<Uri?>(null) }


    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempUri != null) {
            viewModel.updateProfileImage(tempUri)
        }
    }


    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.updateProfileImage(uri)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Si nos dan permiso, creamos el archivo temporal y abrimos la cámara
            val uri = createTempPictureUri(context)
            tempUri = uri
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Debes dar permiso de cámara", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", color = onDark) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = redDark, titleContentColor = onDark)
            )
        },
        // ⭐️ CAMBIO CLAVE 1: Establecer el color del contenedor del Scaffold para eliminar el borde blanco
        containerColor = darkGray
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                // ⭐️ CAMBIO CLAVE 2: Aplicar solo padding horizontal. Se eliminó el background(darkGray) y el padding(16.dp) general.
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ⭐️ CAMBIO CLAVE 3: Aplicar padding superior para espaciar del TopAppBar
            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(top = 16.dp)) {
                if (imageUri != null) {

                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(250.dp)
                            .clip(CircleShape)
                            .background(cardGray),
                        contentScale = ContentScale.Crop
                    )
                } else {

                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Sin foto",
                        modifier = Modifier.size(200.dp),
                        tint = Color.LightGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- BOTONES ---
            Button(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = redPrimary, contentColor = onDark)
            ) {
                Text("Seleccionar de Galería")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { permissionLauncher.launch(android.Manifest.permission.CAMERA) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = onDark),
                // ⭐️ CAMBIO CLAVE 4: Asegurar que el borde sea blanco y visible
                border = BorderStroke(1.dp, onDark)
            ) {
                Text("Tomar Foto con Cámara")
            }
        }
    }
}


fun createTempPictureUri(context: Context): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val file = File.createTempFile(imageFileName, ".jpg", context.externalCacheDir)

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider", // Esto conecta con el Manifest
        file
    )
}