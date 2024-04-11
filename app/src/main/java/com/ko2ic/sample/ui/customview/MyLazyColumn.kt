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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
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
    scrollPosition: Int? = null,
    viewableIndex: ((Int) -> Unit)? = null,
    pageLoadPosition: (() -> Unit)? = null,
    content: LazyListScope.() -> Unit
) {

    scrollPosition?.let {
        LaunchedEffect(scrollPosition) {
            state.scrollToItem(scrollPosition)
        }
    }

    var showLoadingIndicator by remember { mutableStateOf(false) }
    LaunchedEffect(showLoadingIndicator) {
        if (showLoadingIndicator) {
            delay(10_000)
            showLoadingIndicator = false
        }
    }

    LaunchedEffect(state) {
        snapshotFlow {
            val totalCount = state.layoutInfo.totalItemsCount
            if (totalCount > 0) {
                val visibleCount = state.layoutInfo.visibleItemsInfo.size
                val firstPosition = state.layoutInfo.visibleItemsInfo.first().index
                totalCount <= 10 + firstPosition || totalCount == visibleCount + firstPosition
            } else {
                showLoadingIndicator = false
                false
            }
        }.distinctUntilChanged()
            .filter {
                it
            }.collect {
                showLoadingIndicator = true
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
        if (showLoadingIndicator) {
            item {
                LoadingIndicator()
            }
        }
    }

}