package com.ko2ic.sample.repository

import com.ko2ic.sample.model.dto.github.SearchResponseDto
import kotlinx.coroutines.flow.Flow

interface GitHubRepository {

    fun fetch(query: String, page: Int): Flow<SearchResponseDto>
}