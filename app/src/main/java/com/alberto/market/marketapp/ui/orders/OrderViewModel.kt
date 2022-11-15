package com.alberto.market.marketapp.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alberto.market.marketapp.domain.ProductDto
import com.alberto.market.marketapp.usecases.GetProductsOrder
import com.alberto.market.marketapp.usecases.RemoveProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val getProductsOrder: GetProductsOrder,
    private val removeProduct: RemoveProduct
) : ViewModel() {

    private val _state = MutableStateFlow<OrderState>(OrderState.Init)
    val state: StateFlow<OrderState> get() = _state

    fun getProductsOrder() {
        viewModelScope.launch {
            getProductsOrder.invoke()
                .catch { error ->
                    _state.value = OrderState.Error(error.toString())
                }
                .collect { productsDto ->
                    _state.value = OrderState.Success(productsDto)
                }
        }
    }

    fun removeProduct(productId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                removeProduct.invoke(productId)
            }
        }
    }

    sealed class OrderState {

        object Init : OrderState()
        data class IsLoading(val isLoading: Boolean) : OrderState()
        data class Error(val message: String) : OrderState()
        data class Success(val products: List<ProductDto>) : OrderState()
    }
}