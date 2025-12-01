package com.example.rideon.data

import com.example.rideon.model.ProductoUi
import androidx.compose.runtime.mutableStateListOf

object CatalogoStore {
    // Estado observable por Compose
    val productos = mutableStateListOf<ProductoUi>().apply {
        addAll(Catalogo.productos)
    }

    fun nextId(): Int = (productos.maxOfOrNull { it.id } ?: 0) + 1

    fun save(producto: ProductoUi) {
        val idx = productos.indexOfFirst { it.id == producto.id }
        if (idx >= 0) {
            productos[idx] = producto // editar
        } else {
            productos.add(producto)   // agregar
        }
    }

    fun byId(id: Int): ProductoUi? = productos.firstOrNull { it.id == id }

    // Eliminar producto por id
    fun delete(id: Int) {
        val idx = productos.indexOfFirst { it.id == id }
        if (idx >= 0) productos.removeAt(idx)
    }
}
