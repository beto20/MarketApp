package com.alberto.market.marketapp.data.datasource.impl

import com.alberto.market.marketapp.data.ErrorMessage
import com.alberto.market.marketapp.data.datasource.CategoryLocalDataSource
import com.alberto.market.marketapp.data.local.CategoryDao
import com.alberto.market.marketapp.data.local.DbCategory
import com.alberto.market.marketapp.data.tryCallNoReturnData
import com.alberto.market.marketapp.domain.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryLocalDataSourceImpl @Inject constructor(private val categoryDao: CategoryDao)
    : CategoryLocalDataSource {


    override val categories: Flow<List<Category>> = categoryDao.getAll().map { it.toDomainModel() }

    override suspend fun isEmpty(): Boolean {
        return categoryDao.categoriesCount() == 0
    }

    override suspend fun count(): Int {
        return categoryDao.categoriesCount()
    }

    override suspend fun save(dbCategories: List<DbCategory>): ErrorMessage? = tryCallNoReturnData {
        categoryDao.insertCategories(dbCategories)
    }

    private fun List<DbCategory>.toDomainModel(): List<Category> = map { it.toDomainModel() }

    private fun DbCategory.toDomainModel(): Category = Category(uuid, name, cover)
}