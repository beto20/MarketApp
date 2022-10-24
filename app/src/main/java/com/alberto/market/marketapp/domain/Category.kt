package com.alberto.market.marketapp.domain

import java.io.Serializable

data class Category(
    val uuid: String,
    val name: String,
    val cover: String
): Serializable