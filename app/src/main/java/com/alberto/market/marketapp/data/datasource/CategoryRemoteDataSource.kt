package com.alberto.market.marketapp.data.datasource

import arrow.core.Either
import com.alberto.market.marketapp.data.ErrorMessage
import com.alberto.market.marketapp.data.server.RegisterRequestCategory
import com.alberto.market.marketapp.data.server.WrappedResponse
import com.alberto.market.marketapp.domain.Category

interface CategoryRemoteDataSource {

    suspend fun getCategory(): Either<ErrorMessage, List<Category>>

    suspend fun saveCategory(requestCategory: RegisterRequestCategory): Either<ErrorMessage, WrappedResponse<Nothing>>
}