package com.alberto.market.marketapp.data.datasource

import arrow.core.Either
import com.alberto.market.marketapp.data.ErrorMessage
import com.alberto.market.marketapp.data.server.PaymentRequest
import com.alberto.market.marketapp.data.server.WrappedResponse

interface PaymentRemoteDataSource {

    suspend fun processPayment(paymentRequest: PaymentRequest): Either<ErrorMessage, WrappedResponse<String>>
}