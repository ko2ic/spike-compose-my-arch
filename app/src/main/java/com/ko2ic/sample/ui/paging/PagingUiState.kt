package com.ko2ic.sample.ui.paging

import com.ko2ic.sample.ui.CollectionItemUiState
import com.ko2ic.sample.ui.stable.StableReachedLoadPageInvoker

interface PagingUiState {

    val items: List<CollectionItemUiState>
    val reachedLoadPageInvoker: StableReachedLoadPageInvoker

    fun reachedPageLoadPosition() {
        reachedLoadPageInvoker.invoke()
    }

    fun copyItems(items: List<CollectionItemUiState>): PagingUiState
}