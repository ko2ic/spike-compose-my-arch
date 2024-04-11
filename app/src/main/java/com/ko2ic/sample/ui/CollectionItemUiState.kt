package com.ko2ic.sample.ui

interface CollectionItemUiState

class LoadingItemUiState private constructor() : CollectionItemUiState {

    private object Holder {

        val INSTANCE = LoadingItemUiState()
    }

    companion object {

        val instance: LoadingItemUiState by lazy { Holder.INSTANCE }
    }
}