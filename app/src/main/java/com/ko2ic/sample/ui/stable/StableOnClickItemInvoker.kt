package com.ko2ic.sample.ui.stable

import androidx.compose.runtime.Stable

@Stable
class StableOnClickItemInvoker(
    private val invoker: () -> Unit
) {

    fun invoke() {
        invoker()
    }
}