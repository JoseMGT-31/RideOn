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
            imageRes = android.R.drawable.ic_menu_report_image, // agrega kawasaki_z900.webp/jpg en drawable
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
            imageRes = android.R.drawable.ic_menu_report_image, // agrega kawa_ninja650.webp/jpg
            description = "Carenada ágil y versátil; perfecta para uso diario y paseos cortos.",
            engine = "649 cc",
            powerHp = 68,
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
            imageRes = android.R.drawable.ic_menu_report_image,// agrega bmw_f900r.webp/jpg
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
            imageRes = android.R.drawable.ic_menu_report_image,// agrega bmw_r1250gs.webp/jpg
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
            imageRes = R.drawable.mt07, // ya la tienes: mt07.jpg -> R.drawable.mt07
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
            imageRes = android.R.drawable.ic_menu_report_image, // agrega yamaha_r7.webp/jpg
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
            imageRes = android.R.drawable.ic_menu_report_image, // agrega honda_cb500f.webp/jpg
            description = "Naked intermedia muy equilibrada en consumo, peso y comodidad.",
            engine = "471 cc",
            powerHp = 47,
            abs = true
        ),
        ProductoUi(
            id = 8,
            brand = "Honda",
            model = "Africa Twin CRF1100L",
            year = 2024,
            priceClp = 17_990_000,
            stock = 1,
            imageRes = android.R.drawable.ic_menu_report_image, // agrega honda_africa_twin.webp/jpg
            description = "Trail de largo aliento con electrónica avanzada y ergonomía off-road.",
            engine = "1084 cc",
            powerHp = 101,
            abs = true
        )
    )

    fun byId(id: Int): ProductoUi? = productos.firstOrNull { it.id == id }
}
