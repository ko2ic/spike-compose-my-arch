package com.ko2ic.sample.repository.http

import com.ko2ic.sample.model.dto.jsonplaceholder.PostResponseDto
import retrofit2.http.GET

interface JsonPlaceHolderHttpClient {

    @GET("/posts")
    suspend fun fetchPosts(): PostResponseDto

    @GET("/posts/404")
    suspend fun fetchPostsError(): PostResponseDto
}