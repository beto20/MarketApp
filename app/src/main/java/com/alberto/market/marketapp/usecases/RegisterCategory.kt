package com.alberto.market.marketapp.usecases

import arrow.core.Either
import com.alberto.market.marketapp.data.ErrorMessage
import com.alberto.market.marketapp.data.server.RegisterRequestCategory
import com.alberto.market.marketapp.data.server.WrappedResponse
import com.alberto.market.marketapp.data.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterCategory @Inject constructor(private val categoryRepository: CategoryRepository) {

    suspend operator fun invoke(requestCategory: RegisterRequestCategory): Flow<Either<ErrorMessage, WrappedResponse<Nothing>>> {
        return categoryRepository.saveCategory(requestCategory)
    }
}