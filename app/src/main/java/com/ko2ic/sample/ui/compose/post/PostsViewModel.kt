package com.ko2ic.sample.ui.compose.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ko2ic.sample.repository.JsonPlaceholderRepository
import com.ko2ic.sample.ui.coroutines.ApiBaseState
import com.ko2ic.sample.ui.coroutines.catching
import com.ko2ic.sample.ui.coroutines.loading
import com.ko2ic.sample.ui.stable.StableOnClickItemInvoker
import com.ko2ic.sample.ui.stable.StableTransitionEventEmitter
import com.ko2ic.sample.ui.transition.TransitionEventInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val repository: JsonPlaceholderRepository,
    val apiBaseState: ApiBaseState,
) : ViewModel() {

    sealed interface TransitionEvent : TransitionEventInterface {
        data object OnClickItem : TransitionEvent
    }

    val uiState: StateFlow<List<PostItemUiState>>
        get() = _uiState.asStateFlow()
    private val _uiState = MutableStateFlow<List<PostItemUiState>>(emptyList())

    private val _navigationChannel = Channel<TransitionEventInterface>()
    val navigationChannel = _navigationChannel.receiveAsFlow()

    init {
        fetch()
    }

    private fun fetch() {
        repository.fetch().onEach {
            val uiStateList = it.map {
                val itemViewModel = PostsItemViewModel(
                    repository,
                    apiBaseState,
                    viewModelScope,
                    StableTransitionEventEmitter(_navigationChannel::trySend),
                )
                val uiState = PostItemUiState(
                    it.userId,
                    it.id,
                    it.title,
                    it.body,
                    StableOnClickItemInvoker(itemViewModel::onClickItem)
                )
                itemViewModel.init(uiState)
                uiState
            }

            _uiState.value = uiStateList
        }
            .loading(apiBaseState)
            .catching(apiBaseState)
            .launchIn(viewModelScope)
    }
}


