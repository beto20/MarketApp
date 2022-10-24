package com.alberto.market.marketapp.data.server

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    val password: String,
    val firebaseToken: String = ""
)

data class RegisterRequest(
    @SerializedName("nombres")
    var names: String = "",
    @SerializedName("apellidos")
    var surname: String = "",
    var email: String = "",
    var password: String = "",
    @SerializedName("celular")
    var phone: String = "",
    @SerializedName("genero")
    var gender: String = "",
    @SerializedName("nroDoc")
    var documentNumber: String = "",
    var firebaseToken: String = "",
)


data class RegisterRequestCategory(
    @SerializedName("nombres")
    var name: String = "",
    var cover: String = "",
)

data class ProductRequest(
    var uuid: String,
    var categoryId: String,
    var description: String,
    var code: String,
    var features: String,
    var price: Double,
    var quantity: Int,
    var totalAmount: Double,
    var image: String,
)

data class PaymentRequest(
    @SerializedName("direccionEnvio")
    var address: AddressRequest,
    @SerializedName("metodoPago")
    var paymentMethod: PaymentMethodRequest,
    @SerializedName("fechaHora")
    var date: String,
    @SerializedName("productos")
    var orderList: List<OrderRequest>,
    @SerializedName("total")
    var totalPayment: Double
)

data class AddressRequest(
    @SerializedName("tipo")
    var type: Int = 0,
    @SerializedName("direccion")
    var address: String = "",
    @SerializedName("referencia")
    var reference: String = "",
    @SerializedName("distrito")
    var district: String = "",
)

data class PaymentMethodRequest(
    @SerializedName("tipo")
    var type: Int = 0,
    @SerializedName("monto")
    var amount: Double = 0.0,
)

data class OrderRequest(
    @SerializedName("categoriaId")
    var categoryId: String = "",
    @SerializedName("productoId")
    var productId: String = "",
    @SerializedName("cantidad")
    var quantity: Int = 0,
)