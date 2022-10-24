package com.alberto.market.marketapp.data.datasource

import com.alberto.market.marketapp.domain.Order
import kotlinx.coroutines.flow.Flow

interface OrderLocalDataSource {

    fun getProductsOrder(): Flow<List<Order>>
}