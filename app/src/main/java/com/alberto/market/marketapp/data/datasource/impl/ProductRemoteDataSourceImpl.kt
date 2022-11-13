package com.alberto.market.marketapp.data.datasource.impl

import android.content.SharedPreferences
import arrow.core.Either
import com.alberto.market.marketapp.data.ErrorMessage
import com.alberto.market.marketapp.data.datasource.ProductRemoteDataSource
import com.alberto.market.marketapp.data.server.ProductRemote
import com.alberto.market.marketapp.data.server.RecordRemote
import com.alberto.market.marketapp.data.server.RemoteService
import com.alberto.market.marketapp.data.tryCall
import com.alberto.market.marketapp.domain.*
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
            it.data!!.toProductDomainModel()
        }!!
    }

    override suspend fun getRecord(): Either<ErrorMessage, List<Record>> = tryCall {
        val token = sharedPreferences.getString(Constants.TOKEN, "") ?: ""
        val response = remoteService.getRecord("Bearer $token")

        response?.let {
            it.data!!.toRecordDomainModel()
        }!!
    }

    private fun List<ProductRemote>.toProductDomainModel(): List<Product> = map { it.toDomainModel() }
    private fun ProductRemote.toDomainModel(): Product = Product(uuid, description, code, features, price, stock, image, quantity)

    private fun List<RecordRemote>.toRecordDomainModel(): List<Record> = map { it.toDomainModel() }
    private fun RecordRemote.toDomainModel(): Record {
        var productDescDto: ProductDescDto
        val productList: MutableList<ProductDescDto> = arrayListOf()

        product.forEach {
            productDescDto = ProductDescDto(it.uuid, it.description, it.code, it.features, it.price, it.stock, it.images ,it.quantity)
            productList.add(productDescDto)
        }

        val addressDeliveryDto = AddressDeliveryDto(addressDelivery.type, addressDelivery.address, addressDelivery.reference, addressDelivery.district)
        val paymentTypeDto = PaymentTypeDto(paymentType.type, paymentType.amount, paymentType.delivery)

        return Record(uuid, correlative, date, hour, productList, addressDeliveryDto, paymentTypeDto, totalAmount, status)
    }

}