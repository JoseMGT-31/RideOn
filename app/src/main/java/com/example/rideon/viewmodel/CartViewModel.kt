package com.example.rideon.viewmodel

import androidx.lifecycle.ViewModel
import com.example.rideon.model.CartItem
import com.example.rideon.model.CartUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CartViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    fun addItem(product: CartItem) {
        _uiState.update { currentState ->

            val existingItem = currentState.items.find { it.id == product.id }
            val updatedItems = if (existingItem != null) {
                currentState.items.map {
                    if (it.id == product.id) it.copy(quantity = it.quantity + 1) else it
                }
            } else {
                currentState.items + product
            }
            currentState.copy(
                items = updatedItems,
                total = calculateTotal(updatedItems)
            )
        }
    }

    fun removeItem(productId: String) {
        _uiState.update { currentState ->
            val updatedItems = currentState.items.filterNot { it.id == productId }
            currentState.copy(
                items = updatedItems,
                total = calculateTotal(updatedItems)
            )
        }
    }

    private fun calculateTotal(items: List<CartItem>): Double {
        return items.sumOf { it.price * it.quantity }
    }
}