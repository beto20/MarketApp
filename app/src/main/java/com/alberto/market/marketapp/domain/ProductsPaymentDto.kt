package com.alberto.market.marketapp.domain

import java.io.Serializable

data class ProductsPaymentDto(
    var productsDto: List<ProductsDto>,
    var totalAmount: Double = 0.00,
): Serializable


data class ProductsDto(
    var uuid: String = "",
    var categoryId: String = "",
    var quantity: Int = 0,
)