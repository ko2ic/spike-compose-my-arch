package com.ko2ic.sample.ui.transition

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.ko2ic.sample.R

enum class BottomTabType(
    @StringRes val titleResId: Int,
    val icon: ImageVector,
    val route: String
) {

    SEARCH(R.string.search, Icons.Outlined.Search, "top/search"),
    LIST(R.string.list, Icons.AutoMirrored.Outlined.List, "top/list"),
    PROFILE(R.string.profile, Icons.Outlined.AccountCircle, "top/profile")
}