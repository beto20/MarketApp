package com.alberto.market.marketapp.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import retrofit2.HttpException
import java.io.IOException

sealed class ErrorMessage {
    data class Server(val code: Int) : ErrorMessage()
    object Connectivity : ErrorMessage()
    data class Unknown(val message: String) : ErrorMessage()
}

fun Throwable.toError(): ErrorMessage = when (this) {
    is IOException -> ErrorMessage.Connectivity
    is HttpException -> ErrorMessage.Server(code())
    else -> ErrorMessage.Unknown(message ?: "")
}

suspend fun <T> tryCall(action: suspend () -> T): Either<ErrorMessage, T> = try {
    action().right()
} catch (exception: Exception) {
    exception.toError().left()
}

suspend fun <T> tryCallNoReturnData(action: suspend () -> T): ErrorMessage? = try {
    action()
    null
} catch (exception: Exception) {
    exception.toError()
}