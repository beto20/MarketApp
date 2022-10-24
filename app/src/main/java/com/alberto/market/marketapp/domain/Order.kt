package com.alberto.market.marketapp.domain

import java.io.Serializable

data class Order(
    var title: String,
    var quantity: Int,
    var totalAmount: Double,
    var image: String,
): Serializable
