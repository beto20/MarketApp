package com.alberto.market.marketapp.data.local

import androidx.room.*


@Database(entities = [DbCategory::class, DbProduct::class], version = 1, exportSchema = true)
abstract class AppDatabase: RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    abstract fun productDao(): ProductDao
}