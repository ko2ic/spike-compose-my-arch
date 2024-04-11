package com.ko2ic.sample.ui.stable

import androidx.compose.runtime.Stable

@Stable
class StableReachedLoadPageInvoker(
    private val invoker: () -> Unit
) {

    operator fun invoke() {
        invoker()
    }
}