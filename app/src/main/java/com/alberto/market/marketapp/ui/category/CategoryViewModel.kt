package com.alberto.market.marketapp.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alberto.market.marketapp.domain.Category
import com.alberto.market.marketapp.usecases.GetCategories
import com.alberto.market.marketapp.usecases.RequestCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val requestCategory: RequestCategory,
    getCategories: GetCategories
    ): ViewModel() {

    private val _state = MutableStateFlow<CategoryState>(CategoryState.Init)
    val state: StateFlow<CategoryState> get() = _state

    init {
        viewModelScope.launch {
            getCategories()
                .catch { error ->
                    _state.value = CategoryState.Error(error.toString())
                }
                .collect { categories ->
                    _state.value = CategoryState.Success(categories)
                }
        }
    }

    fun onUiReady() {
        viewModelScope.launch {
            _state.value = CategoryState.IsLoading(true)
            requestCategory()
            _state.value = CategoryState.IsLoading(false)
        }
    }

    sealed class CategoryState {

        object Init: CategoryState()
        data class IsLoading(val isLoading: Boolean): CategoryState()
        data class Success(val categories: List<Category>): CategoryState()
        data class Error(val rawResponse: String): CategoryState()
    }
}