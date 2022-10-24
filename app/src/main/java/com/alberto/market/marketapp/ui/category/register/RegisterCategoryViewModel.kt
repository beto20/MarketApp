package com.alberto.market.marketapp.ui.category.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alberto.market.marketapp.data.server.RegisterRequestCategory
import com.alberto.market.marketapp.usecases.RegisterCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterCategoryViewModel @Inject constructor(private val registerCategory: RegisterCategory)
: ViewModel() {

    private val _state = MutableStateFlow<CategoryCreateState>(CategoryCreateState.Init)
    val state: StateFlow<CategoryCreateState> get() = _state.asStateFlow()

    fun saveCategory(requestCategory: RegisterRequestCategory) {
        viewModelScope.launch {
            _state.value = CategoryCreateState.IsLoading(true)

            try {

                val response = withContext(Dispatchers.IO) {
                    registerCategory.invoke(requestCategory)
                }

                response.collect() {
                    it.fold(
                        { error ->
                            _state.value = CategoryCreateState.Error(error.toString())
                        },
                        { response ->
                            _state.value = CategoryCreateState.Success(response.message)
                        }
                    )
                }

            } catch (e: Exception) {
                _state.value = CategoryCreateState.Error(e.message.toString())
            } finally {
                _state.value = CategoryCreateState.IsLoading(false)
            }

        }
    }

    sealed class CategoryCreateState {
        object Init: CategoryCreateState()

        data class IsLoading(val isLoading: Boolean): CategoryCreateState()
        data class Success(val response: String): CategoryCreateState()
        data class Error(val message: String): CategoryCreateState()
    }

}