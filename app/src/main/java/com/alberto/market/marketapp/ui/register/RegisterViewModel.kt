package com.alberto.market.marketapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alberto.market.marketapp.data.server.RegisterRequest
import com.alberto.market.marketapp.domain.Gender
import com.alberto.market.marketapp.domain.User
import com.alberto.market.marketapp.usecases.GetGenders
import com.alberto.market.marketapp.usecases.RegisterAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val getGenders: GetGenders,
    private val registerAccount: RegisterAccount,
    ): ViewModel() {

    private val _state = MutableLiveData<RegisterAccountState>(RegisterAccountState.Init)
    val state : LiveData<RegisterAccountState> get() = _state


    init {
        viewModelScope.launch {
            _state.value = RegisterAccountState.IsLoading(true)

            try {
                val response = withContext(Dispatchers.IO) {
                    getGenders()
                }
                response.fold(
                    { errorMessage ->
                        _state.value = RegisterAccountState.Error(errorMessage.toString())
                    },
                    { genders ->
                        _state.value = RegisterAccountState.SuccessGenders(genders)
                    }
                )

            } catch (exception: Exception) {
                _state.value = RegisterAccountState.Error(exception.message.toString())
            } finally {
                _state.value = RegisterAccountState.IsLoading(false)
            }
        }
    }

    fun createAccount(registerRequest: RegisterRequest) {
        viewModelScope.launch {
            _state.value = RegisterAccountState.IsLoading(true)

            try {
                val response = withContext(Dispatchers.IO) {
                    registerAccount(registerRequest)
                }
                response.fold(
                    { errorMessage ->
                        _state.value = RegisterAccountState.Error(errorMessage.toString())
                    },
                    { user ->
                        _state.value = RegisterAccountState.SuccessRegister(user)
                    }
                )

            } catch (exception: Exception) {
                _state.value = RegisterAccountState.Error(exception.message.toString())
            } finally {
                _state.value = RegisterAccountState.IsLoading(false)
            }
        }
    }

    sealed class RegisterAccountState {
        object Init: RegisterAccountState()

        data class IsLoading(val isLoading: Boolean): RegisterAccountState()
        data class SuccessGenders(val genders: List<Gender>): RegisterAccountState()
        data class Error(val message: String): RegisterAccountState()
        data class SuccessRegister(val user: User): RegisterAccountState()
    }
}