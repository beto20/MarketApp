package com.alberto.market.marketapp.usecases

import arrow.core.Either
import com.alberto.market.marketapp.data.ErrorMessage
import com.alberto.market.marketapp.data.repository.UserRepository
import com.alberto.market.marketapp.domain.Gender
import javax.inject.Inject

class GetGenders @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke(): Either<ErrorMessage, List<Gender>> = userRepository.getGenders()
}