package com.ko2ic.sample.model.dto.github

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class RepositoryDto(

    @SerialName("name")
    val name: String,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("description")
    val description: String? = "",
    @SerialName("html_url")
    val url: String,
    @SerialName("stargazers_count")
    val stars: Int,
    @SerialName("forks_count")
    val forks: Int,
    @SerialName("language")
    val language: String?,
    @SerialName("owner") private val owner: JsonElement
) {

    val avatarUrl: String
        get() = owner.jsonObject["avatar_url"]?.jsonPrimitive?.content ?: ""
}

