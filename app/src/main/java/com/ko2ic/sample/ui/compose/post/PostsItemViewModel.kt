package com.ko2ic.sample.ui.compose.post

import com.ko2ic.sample.repository.JsonPlaceholderRepository
import com.ko2ic.sample.ui.coroutines.ApiBaseState
import com.ko2ic.sample.ui.coroutines.catching
import com.ko2ic.sample.ui.coroutines.loading
import com.ko2ic.sample.ui.stable.StableTransitionEventEmitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PostsItemViewModel(
    private val repository: JsonPlaceholderRepository?,
    private val apiBaseState: ApiBaseState,
    private val viewModelScope: CoroutineScope?,
    private val emitter: StableTransitionEventEmitter,
) {

    private lateinit var uiState: PostItemUiState

    fun init(uiState: PostItemUiState) {
        this.uiState = uiState
    }

    fun onClickItem() {
        repository?.fetchPostsError()?.onEach {
            emitter.trySend(PostsViewModel.TransitionEvent.OnClickItem)
        }?.loading(apiBaseState)?.catching(apiBaseState) { e ->
            // TODO 特別なエラー表示なら、戻り値をtrueにしてuiStateに入れて表示するようにすれば良い
            return@catching false
        }?.launchIn(viewModelScope!!)
    }
}