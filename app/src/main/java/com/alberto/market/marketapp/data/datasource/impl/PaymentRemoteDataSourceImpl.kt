package com.alberto.market.marketapp.data.datasource.impl

import android.content.SharedPreferences
import arrow.core.Either
import com.alberto.market.marketapp.data.ErrorMessage
import com.alberto.market.marketapp.data.datasource.PaymentRemoteDataSource
import com.alberto.market.marketapp.data.server.PaymentRequest
import com.alberto.market.marketapp.data.server.RemoteService
import com.alberto.market.marketapp.data.server.WrappedResponse
import com.alberto.market.marketapp.data.tryCall
import com.alberto.market.marketapp.util.Constants
import javax.inject.Inject

class PaymentRemoteDataSourceImpl @Inject constructor(
    private val remoteService: RemoteService,
    private val sharedPreferences: SharedPreferences
): PaymentRemoteDataSource {

    override suspend fun processPayment(paymentRequest: PaymentRequest): Either<ErrorMessage, WrappedResponse<String>> = tryCall {
        val token = sharedPreferences.getString(Constants.TOKEN, "") ?: ""
        remoteService.processPayment("Bearer $token", paymentRequest)
    }
}