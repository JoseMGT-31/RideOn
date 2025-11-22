package com.example.rideon.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rideon.data.OrderRepository
import com.example.rideon.model.CartItem
import com.example.rideon.model.CartUiState
import com.example.rideon.model.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()


    private val orderRepository = OrderRepository(application)

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
            currentState.copy(items = updatedItems, total = calculateTotal(updatedItems))
        }
    }

    fun removeItem(productId: String) {
        _uiState.update { currentState ->
            val updatedItems = currentState.items.filterNot { it.id == productId }
            currentState.copy(items = updatedItems, total = calculateTotal(updatedItems))
        }
    }

    fun confirmOrder(onSuccess: () -> Unit) {
        val currentItems = _uiState.value.items
        if (currentItems.isEmpty()) return

        viewModelScope.launch {

            val newOrder = Order(
                id = UUID.randomUUID().toString().take(8).uppercase(),
                date = Date(),
                total = _uiState.value.total,
                itemCount = currentItems.sumOf { it.quantity }
            )

            orderRepository.addOrder(newOrder)
            _uiState.update { CartUiState() }

            onSuccess()
        }
    }

    private fun calculateTotal(items: List<CartItem>): Double {
        return items.sumOf { it.price * it.quantity }
    }
}