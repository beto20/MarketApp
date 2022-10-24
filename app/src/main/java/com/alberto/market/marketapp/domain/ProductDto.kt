package com.alberto.market.marketapp.domain

import java.io.Serializable

data class ProductDto(
    val uuid: String,
    val categoryId: String,
    val description: String,
    val code: String,
    val features: String,
    val price: Double,
    val quantity: Int,
    val totalAmount: Double,
    val image: String,
): Serializable
