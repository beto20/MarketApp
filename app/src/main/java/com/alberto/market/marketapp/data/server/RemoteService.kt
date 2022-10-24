package com.alberto.market.marketapp.data.server

import com.alberto.market.marketapp.data.*
import retrofit2.http.*

interface RemoteService {

    @POST("/api/usuarios/login")
    suspend fun auth(@Body request: LoginRequest): WrappedResponse<UserRemote>

    @GET("/api/usuarios/obtener-generos")
    suspend fun getGenders(): WrappedListResponse<GenderRemote>

    @POST("/api/usuarios/crear-cuenta")
    suspend fun registerAccount(@Body request: RegisterRequest): WrappedResponse<UserRemote>

    @GET("/api/categorias")
    suspend fun getCategories(@Header("Authorization") token: String): WrappedListResponse<CategoryRemote>

    @GET("/api/categorias/{categoriaId}/productos")
    suspend fun getProducts(@Header("Authorization") token : String,
                            @Path("categoriaId") categoryId: String ) : WrappedListResponse<ProductRemote>

    @POST("/api/admin/crear-categoria")
    suspend fun createCategory(@Header("Authorization") token: String,
                               @Body category: RegisterRequestCategory): WrappedResponse<Nothing>

    @POST("/api/compras/nueva-compra")
    suspend fun processPayment(@Header("Authorization") token: String,
                               @Body paymentRequest: PaymentRequest): WrappedResponse<String>

}