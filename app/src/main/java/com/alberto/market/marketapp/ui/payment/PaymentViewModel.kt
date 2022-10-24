package com.alberto.market.marketapp.ui.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alberto.market.marketapp.data.server.PaymentRequest
import com.alberto.market.marketapp.data.server.WrappedResponse
import com.alberto.market.marketapp.usecases.ProcessPayment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val processPayment: ProcessPayment
): ViewModel() {

    private val _state = MutableLiveData<PaymentState>(PaymentState.Init)
    val state: LiveData<PaymentState> get() = _state

    fun processPayment(paymentRequest: PaymentRequest) {
        viewModelScope.launch {
//            _state.value = PaymentState.IsLoading(true)

            try {

                val response = withContext(Dispatchers.IO) {
                    processPayment.invoke(paymentRequest)
                }
                response.fold(
                    {
                        _state.value = PaymentState.Error(it.toString())
                    },
                    {
                        _state.value = PaymentState.SuccessPayment(it)
                    }
                )
            } catch (ex: Exception) {
                _state.value = PaymentState.Error(ex.message.toString())
            } finally {
//                _state.value = PaymentState.IsLoading(false)
            }
        }
    }

    sealed class PaymentState {
        object Init: PaymentState()
        // TODO:: add loading path
        data class SuccessPayment(val response: WrappedResponse<String>): PaymentState()
        data class Error(val message: String): PaymentState()
    }
}