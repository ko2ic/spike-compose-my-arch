package com.ko2ic.sample.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ko2ic.sample.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val gitHubRepository: GitHubRepository,
) : ViewModel() {

    fun fetch(query: String, page: Int) {
        gitHubRepository.fetch(query, page).onEach {
            Log.d("", it.toString())
        }.catch { e ->
            Log.e("", e.toString())
        }.onCompletion {

        }.launchIn(viewModelScope)
    }
}