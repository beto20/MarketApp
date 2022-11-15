package com.alberto.market.marketapp.domain

import java.io.Serializable

data class ProductResponseDto(
    var uuid: String = "",
    var categoryId: String = "",
    var description: String = "",
    var code: String = "",
    var features: String = "",
    var price: Double = 0.0,
    var stock: Int = 0,
    var image: List<String>? = null,
    var quantity: Int = 0,
): Serializable

