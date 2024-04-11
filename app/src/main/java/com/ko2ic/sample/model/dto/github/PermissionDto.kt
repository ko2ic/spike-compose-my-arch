package com.ko2ic.sample.model.dto.github

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PermissionDto(
    @SerialName("admin")
    val haveAdmin: Boolean,

    @SerialName("push")
    val havePushAuthorizetion: Boolean,

    @SerialName("pull")
    val havePullAuthorizetion: Boolean,
)