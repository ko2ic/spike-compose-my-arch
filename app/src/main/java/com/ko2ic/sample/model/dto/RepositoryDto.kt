package com.ko2ic.sample.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepositoryDto(

    @SerialName("name")
    val name: String,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("description")
    val description: String,
    @SerialName("html_url")
    val url: String,
    @SerialName("stargazers_count")
    val stars: Int,
    @SerialName("forks_count")
    val forks: Int,
    @SerialName("language")
    val language: String?

)
