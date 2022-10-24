package com.alberto.market.marketapp.data.repository

import arrow.core.Either
import com.alberto.market.marketapp.data.ErrorMessage
import com.alberto.market.marketapp.data.server.RegisterRequest
import com.alberto.market.marketapp.data.datasource.UserRemoteDataSource
import com.alberto.market.marketapp.domain.Gender
import com.alberto.market.marketapp.domain.User
import javax.inject.Inject


class UserRepository @Inject constructor(private val userRemoteDataSource: UserRemoteDataSource) {

    suspend fun requestAuth(email: String, password: String, firebaseToken: String): Either<ErrorMessage, User> {
        return userRemoteDataSource.auth(email, password, firebaseToken)
    }

    suspend fun getGenders(): Either<ErrorMessage, List<Gender>> {
        return userRemoteDataSource.getGenders()
    }

    suspend fun registerAccount(registerRequest: RegisterRequest): Either<ErrorMessage, User> {
        return userRemoteDataSource.registerAccount(registerRequest)
    }
}