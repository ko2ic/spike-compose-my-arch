package com.ko2ic.sample.ui.coroutines

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class ApiBaseState @Inject constructor() {

    private val _error = Channel<Throwable>()
    val error = _error.receiveAsFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun send(t: Throwable) {
        _error.trySend(t)
    }

    fun loading(isLoading: Boolean) {
        _loading.value = isLoading
    }
}