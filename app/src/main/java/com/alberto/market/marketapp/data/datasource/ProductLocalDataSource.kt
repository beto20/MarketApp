package com.alberto.market.marketapp.data.datasource

import com.alberto.market.marketapp.data.ErrorMessage
import com.alberto.market.marketapp.data.local.DbProduct
import com.alberto.market.marketapp.domain.ProductDto
import kotlinx.coroutines.flow.Flow

interface ProductLocalDataSource {

    suspend fun save(dbProduct: DbProduct): ErrorMessage?

    suspend fun getProductsOrder(): Flow<List<ProductDto>>
}