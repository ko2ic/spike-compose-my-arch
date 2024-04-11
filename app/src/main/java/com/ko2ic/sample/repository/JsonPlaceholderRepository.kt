package com.ko2ic.sample.repository

import com.ko2ic.sample.model.dto.jsonplaceholder.PostResponseDto
import kotlinx.coroutines.flow.Flow

interface JsonPlaceholderRepository {

    fun fetch(): Flow<PostResponseDto>

    fun fetchPostsError(): Flow<PostResponseDto>
}