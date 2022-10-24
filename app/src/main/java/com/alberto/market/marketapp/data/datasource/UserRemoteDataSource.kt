package com.alberto.market.marketapp.data.datasource

import arrow.core.Either
import com.alberto.market.marketapp.data.ErrorMessage
import com.alberto.market.marketapp.data.server.RegisterRequest
import com.alberto.market.marketapp.domain.Gender
import com.alberto.market.marketapp.domain.User

interface UserRemoteDataSource {

    suspend fun auth(email: String, password: String, firebaseToken: String): Either<ErrorMessage, User>

    suspend fun getGenders(): Either<ErrorMessage, List<Gender>>

    suspend fun registerAccount(registerRequest: RegisterRequest): Either<ErrorMessage, User>
}