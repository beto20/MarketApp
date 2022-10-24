package com.alberto.market.marketapp.data.repository

import arrow.core.Either
import com.alberto.market.marketapp.data.ErrorMessage
import com.alberto.market.marketapp.data.server.RegisterRequestCategory
import com.alberto.market.marketapp.data.server.WrappedResponse
import com.alberto.market.marketapp.data.datasource.CategoryLocalDataSource
import com.alberto.market.marketapp.data.datasource.CategoryRemoteDataSource
import com.alberto.market.marketapp.data.local.DbCategory
import com.alberto.market.marketapp.data.tryCallNoReturnData
import com.alberto.market.marketapp.domain.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryRemoteDataSource: CategoryRemoteDataSource,
    private val categoryLocalDataSource: CategoryLocalDataSource
    ) {

    val categories = categoryLocalDataSource.categories

    suspend fun requestCategories(): ErrorMessage? = tryCallNoReturnData {

        val categories = categoryRemoteDataSource.getCategory()
        categories.fold(
            {
                null
            },
            { categoriesDomain ->

                val countRemote = categoriesDomain.size
                val countLocal = categoryLocalDataSource.count()

                if (countRemote > countLocal) {
                    categoryLocalDataSource.save(categoriesDomain.toLocalModel())
                }
            }
        )
    }

    suspend fun saveCategory(requestCategory: RegisterRequestCategory): Flow<Either<ErrorMessage, WrappedResponse<Nothing>>> {
        return flow {
            emit(categoryRemoteDataSource.saveCategory(requestCategory))
        }
    }

    private fun List<Category>.toLocalModel(): List<DbCategory> = map{ it.toLocalModel() }
    private fun Category.toLocalModel(): DbCategory = DbCategory(uuid, name, cover)
}