package com.alberto.market.marketapp.domain

import java.io.Serializable


data class Record(
    val uuid: String,
    val correlative: String,
    val date: String,
    val hour: String,
    val product: List<ProductDescDto>,
    val addressDelivery: AddressDeliveryDto,
    val paymentType: PaymentTypeDto,
    val totalAmount: Double,
    val status: Int,
): Serializable


data class ProductDescDto(
    val uuid: String = "",
    val description: String = "",
    val code: String = "",
    val features: String = "",
    val price: Double = 0.00,
    val stock: Int = 0,
    val images: List<String>? = null,
    val quantity: Int = 0,
): Serializable

data class AddressDeliveryDto(
    val type: Int,
    val address: String,
    val reference: String,
    val district: String,
): Serializable

data class PaymentTypeDto(
    val type: Int,
    val amount: Double,
    val delivery: Double,
): Serializable