package com.ko2ic.sample.repository

import com.ko2ic.sample.model.dto.github.SearchResponseDto
import ko2ic.sample.repository.http.GitHubHttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GitHubRepositoryImpl @Inject constructor(
    private val gitHubHttpClient: GitHubHttpClient,
) : GitHubRepository {

    override fun fetch(query: String, page: Int): Flow<SearchResponseDto> {
        return flow {
            emit(gitHubHttpClient.fetchRepository(query, page))
        }.flowOn(Dispatchers.IO)
    }

}