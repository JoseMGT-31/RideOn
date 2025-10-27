package com.example.rideon.model

import androidx.annotation.DrawableRes

data class ProductoUi(
    val id: Int,
    val brand: String,
    val model: String,
    val year: Int,
    val priceClp: Int,
    val stock: Int,
    @DrawableRes val imageRes: Int,
    val description: String,
    val engine: String,
    val powerHp: Int,
    val abs: Boolean
)
