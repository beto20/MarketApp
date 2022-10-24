package com.alberto.market.marketapp.usecases

import com.alberto.market.marketapp.data.repository.ProductRepository
import com.alberto.market.marketapp.data.server.ProductRequest
import javax.inject.Inject

class AddProduct @Inject constructor(private val productRepository: ProductRepository) {

    suspend operator fun invoke(productRequest: ProductRequest) = productRepository.saveProduct(productRequest)
}