package com.ko2ic.sample.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepositoryDto(

    @SerialName("full_name")
    val fullName: String,

    @SerialName("stargazers_count")
    val stars: Int,

    @SerialName("permissions")
    val permissions: PermissionDto? = null
)
