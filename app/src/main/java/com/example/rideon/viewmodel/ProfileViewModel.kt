package com.example.rideon.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel : ViewModel() {
    // Estado que guarda la URI de la imagen, inicia siendo null osea sin foto
    private val _profileImageUri = MutableStateFlow<Uri?>(null)
    val profileImageUri: StateFlow<Uri?> = _profileImageUri.asStateFlow()

    // actualizar la imagen cuando sacamos una foto o elegimos de galería
    fun updateProfileImage(uri: Uri?) {
        _profileImageUri.value = uri
    }
}