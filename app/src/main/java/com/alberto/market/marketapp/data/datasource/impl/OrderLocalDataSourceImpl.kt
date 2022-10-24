package com.alberto.market.marketapp.data.datasource.impl

import com.alberto.market.marketapp.data.datasource.OrderLocalDataSource
import com.alberto.market.marketapp.domain.Order
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderLocalDataSourceImpl @Inject constructor(
): OrderLocalDataSource {


    override fun getProductsOrder(): Flow<List<Order>> {
//        return orderDao.getAllProducts().map {
//            it.toDomainModel
//        }

        TODO()
    }


}