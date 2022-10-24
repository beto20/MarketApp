package com.alberto.market.marketapp.domain

import java.io.Serializable

data class Product(
    val uuid: String,
    val description: String,
    val code: String,
    val features: String,
    val price: Double,
    val stock: Int,
    val image: List<String>? = null,
    val quantity: Int,
): Serializable

