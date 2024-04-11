package com.ko2ic.sample.ui.compose.search

import androidx.compose.runtime.Stable
import com.ko2ic.sample.ui.CollectionItemUiState
import com.ko2ic.sample.ui.stable.StableReachedLoadPageInvoker
import com.ko2ic.sample.ui.stable.StableSendViewableLogInvoker

@Stable
data class SearchUiState(
    val totalCount: Int,
    val isIncompleteResults: Boolean,
    val items: List<CollectionItemUiState>,
    private val sendViewableLogInvoker: StableSendViewableLogInvoker,
    private val reachedLoadPageInvoker: StableReachedLoadPageInvoker
) {

    fun sendViewableLog(index: Int) {
        sendViewableLogInvoker.invoke(index)
    }

    fun reachedPageLoadPosition() {
        reachedLoadPageInvoker.invoke()
    }
}

