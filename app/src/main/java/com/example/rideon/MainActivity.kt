package com.example.rideon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.rideon.data.Catalogo
import com.example.rideon.ui.theme.RideOnTheme
import com.example.rideon.view.screens.CatalogoScreen
import com.example.rideon.view.screens.ProductoScreen
import com.example.rideon.view.screens.ProductoUi

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RideOnTheme {
                // estado simple: null = estoy en catálogo; !=null = estoy en detalle
                var seleccionado by remember { mutableStateOf<ProductoUi?>(null) }

                if (seleccionado == null) {
                    CatalogoScreen(
                        productos = Catalogo.productos,
                        onOpen = { p -> seleccionado = p } // abrir detalle
                    )
                } else {
                    ProductoScreen(
                        producto = seleccionado!!,
                        onBack = { seleccionado = null },      // volver al catálogo
                        onAddToCart = { qty ->
                            // por ahora solo mostramos en consola
                            println("Agregaste $qty ${seleccionado!!.model} al carrito")
                        }
                    )
                }
            }
        }
    }
}
