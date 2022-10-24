package com.alberto.market.marketapp.data.datasource

import arrow.core.Either
import com.alberto.market.marketapp.data.ErrorMessage
import com.alberto.market.marketapp.domain.Product

interface ProductRemoteDataSource {

    suspend fun getProducts(categoryId: String): Either<ErrorMessage, List<Product>>
}