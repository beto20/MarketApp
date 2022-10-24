package com.alberto.market.marketapp.ui.product.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alberto.market.marketapp.data.server.ProductRequest
import com.alberto.market.marketapp.usecases.AddProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailProductViewModel @Inject constructor(
    private val addProduct: AddProduct,
): ViewModel() {

    private val _state = MutableStateFlow<DetailProductState>(DetailProductState.Init)
    val state: StateFlow<DetailProductState> get() = _state


    fun addProduct(productRequest: ProductRequest) {
        viewModelScope.launch {
            _state.value = DetailProductState.IsLoading(true)
            addProduct.invoke(productRequest)
            _state.value = DetailProductState.IsLoading(false)
        }
    }

    sealed class DetailProductState {

        object Init: DetailProductState()
        data class IsLoading(val isLoading: Boolean) : DetailProductState()
        data class Error(val message: String) : DetailProductState()
    }

}