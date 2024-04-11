package com.ko2ic.sample.model.dto.jsonplaceholder

import kotlinx.serialization.Serializable

typealias PostResponseDto = List<PostDto>

@Serializable
data class PostDto(
    val userId: Long,
    val id: Long,
    val title: String,
    val body: String
)
