package com.alberto.market.marketapp.data.server

data class WrappedResponse<T>(
    val message: String,
    val success:Boolean,
    val data:T? = null,
    val token:String? = null
)

data class WrappedListResponse<T>(
    val message: String,
    val success:Boolean,
    val data:List<T>? = null
)