package com.alberto.market.marketapp.usecases

import com.alberto.market.marketapp.data.repository.CategoryRepository
import javax.inject.Inject

class GetCategories @Inject constructor(private val categoryRepository: CategoryRepository){

    operator fun invoke() = categoryRepository.categories
}