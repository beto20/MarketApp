package com.alberto.market.marketapp.data.datasource

import com.alberto.market.marketapp.data.ErrorMessage
import com.alberto.market.marketapp.data.local.DbCategory
import com.alberto.market.marketapp.domain.Category
import kotlinx.coroutines.flow.Flow

interface CategoryLocalDataSource {

    val categories: Flow<List<Category>>

    suspend fun isEmpty(): Boolean

    suspend fun count(): Int

    suspend fun save(dbCategories: List<DbCategory>): ErrorMessage?
}