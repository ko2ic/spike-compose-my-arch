package com.ko2ic.sample.model.dto.github

import com.ko2ic.sample.ui.paging.PagingCursor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponseDto(

    @SerialName("total_count")
    val totalCount: Int,
    @SerialName("incomplete_results")
    val isIncompleteResults: Boolean,
    @SerialName("items")
    val items: List<RepositoryDto>
) : PagingCursor {

    override var nextCursor: String? = null

}