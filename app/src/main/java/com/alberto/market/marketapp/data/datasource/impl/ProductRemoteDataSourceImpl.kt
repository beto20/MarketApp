package com.alberto.market.marketapp.data.datasource.impl

import android.content.SharedPreferences
import arrow.core.Either
import com.alberto.market.marketapp.data.ErrorMessage
import com.alberto.market.marketapp.data.datasource.ProductRemoteDataSource
import com.alberto.market.marketapp.data.server.ProductRemote
import com.alberto.market.marketapp.data.server.RemoteService
import com.alberto.market.marketapp.data.tryCall
import com.alberto.market.marketapp.domain.Product
import com.alberto.market.marketapp.util.Constants
import javax.inject.Inject

class ProductRemoteDataSourceImpl @Inject constructor(
    private val remoteService: RemoteService,
    private val sharedPreferences: SharedPreferences
): ProductRemoteDataSource {
    override suspend fun getProducts(categoryId: String): Either<ErrorMessage, List<Product>> = tryCall {
        val token = sharedPreferences.getString(Constants.TOKEN,"") ?: ""
        val response = remoteService.getProducts("Bearer $token",categoryId)

        response?.let {
            it.data!!.toDomainModel()
        }!!
    }

    private fun List<ProductRemote>.toDomainModel(): List<Product> = map { it.toDomainModel() }

    private fun ProductRemote.toDomainModel(): Product = Product(uuid, description, code, features, price, stock, image, quantity)
}