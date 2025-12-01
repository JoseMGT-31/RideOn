package com.example.rideon.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.rideon.data.CatalogoStore
import com.example.rideon.model.ProductoUi

data class InventarioForm(
    val id: Int? = null,
    val brand: String = "",
    val model: String = "",
    val year: String = "",
    val price: String = "",
    val stock: String = "",
    val engine: String = "",
    val power: String = "",
    val description: String = "",
    val abs: Boolean = true,
    val imageRes: Int? = null,
    val errors: Map<String, String> = emptyMap()
)

class InventarioViewModel : ViewModel() {

    var form by mutableStateOf(InventarioForm())
        private set

    /** Cargar datos para editar (si corresponde) */
    fun loadForEdit(producto: ProductoUi) {
        form = InventarioForm(
            id = producto.id,
            brand = producto.brand,
            model = producto.model,
            year = producto.year.toString(),
            price = producto.priceClp.toString(),
            stock = producto.stock.toString(),
            engine = producto.engine,
            power = producto.powerHp.toString(),
            description = producto.description,
            abs = producto.abs,
            imageRes = producto.imageRes
        )
    }

    // Nuevo helper para inicio de formulario vacío
    fun clearForm() { form = InventarioForm() }

    fun onChange(field: String, value: String) {
        form = when (field) {
            "brand" -> form.copy(brand = value, errors = form.errors - field)
            "model" -> form.copy(model = value, errors = form.errors - field)
            "year" -> form.copy(year = value, errors = form.errors - field)
            "price" -> form.copy(price = value, errors = form.errors - field)
            "stock" -> form.copy(stock = value, errors = form.errors - field)
            "engine" -> form.copy(engine = value, errors = form.errors - field)
            "power" -> form.copy(power = value, errors = form.errors - field)
            "description" -> form.copy(description = value, errors = form.errors - field)
            else -> form
        }
    }

    fun setAbs(v: Boolean) { form = form.copy(abs = v) }


    private fun validate(): Boolean {
        val f = form
        val errs = buildMap {
            if (f.brand.isBlank()) put("brand", "Requerido")
            if (f.model.isBlank()) put("model", "Requerido")
            val y = f.year.toIntOrNull()
            if (y == null || y !in 2020..2025) put("year", "Debe ser 2020–2025")
            if (f.price.toIntOrNull()?.let { it > 0 } != true) put("price", "Número > 0")
            if (f.stock.toIntOrNull()?.let { it >= 0 } != true) put("stock", "Número ≥ 0")
            if (f.engine.isBlank()) put("engine", "Requerido")
            if (f.power.toIntOrNull()?.let { it > 0 } != true) put("power", "Número > 0")
            if (f.description.length < 30) put("description", "Mín. 30 caracteres")
        }
        form = form.copy(errors = errs)
        return errs.isEmpty()
    }


    fun submit(defaultImageRes: Int, onSaved: (ProductoUi) -> Unit) {
        if (!validate()) return
        val f = form
        val id = f.id ?: CatalogoStore.nextId()
        val producto = ProductoUi(
            id = id,
            brand = f.brand.trim(),
            model = f.model.trim(),
            year = f.year.toInt(),
            priceClp = f.price.toInt(),
            stock = f.stock.toInt(),
            imageRes = f.imageRes ?: defaultImageRes,
            description = f.description.trim(),
            engine = f.engine.trim(),
            powerHp = f.power.toInt(),
            abs = f.abs
        )
        CatalogoStore.save(producto)
        onSaved(producto)
        form = InventarioForm() // limpiar formulario
    }
}
