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