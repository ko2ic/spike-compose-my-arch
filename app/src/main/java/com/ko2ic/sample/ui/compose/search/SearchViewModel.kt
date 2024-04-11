package com.ko2ic.sample.ui.compose.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ko2ic.sample.model.Analytics
import com.ko2ic.sample.repository.GitHubRepository
import com.ko2ic.sample.ui.LoadingItemUiState
import com.ko2ic.sample.ui.coroutines.ApiBaseState
import com.ko2ic.sample.ui.coroutines.catching
import com.ko2ic.sample.ui.coroutines.loading
import com.ko2ic.sample.ui.stable.StableOnClickItemInvoker
import com.ko2ic.sample.ui.stable.StableReachedLoadPageInvoker
import com.ko2ic.sample.ui.stable.StableSendViewableLogInvoker
import com.ko2ic.sample.ui.stable.StableTransitionEventEmitter
import com.ko2ic.sample.ui.transition.TransitionEventInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val analytics: Analytics,
    private val gitHubRepository: GitHubRepository,
    val apiBaseState: ApiBaseState,
) : ViewModel() {

    sealed interface TransitionEvent : TransitionEventInterface {
        data class OnClickItem(val url: String) : TransitionEvent
    }

    val inputUiState: StateFlow<String>
        get() = _inputUiState.asStateFlow()
    private val _inputUiState = MutableStateFlow("")

    val uiState: StateFlow<SearchUiState?>
        get() = _uiState.asStateFlow()
    private val _uiState = MutableStateFlow<SearchUiState?>(null)

    val customErrorState: StateFlow<String?>
        get() = _customErrorState.asStateFlow()
    private val _customErrorState = MutableStateFlow<String?>(null)

    private val _navigationChannel = Channel<TransitionEventInterface>()
    val navigationChannel = _navigationChannel.receiveAsFlow()

    private var nextCursor: Int = 1

    fun init(query: String) {
        fetch(query, nextCursor)
    }

    private fun fetch(query: String, page: Int) {
        gitHubRepository.fetch(query, page).onEach {
            val uiState = SearchUiState(
                it.totalCount,
                it.isIncompleteResults,
                it.items.map {
                    val itemViewModel =
                        RepositoriesItemViewModel(
                            analytics,
                            StableTransitionEventEmitter(_navigationChannel::trySend)
                        )
                    val itemUiState = RepositoriesItemUiState(
                        it,
                        StableOnClickItemInvoker(itemViewModel::onClickItem)
                    )
                    itemViewModel.init(itemUiState)
                    itemUiState
                },
                StableSendViewableLogInvoker { index ->
                    analytics.sendViewableLog(index)
                },
                StableReachedLoadPageInvoker {
                    apiBaseState.enablePaging()
                    val oldItems = _uiState.value?.items?.toMutableList()
                    oldItems?.add(LoadingItemUiState.instance)
                    _uiState.value = _uiState.value?.copy(items = oldItems ?: emptyList())
                    nextCursor++
                    viewModelScope.launch {
                        delay(5000)
                    }
                    fetch(query, nextCursor)
                }
            )
            if (nextCursor == 1) {
                _uiState.value = uiState
            } else {
                val oldItems = _uiState.value?.items?.toMutableList()
                oldItems?.addAll(uiState.items)
                _uiState.value = uiState.copy(items = oldItems ?: emptyList())
            }
        }.loading(apiBaseState).catching(apiBaseState) {
            // 特別なエラー表示をしたいならここでカスタムエラー用のStateに設定し、trueを返す
            _customErrorState.value = "エラーが発生しました"
            return@catching false
        }.onCompletion {
            val oldItems = _uiState.value?.items?.toMutableList()
            oldItems?.remove(LoadingItemUiState.instance)
            _uiState.value = _uiState.value?.copy(items = oldItems ?: emptyList())
        }.launchIn(viewModelScope)
    }
}


