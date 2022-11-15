package com.alberto.market.marketapp.usecases

import com.alberto.market.marketapp.data.repository.ProductRepository
import javax.inject.Inject

class RemoveProduct @Inject constructor(private val productRepository: ProductRepository) {

    suspend operator fun invoke(productId: String) = productRepository.removeProduct(productId)
}