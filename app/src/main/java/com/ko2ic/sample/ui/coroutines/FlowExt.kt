package com.ko2ic.sample.ui.coroutines

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion

fun <T> Flow<T>.loading(apiBaseState: ApiBaseState): Flow<T> {
    apiBaseState.loading(true)
    return this.onCompletion {
        apiBaseState.loading(false)
    }
}

/**
 * customErrorHandlingでtrueを返すと共通エラーの処理を動かさない
 */
fun <T> Flow<T>.catching(
    apiBaseState: ApiBaseState,
    customErrorHandling: ((type: Throwable) -> Boolean)? = null
): Flow<T> {
    return this.catch { cause ->
        if (customErrorHandling != null) {
            kotlin.runCatching {
                if (customErrorHandling(cause)) {
                    return@catch
                }
            }.onFailure {
                apiBaseState.send(it)
                return@catch
            }
        }
        apiBaseState.send(cause)
    }
}