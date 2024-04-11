package com.ko2ic.sample.ui.compose.search

import com.ko2ic.sample.model.Analytics
import com.ko2ic.sample.ui.stable.StableTransitionEventEmitter

class RepositoriesItemViewModel(
    private val analytics: Analytics,
    private val emitter: StableTransitionEventEmitter,
) {

    private lateinit var repositoriesItemUiState: RepositoriesItemUiState

    fun init(repositoriesItemUiState: RepositoriesItemUiState) {
        this.repositoriesItemUiState = repositoriesItemUiState
    }

    fun onClickItem() {
        emitter.trySend(SearchViewModel.TransitionEvent.OnClickItem(repositoriesItemUiState.repositoryDto.url))
    }
}