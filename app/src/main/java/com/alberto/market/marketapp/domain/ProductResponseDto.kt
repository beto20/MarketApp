package com.alberto.market.marketapp.domain

import java.io.Serializable

data class ProductResponseDto(
    var uuid: String,
    var categoryId: String,
    var description: String,
    var code: String,
    var features: String,
    var price: Double,
    var stock: Int,
    var image: List<String>? = null,
    var quantity: Int,
): Serializable

