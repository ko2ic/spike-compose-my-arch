package com.ko2ic.sample.ui.compose.search

import androidx.compose.runtime.Stable
import com.ko2ic.sample.model.dto.github.RepositoryDto
import com.ko2ic.sample.ui.CollectionItemUiState
import com.ko2ic.sample.ui.stable.StableOnClickItemInvoker

@Stable
data class RepositoriesItemUiState(
    val repositoryDto: RepositoryDto,
    private val onClickItemInvoker: StableOnClickItemInvoker
) : CollectionItemUiState {

    val fullName = repositoryDto.fullName
    val description = repositoryDto.description
    val language = repositoryDto.language
    val stars = repositoryDto.stars
    val forks = repositoryDto.forks
    val avatarUrl = repositoryDto.avatarUrl

    fun onClickItem() {
        onClickItemInvoker.invoke()
    }
}