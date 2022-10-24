package com.alberto.market.marketapp.usecases

import com.alberto.market.marketapp.data.repository.CategoryRepository
import javax.inject.Inject

class RequestCategory @Inject constructor(private val categoryRepository: CategoryRepository) {

    suspend operator fun invoke() = categoryRepository.requestCategories()
}