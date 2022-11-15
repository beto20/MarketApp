package com.alberto.market.marketapp.data.datasource.impl

import com.alberto.market.marketapp.data.ErrorMessage
import com.alberto.market.marketapp.data.datasource.ProductLocalDataSource
import com.alberto.market.marketapp.data.local.DbProduct
import com.alberto.market.marketapp.data.local.ProductDao
import com.alberto.market.marketapp.data.tryCallNoReturnData
import com.alberto.market.marketapp.domain.ProductDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductLocalDataSourceImpl @Inject constructor(
    private val productDao: ProductDao
): ProductLocalDataSource {

    override suspend fun save(dbProduct: DbProduct): ErrorMessage? = tryCallNoReturnData {
        productDao.insertProduct(dbProduct)
    }

    override suspend fun getProductsOrder(): Flow<List<ProductDto>> {
        return productDao.getAllProducts().map {
            it.toDomainModel()
        }
    }

    override suspend fun remove(productId: String): ErrorMessage? = tryCallNoReturnData {
        productDao.deleteProductById(productId)
    }

    private fun List<DbProduct>.toDomainModel(): List<ProductDto> = map { it.toDomainModel() }
    private fun DbProduct.toDomainModel(): ProductDto = ProductDto(uuid, categoryId, description, code, features, price, quantity, totalAmount, image)
}