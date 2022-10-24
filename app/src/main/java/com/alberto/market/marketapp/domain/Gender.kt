package com.alberto.market.marketapp.domain

import java.io.Serializable


data class Gender(
    val gender: String,
    val description: String
): Serializable {
    override fun toString(): String {
        return "$description"
    }
}