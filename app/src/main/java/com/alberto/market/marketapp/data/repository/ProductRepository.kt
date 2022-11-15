package com.alberto.market.marketapp.data.repository

import arrow.core.Either
import com.alberto.market.marketapp.data.ErrorMessage
import com.alberto.market.marketapp.data.datasource.ProductLocalDataSource
import com.alberto.market.marketapp.data.datasource.ProductRemoteDataSource
import com.alberto.market.marketapp.data.local.DbProduct
import com.alberto.market.marketapp.data.server.ProductRequest
import com.alberto.market.marketapp.data.tryCallNoReturnData
import com.alberto.market.marketapp.domain.Product
import com.alberto.market.marketapp.domain.ProductDto
import com.alberto.market.marketapp.domain.Record
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productRemoteDataSource: ProductRemoteDataSource,
    private val productLocalDataSource: ProductLocalDataSource
    ) {

    suspend fun getProducts(categoryId: String): Flow<Either<ErrorMessage, List<Product>>> {
        return flow {
            emit(productRemoteDataSource.getProducts(categoryId))
        }
    }

    suspend fun saveProduct(productRequest: ProductRequest): ErrorMessage?  = tryCallNoReturnData {
        productLocalDataSource.save(productRequest.toLocalModel())
    }

    suspend fun getProductsOrder(): Flow<List<ProductDto>> {
        return productLocalDataSource.getProductsOrder()
    }

    suspend fun getRecord(): Flow<Either<ErrorMessage, List<Record>>> {
        return flow {
            emit(productRemoteDataSource.getRecord())
        }
    }

    suspend fun removeProduct(productId: String): ErrorMessage? = tryCallNoReturnData {
        productLocalDataSource.remove(productId)
    }

    private fun ProductRequest.toLocalModel(): DbProduct = DbProduct(uuid, categoryId, description, code, features, price, quantity, totalAmount, image)

}