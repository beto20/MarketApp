package com.alberto.market.marketapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(dbProduct: DbProduct)

    @Query("SELECT *FROM tb_product")
    fun getAllProducts(): Flow<List<DbProduct>>

    @Query("DELETE FROM tb_product WHERE uuid = :productId")
    fun deleteProductById(productId: String)
}