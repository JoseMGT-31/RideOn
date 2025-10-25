package com.example.rideon.model

data class CartItem(

    //datos que se veran en solo un item del carrito
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String,
    var quantity: Int = 1
)
data class CartUiState(

    val items: List<CartItem> = emptyList<CartItem>(),
    val total: Double = 0.0
)
