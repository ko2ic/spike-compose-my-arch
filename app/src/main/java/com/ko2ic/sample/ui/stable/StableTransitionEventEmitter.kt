package com.ko2ic.sample.ui.stable

import androidx.compose.runtime.Stable
import com.ko2ic.sample.ui.transition.TransitionEventInterface

@Stable
class StableTransitionEventEmitter(private val sendFunc: (TransitionEventInterface) -> Unit) {

    fun trySend(event: TransitionEventInterface) {
        sendFunc(event)
    }

    suspend fun send(event: TransitionEventInterface) {
        sendFunc(event)
    }
}