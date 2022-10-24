package com.alberto.market.marketapp.usecases

import arrow.core.Either
import com.alberto.market.marketapp.data.ErrorMessage
import com.alberto.market.marketapp.data.repository.PaymentRepository
import com.alberto.market.marketapp.data.server.PaymentRequest
import com.alberto.market.marketapp.data.server.WrappedResponse
import javax.inject.Inject

class ProcessPayment @Inject constructor(private val paymentRepository: PaymentRepository) {

    suspend operator fun invoke(paymentRequest: PaymentRequest): Either<ErrorMessage, WrappedResponse<String>> {
        return paymentRepository.processPayment(paymentRequest)
    }
}