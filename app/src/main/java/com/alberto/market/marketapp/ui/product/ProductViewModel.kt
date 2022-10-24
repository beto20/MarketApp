package com.alberto.market.marketapp.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alberto.market.marketapp.domain.Product
import com.alberto.market.marketapp.domain.ProductResponseDto
import com.alberto.market.marketapp.usecases.RequestProducts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductViewModel
@Inject constructor(
    private val requestProducts: RequestProducts
) : ViewModel() {

    private val _state = MutableStateFlow<ProductState>(ProductState.Init)
    val state: StateFlow<ProductState> get() = _state

    fun getProducts(categoryId: String) {
        viewModelScope.launch {

            _state.value = ProductState.IsLoading(true)
            try {
                val response = withContext(Dispatchers.IO) {
                    requestProducts.invoke(categoryId)
                }
                response.collect {
                    it.fold(
                        { error ->
                            _state.value = ProductState.Error(error.toString())
                        },
                        { products ->
                            _state.value = ProductState.Success(products.toResponseDto(categoryId))
                        }
                    )
                }

            } catch (ex: Exception) {
                _state.value = ProductState.Error(ex.message.toString())
            } finally {
                _state.value = ProductState.IsLoading(false)
            }
        }
    }

    private fun List<Product>.toResponseDto(categoryId: String): List<ProductResponseDto> {
        return map {
            it.toResponseDto(categoryId)
        }
    }

    private fun Product.toResponseDto(categoryId: String): ProductResponseDto {
        return ProductResponseDto(uuid, categoryId, description, code, features, price, stock, image, quantity)
    }

    sealed class ProductState {

        object Init : ProductState()
        data class IsLoading(val isLoading: Boolean) : ProductState()
        data class Success(val products: List<ProductResponseDto>) : ProductState()
        data class Error(val rawResponse: String) : ProductState()
    }
}
