package com.alberto.market.marketapp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.alberto.market.marketapp.application.App
import com.alberto.market.marketapp.data.datasource.*
import com.alberto.market.marketapp.data.server.RemoteService
import com.alberto.market.marketapp.data.datasource.impl.*
import com.alberto.market.marketapp.data.local.AppDatabase
import com.alberto.market.marketapp.util.Constants
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    @ApiUrl
    fun provideApiUrl(): String = "https://marketapp2021.herokuapp.com"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = HttpLoggingInterceptor().run {
        level = HttpLoggingInterceptor.Level.BODY
        level = HttpLoggingInterceptor.Level.HEADERS
        OkHttpClient.Builder().addInterceptor(this).build()
    }

    @Provides
    @Singleton
    fun provideRemoteService(@ApiUrl apiUrl: String, okHttpClient: OkHttpClient): RemoteService {
        return Retrofit.Builder()
            .baseUrl(apiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            Constants.PREFERENCE_LOGIN,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    @Provides
    @Singleton
    fun provideApplication(@ApplicationContext app: Context): App {
        return app as App
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "dbMarket"
    ).build()

    @Provides
    @Singleton
    fun provideCategoryDao(db: AppDatabase) = db.categoryDao()

    @Provides
    @Singleton
    fun provideProductDao(db: AppDatabase) = db.productDao()

}


@Module
@InstallIn(SingletonComponent::class)
abstract class AppDataModule {

    @Binds
    abstract fun bindUserRemoteDataSource(userRemoteDataSourceImpl: UserRemoteDataSourceImpl): UserRemoteDataSource

    @Binds
    abstract fun bindCategoryRemoteDataSource(categoryRemoteDataSourceImpl: CategoryRemoteDataSourceImpl): CategoryRemoteDataSource

    @Binds
    abstract fun bindProductRemoteDataSource(productRemoteDataSourceImpl: ProductRemoteDataSourceImpl): ProductRemoteDataSource

    @Binds
    abstract fun bindCategoryLocalDataSource(categoryLocalDataSourceImpl: CategoryLocalDataSourceImpl): CategoryLocalDataSource

    @Binds
    abstract fun bindProductLocalDataSource(productLocalDataSourceImpl: ProductLocalDataSourceImpl): ProductLocalDataSource

    @Binds
    abstract fun bindPaymentRemoteDataSource(paymentRemoteDataSourceImpl: PaymentRemoteDataSourceImpl): PaymentRemoteDataSource
}