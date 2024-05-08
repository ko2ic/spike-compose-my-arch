package com.ko2ic.sample.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ko2ic.sample.model.dto.RepositoryDto
import com.ko2ic.sample.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val gitHubRepository: GitHubRepository,
) : ViewModel() {

    sealed interface Event {
        data object Init : Event
    }

    val inputUiState: StateFlow<String>
        get() = _inputUiState.asStateFlow()
    private val _inputUiState = MutableStateFlow("")

    val resultUiState: StateFlow<ResultUiState>
        get() = _resultUiState.asStateFlow()
    private val _resultUiState = MutableStateFlow<ResultUiState>(ResultUiState.Loading)
    val viewModelEvent: SharedFlow<Event>
        get() = _viewModelEvent.asSharedFlow()
    private val _viewModelEvent =
        MutableSharedFlow<Event>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    init {
        fetch(inputUiState.value, 1)
    }

    fun fetch(query: String, page: Int) {
        gitHubRepository.fetch(query, page).onEach {
            val uiState = MainUiState(
                it.totalCount,
                it.isIncompleteResults,
                it.items.map {
                    MainItemUiState(it)
                }
            )
            _resultUiState.value = ResultUiState.Success(uiState)
        }.catch { e ->
            Log.e("", e.toString())
        }.onCompletion {

        }.launchIn(viewModelScope)
    }
}

sealed interface ResultUiState {

    data object Loading : ResultUiState
    data object Failed : ResultUiState

    data class Success(val uiState: MainUiState) : ResultUiState
}

class MainUiState(
    val totalCount: Int,
    val isIncompleteResults: Boolean,
    val items: List<MainItemUiState>
)

class MainItemUiState(
    val repositoryDto: RepositoryDto
)
