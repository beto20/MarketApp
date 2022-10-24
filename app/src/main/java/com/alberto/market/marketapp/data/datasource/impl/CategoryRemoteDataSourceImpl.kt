package com.alberto.market.marketapp.data.datasource.impl

import android.content.SharedPreferences
import arrow.core.Either
import com.alberto.market.marketapp.data.ErrorMessage
import com.alberto.market.marketapp.data.server.RegisterRequestCategory
import com.alberto.market.marketapp.data.server.WrappedResponse
import com.alberto.market.marketapp.data.datasource.CategoryRemoteDataSource
import com.alberto.market.marketapp.data.server.CategoryRemote
import com.alberto.market.marketapp.data.server.RemoteService
import com.alberto.market.marketapp.data.tryCall
import com.alberto.market.marketapp.domain.Category
import com.alberto.market.marketapp.util.Constants
import javax.inject.Inject

class CategoryRemoteDataSourceImpl @Inject constructor(
    private val remoteService: RemoteService,
    private val sharedPreferences: SharedPreferences
    )
    : CategoryRemoteDataSource {


    override suspend fun getCategory(): Either<ErrorMessage, List<Category>> = tryCall {
        val token = sharedPreferences.getString(Constants.TOKEN, "") ?: ""
        val response = remoteService.getCategories("Bearer $token")

        response?.let {
            it.data!!.toDomainModel()
        }!!
    }

    override suspend fun saveCategory(requestCategory: RegisterRequestCategory): Either<ErrorMessage, WrappedResponse<Nothing>> = tryCall {
//        val token = sharedPreferences.getString(Constants.TOKEN, "") ?: ""
        val token = ""
        remoteService.createCategory("Bearer $token", requestCategory)
    }


    private fun List<CategoryRemote>.toDomainModel() : List<Category> = map { it.toDomainModel()}

    private fun CategoryRemote.toDomainModel(): Category = Category(uuid, name, cover)

}