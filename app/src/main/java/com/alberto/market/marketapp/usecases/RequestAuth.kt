package com.alberto.market.marketapp.usecases

import arrow.core.Either
import com.alberto.market.marketapp.data.ErrorMessage
import com.alberto.market.marketapp.data.repository.UserRepository
import com.alberto.market.marketapp.domain.User
import javax.inject.Inject

class RequestAuth @Inject constructor(private val userRepository: UserRepository){

    suspend operator fun invoke(email: String, password:String, firebaseToken: String): Either<ErrorMessage, User> {
        return userRepository.requestAuth(email, password, firebaseToken)
    }
}