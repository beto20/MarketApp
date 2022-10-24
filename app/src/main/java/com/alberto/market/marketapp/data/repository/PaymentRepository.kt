package com.alberto.market.marketapp.data.repository

import arrow.core.Either
import com.alberto.market.marketapp.data.ErrorMessage
import com.alberto.market.marketapp.data.datasource.PaymentRemoteDataSource
import com.alberto.market.marketapp.data.server.PaymentRequest
import com.alberto.market.marketapp.data.server.WrappedResponse
import javax.inject.Inject

class PaymentRepository @Inject constructor(
    private val paymentRemoteDataSource: PaymentRemoteDataSource
) {
    suspend fun processPayment(paymentRequest: PaymentRequest): Either<ErrorMessage, WrappedResponse<String>> {
        return paymentRemoteDataSource.processPayment(paymentRequest)
    }
}