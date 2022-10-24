package com.alberto.market.marketapp.usecases

import com.alberto.market.marketapp.data.repository.ProductRepository
import com.alberto.market.marketapp.domain.ProductDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsOrder @Inject constructor(private val productRepository: ProductRepository) {

    suspend operator fun invoke(): Flow<List<ProductDto>> {
        return productRepository.getProductsOrder()
    }
}