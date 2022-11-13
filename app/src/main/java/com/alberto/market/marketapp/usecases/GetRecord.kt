package com.alberto.market.marketapp.usecases

import arrow.core.Either
import com.alberto.market.marketapp.data.ErrorMessage
import com.alberto.market.marketapp.data.repository.ProductRepository
import com.alberto.market.marketapp.domain.Record
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecord @Inject constructor(private val productRepository: ProductRepository) {

    suspend operator fun invoke(): Flow<Either<ErrorMessage, List<Record>>> {
        return productRepository.getRecord();
    }
}