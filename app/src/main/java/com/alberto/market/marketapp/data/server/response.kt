package com.alberto.market.marketapp.data.server

import com.google.gson.annotations.SerializedName

data class UserRemote(
    val uuid: String,
    @SerializedName("nombres")
    val names: String,
    @SerializedName("apellidos")
    val surname: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("celular")
    val phone: String,
    @SerializedName("genero")
    val gender: String,
    @SerializedName("nroDoc")
    val documentNumber: String,
    @SerializedName("tipo")
    val type: String
)

data class GenderRemote(
    @SerializedName("genero")
    val gender: String,
    @SerializedName("descripcion")
    val description: String
) {
    override fun toString(): String {
        return "$description"
    }
}

data class CategoryRemote(
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("nombre")
    val name: String,
    val cover: String
)

data class ProductRemote(
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("descripcion")
    val description: String,
    @SerializedName("codigo")
    val code: String,
    @SerializedName("caracteristicas")
    val features: String,
    @SerializedName("precio")
    val price: Double,
    @SerializedName("stock")
    val stock: Int,
    @SerializedName("imagenes")
    val image: List<String>? = null,
    @SerializedName("cantidad")
    val quantity: Int,
)

data class RecordRemote(
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("correlativo")
    val correlative: String,
    @SerializedName("fechaEnvio")
    val date: String,
    @SerializedName("horaEnvio")
    val hour: String,
    @SerializedName("productos")
    val product: List<ProductDto>,
    @SerializedName("direccionEnvio")
    val addressDelivery: AddressDeliveryDto,
    @SerializedName("metodoPago")
    val paymentType: PaymentTypeDto,
    @SerializedName("total")
    val totalAmount: Double,
    @SerializedName("estado")
    val status: Int,
)


data class ProductDto(
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("descripcion")
    val description: String,
    @SerializedName("codigo")
    val code: String,
    @SerializedName("caracteristicas")
    val features: String,
    @SerializedName("precio")
    val price: Double,
    @SerializedName("stock")
    val stock: Int,
    @SerializedName("imagenes")
    val images: List<String>? = null,
    @SerializedName("cantidad")
    val quantity: Int,
)

data class AddressDeliveryDto(
    @SerializedName("tipo")
    val type: Int,
    @SerializedName("direccion")
    val address: String,
    @SerializedName("referencia")
    val reference: String,
    @SerializedName("distrito")
    val district: String,
)

data class PaymentTypeDto(
    @SerializedName("tipo")
    val type: Int,
    @SerializedName("monto")
    val amount: Double,
    @SerializedName("delivery")
    val delivery: Double,
)