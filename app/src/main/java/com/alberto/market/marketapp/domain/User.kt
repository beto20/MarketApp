package com.alberto.market.marketapp.domain

import java.io.Serializable

data class User(

    val uuid: String,
    val names: String,
    val surname: String,
    val email: String,
    val phone: String,
    val gender: String,
    val documentNumber: String,
    val type: String
): Serializable