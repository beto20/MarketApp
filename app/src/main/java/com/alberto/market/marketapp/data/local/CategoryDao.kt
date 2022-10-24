package com.alberto.market.marketapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(dbCategories: List<DbCategory>)

    @Query("SELECT *FROM tb_category")
    fun getAll(): Flow<List<DbCategory>>

    @Query("SELECT COUNT(uuid) from tb_category")
    suspend fun categoriesCount(): Int

    //Examples
    @Query("SELECT *FROM tb_category WHERE uuid=:id")
    fun findByUuid(id:String) : Flow<DbCategory>

    @Update
    suspend fun updateCategory(dbCategory: DbCategory)

    @Delete
    suspend fun deleteCategory(dbCategory: DbCategory)
}