package com.alberto.market.marketapp.usecases

import arrow.core.Either
import com.alberto.market.marketapp.data.ErrorMessage
import com.alberto.market.marketapp.data.server.RegisterRequest
import com.alberto.market.marketapp.data.repository.UserRepository
import com.alberto.market.marketapp.domain.User
import javax.inject.Inject

class RegisterAccount @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke(registerRequest: RegisterRequest): Either<ErrorMessage, User> {
        return userRepository.registerAccount(registerRequest)
    }
}