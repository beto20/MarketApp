package com.alberto.market.marketapp.data.datasource.impl

import android.content.SharedPreferences
import arrow.core.Either
import com.alberto.market.marketapp.data.*
import com.alberto.market.marketapp.data.datasource.UserRemoteDataSource
import com.alberto.market.marketapp.data.server.*
import com.alberto.market.marketapp.domain.Gender
import com.alberto.market.marketapp.domain.User
import com.alberto.market.marketapp.util.Constants
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val remoteService: RemoteService,
    private val sharedPreferences: SharedPreferences
    )
    : UserRemoteDataSource{

    override suspend fun auth(email: String, password: String, firebaseToken: String): Either<ErrorMessage, User> = tryCall {

        val response = remoteService.auth(LoginRequest(email, password))

        response.let {
            sharedPreferences.edit().putString(Constants.TOKEN, response.token).apply()
            it.data!!.toUserDomainModel()
        }
    }

    private fun UserRemote.toUserDomainModel(): User = User(uuid, names, surname, email, phone, gender, documentNumber, type)

    override suspend fun getGenders(): Either<ErrorMessage, List<Gender>> = tryCall {
        val response = remoteService.getGenders()

        response.let {
            it.data!!.toGenderDomainModel()
        }

    }

    private fun List<GenderRemote>.toGenderDomainModel(): List<Gender> = map{ it.toGenderDomainModel() }
    private fun GenderRemote.toGenderDomainModel(): Gender = Gender(gender, description)


    override suspend fun registerAccount(registerRequest: RegisterRequest): Either<ErrorMessage, User> = tryCall {
        val response = remoteService.registerAccount(registerRequest)

        response.let {
            it.data!!.toUserDomainModel()
        }
    }

}