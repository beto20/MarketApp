package com.alberto.market.marketapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_category")
data class DbCategory(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "uuid")
    val uuid: String,

    @ColumnInfo(name = "name")
    val name: String,

    val cover: String
)

@Entity(tableName = "tb_product")
data class DbProduct(
    @PrimaryKey(autoGenerate = false)
    val uuid: String,
    val categoryId: String,
    val description: String,
    val code: String,
    val features: String,
    val price: Double,
    val quantity: Int,
    val totalAmount: Double,
    val image: String,
)