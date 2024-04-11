package com.ko2ic.sample.ui.compose.post

import androidx.compose.runtime.Stable
import com.ko2ic.sample.ui.stable.StableOnClickItemInvoker

@Stable
data class PostItemUiState(
    val userId: Long,
    val id: Long,
    val title: String,
    val body: String,
    private val onClickItemInvoker: StableOnClickItemInvoker
) {

    fun onClickItem() {
        onClickItemInvoker.invoke()
    }
}