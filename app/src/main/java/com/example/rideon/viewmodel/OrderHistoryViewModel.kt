package com.example.rideon.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rideon.data.OrderRepository
import com.example.rideon.model.Order
import kotlinx.coroutines.flow.*

data class OrderHistoryUiState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = true
)

class OrderHistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val orderRepository = OrderRepository(application)


    val uiState: StateFlow<OrderHistoryUiState> = orderRepository.ordersFlow
            .map { orderList ->
                // Ordenamos los pedidos del más reciente al más antiguo
                OrderHistoryUiState(orders = orderList.sortedByDescending { it.date }, isLoading = false)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = OrderHistoryUiState(isLoading = true)
            )
}