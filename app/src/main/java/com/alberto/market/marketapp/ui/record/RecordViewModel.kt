package com.alberto.market.marketapp.ui.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alberto.market.marketapp.data.ErrorMessage
import com.alberto.market.marketapp.domain.Record
import com.alberto.market.marketapp.usecases.GetRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val getRecord: GetRecord
) : ViewModel() {

    private val _state = MutableStateFlow<RecordState>(RecordState.Init)
    val state: StateFlow<RecordState> get() = _state

    fun getRecords() {
        viewModelScope.launch {

            try {
                val response = withContext(Dispatchers.IO) {
                    getRecord.invoke()
                }

                response.collect {
                    it.fold(
                        { error ->
                            _state.value = RecordState.Error(error.toString())
                        },
                        { records ->
                            _state.value = RecordState.Success(records)
                        }
                    )
                }
            } catch (ex: Exception) {
                _state.value = RecordState.Error(ex.message.toString())
            }
        }
    }


    sealed class RecordState {
        object Init : RecordState()
        data class Error(val message: String) : RecordState()
        data class Success(val records: List<Record>) : RecordState()
    }


}