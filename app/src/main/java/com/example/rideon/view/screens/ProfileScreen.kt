package com.example.rideon.view.screens

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
        topBar = { TopAppBar(title = { Text("Mi Perfil") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(contentAlignment = Alignment.Center) {
                if (imageUri != null) {

                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape)
                            .background(Color.Gray),
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
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Seleccionar de Galería")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { permissionLauncher.launch(android.Manifest.permission.CAMERA) }, // Pide permiso y abre Cámara
                modifier = Modifier.fillMaxWidth()
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