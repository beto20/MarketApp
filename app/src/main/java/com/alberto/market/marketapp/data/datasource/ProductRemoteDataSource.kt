package com.alberto.market.marketapp.data.datasource

import arrow.core.Either
import com.alberto.market.marketapp.data.ErrorMessage
import com.alberto.market.marketapp.domain.Product
import com.alberto.market.marketapp.domain.Record

interface ProductRemoteDataSource {

    suspend fun getProducts(categoryId: String): Either<ErrorMessage, List<Product>>

    suspend fun getRecord(): Either<ErrorMessage, List<Record>>
}