package com.alberto.market.marketapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alberto.market.marketapp.domain.User
import com.alberto.market.marketapp.usecases.RequestAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val requestAuth: RequestAuth)
    : ViewModel() {

    private val _state = MutableLiveData<LoginState>(LoginState.Init)
    val state: LiveData<LoginState> = _state

    fun auth(email: String, password: String) {

        viewModelScope.launch {

            _state.value = LoginState.IsLoading(true)
            try {
                val response = withContext(Dispatchers.IO) {
                    requestAuth(email, password, "")
                }
                response.fold(
                    { errorMessage ->
                        _state.value = LoginState.Error(errorMessage.toString())
                    },
                    { user ->
                        _state.value = LoginState.Success(user)
                    }
                )
            } catch (exception: Exception) {
                _state.value = LoginState.Error(exception.message.toString())
            } finally {
                _state.value = LoginState.IsLoading(false)
            }
        }
    }

    sealed class LoginState {

        object Init: LoginState()
        data class IsLoading(val isLoading: Boolean): LoginState()
        data class Error(val error: String): LoginState()
        data class Success(val user: User): LoginState()
    }
}