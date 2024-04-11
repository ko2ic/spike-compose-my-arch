package com.ko2ic.sample.repository

import com.ko2ic.sample.model.dto.jsonplaceholder.PostResponseDto
import com.ko2ic.sample.repository.http.JsonPlaceHolderHttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class JsonPlaceholderRepositoryImpl @Inject constructor(
    private val httpClient: JsonPlaceHolderHttpClient,
) : JsonPlaceholderRepository {

    override fun fetch(): Flow<PostResponseDto> {
        return flow {
            emit(httpClient.fetchPosts())
        }.flowOn(Dispatchers.IO)
    }

    override fun fetchPostsError(): Flow<PostResponseDto> {
        return flow {
            emit(httpClient.fetchPostsError())
        }.flowOn(Dispatchers.IO)
    }
}