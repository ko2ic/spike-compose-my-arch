package com.ko2ic.sample.ui.customview

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun MyLazyColumn(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    viewableIndex: ((Int) -> Unit)? = null,
    pageLoadPosition: (() -> Unit)? = null,
    content: LazyListScope.() -> Unit
) {

    LazyColumn(
        modifier,
        state,
        contentPadding,
        reverseLayout,
        verticalArrangement,
        horizontalAlignment,
        flingBehavior,
        userScrollEnabled,
    ) {
        content()
    }

    LaunchedEffect(state) {
        snapshotFlow {
            val totalCount = state.layoutInfo.totalItemsCount
            if (totalCount > 0) {
                val visibleCount = state.layoutInfo.visibleItemsInfo.size
                val firstPosition = state.layoutInfo.visibleItemsInfo.first().index
                totalCount <= 10 + firstPosition || totalCount == visibleCount + firstPosition
            } else {
                false
            }
        }.distinctUntilChanged()
            .filter {
                it
            }.collect {
                pageLoadPosition?.invoke()
            }
    }

    LaunchedEffect(state) {
        snapshotFlow {
            if (state.layoutInfo.visibleItemsInfo.isEmpty()) {
                Pair(false, 0)
            } else {
                val itemHeight = state.layoutInfo.visibleItemsInfo.last().size
                val itemTop = state.layoutInfo.visibleItemsInfo.last().offset
                val deviceHeight = state.layoutInfo.viewportSize.height
                val viewableTopPixel = deviceHeight - itemTop
                val isTarget = viewableTopPixel > itemHeight * 0.7
                Pair(isTarget, state.layoutInfo.visibleItemsInfo.last().index)
            }
        }.distinctUntilChanged()
            .filter {
                it.first
            }.collect {
                viewableIndex?.invoke(it.second)
            }
    }
}