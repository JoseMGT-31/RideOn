package com.example.rideon.data

import com.example.rideon.R
import com.example.rideon.view.screens.ProductoUi

object Catalogo {

    // Lista completa del catálogo (8 modelos)
    val productos: List<ProductoUi> = listOf(
        // KAWASAKI
        ProductoUi(
            id = 1,
            brand = "Kawasaki",
            model = "Z900",
            year = 2021,
            priceClp = 8_990_000,
            stock = 3,
            imageRes = R.drawable.z900,
            description = "Naked deportiva de 4 cilindros con chasis trellis y postura cómoda para ciudad y carretera.",
            engine = "948 cc",
            powerHp = 125,
            abs = true
        ),
        ProductoUi(
            id = 2,
            brand = "Kawasaki",
            model = "Ninja 650",
            year = 2022,
            priceClp = 7_990_000,
            stock = 4,
            imageRes = R.drawable.ninja650,
            description = "Carenada ágil y versátil; perfecta para uso diario y paseos cortos.",
            engine = "649 cc",
            powerHp = 68,
            abs = true
        ),
        ProductoUi(
            id = 9,
            brand = "Kawasaki",
            model = "Ninja H2R",
            year = 2023,
            priceClp = 59_990_000, // precio de referencia aprox. en Chile
            stock = 1,
            imageRes = R.drawable.h2r,
            description = "Diseñada para circuito, con aerodinámica de fibra de carbono y rendimiento extremo. ",
            engine = "998 cc sobrealimentado",
            powerHp = 310,
            abs = true
        ),

        // BMW
        ProductoUi(
            id = 3,
            brand = "BMW",
            model = "F 900 R",
            year = 2020,
            priceClp = 10_990_000,
            stock = 2,
            imageRes = R.drawable.f900r,
            description = "Roadster dinámica con electrónica moderna y gran manejo en curvas.",
            engine = "895 cc",
            powerHp = 105,
            abs = true
        ),
        ProductoUi(
            id = 4,
            brand = "BMW",
            model = "R 1250 GS",
            year = 2023,
            priceClp = 19_990_000,
            stock = 1,
            imageRes = R.drawable.r1250gs,
            description = "Adventure icónica bóxer, ideal para rutas largas y caminos mixtos.",
            engine = "1254 cc",
            powerHp = 136,
            abs = true
        ),

        // YAMAHA
        ProductoUi(
            id = 5,
            brand = "Yamaha",
            model = "MT-07",
            year = 2021,
            priceClp = 7_890_000,
            stock = 5,
            imageRes = R.drawable.mt07,
            description = "Hyper naked compacta, torque amigable y bajo peso. Excelente para ciudad y paseos.",
            engine = "689 cc",
            powerHp = 73,
            abs = true
        ),
        ProductoUi(
            id = 6,
            brand = "Yamaha",
            model = "YZF-R7",
            year = 2022,
            priceClp = 9_990_000,
            stock = 2,
            imageRes = R.drawable.r7,
            description = "Supersport accesible con chasis afinado y postura deportiva.",
            engine = "689 cc",
            powerHp = 73,
            abs = true
        ),

        // HONDA
        ProductoUi(
            id = 7,
            brand = "Honda",
            model = "CB500F",
            year = 2022,
            priceClp = 6_290_000,
            stock = 6,
            imageRes = R.drawable.cb500f,
            description = "Naked intermedia muy equilibrada en consumo, peso y comodidad.",
            engine = "471 cc",
            powerHp = 47,
            abs = true
        ),
    )

    fun byId(id: Int): ProductoUi? = productos.firstOrNull { it.id == id }
}
