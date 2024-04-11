package com.ko2ic.sample.ui.compose.search

import androidx.compose.runtime.Stable
import com.ko2ic.sample.ui.CollectionItemUiState
import com.ko2ic.sample.ui.paging.PagingUiState
import com.ko2ic.sample.ui.stable.StableReachedLoadPageInvoker
import com.ko2ic.sample.ui.stable.StableSendViewableLogInvoker

@Stable
data class SearchUiState(
    val totalCount: Int,
    val isIncompleteResults: Boolean,
    override val items: List<CollectionItemUiState>,
    private val sendViewableLogInvoker: StableSendViewableLogInvoker,
    override val reachedLoadPageInvoker: StableReachedLoadPageInvoker,
) : PagingUiState {

    fun sendViewableLog(index: Int) {
        sendViewableLogInvoker.invoke(index)
    }

    override fun copyItems(items: List<CollectionItemUiState>): SearchUiState {
        return copy(items = items)
    }
}

