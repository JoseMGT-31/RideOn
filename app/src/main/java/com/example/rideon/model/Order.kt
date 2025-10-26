package com.example.rideon.model

import java.util.Date

data class Order(

    val id: String,
    val date: Date,
    val total: Double,
    val itemCount: Int
)


