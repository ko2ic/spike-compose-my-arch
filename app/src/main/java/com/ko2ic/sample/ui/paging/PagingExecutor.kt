package com.ko2ic.sample.ui.paging

import com.ko2ic.sample.ui.coroutines.ApiBaseState
import com.ko2ic.sample.ui.stable.StableReachedLoadPageInvoker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onCompletion

class PagingExecutor<T : PagingUiState> {

    // 外部から渡す
    var forPullRefresh = false

    // 最初のページかどうか
    private var isFirstPage = true

    // 更に読み込む(or最新のページ)かどうかの判定
    private var isLastPageForNext = false

    // アイテムのindex。ページングが動いても続きからカウントされる
    private var index = 0

    // 現在のページ（1始まり。fetchが呼ばれるたびにプラスされる）
    private var currentPage = 0

    private var pagingUiState: T? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    fun <R : PagingCursor> execute(
        viewModel: PagingViewModel,
        apiBaseState: ApiBaseState,
        fetchList: () -> Flow<R>,
        createItemUiState: (R, StableReachedLoadPageInvoker, Int) -> PagingUiState,
        setNextCursor: ((R) -> Unit)? = null,
        onPostSuccess: ((isLastPage: Boolean, currentPage: Int) -> Unit)? = null,
    ): Flow<T?> {

        if (forPullRefresh) {
            isFirstPage = true
        }

        val flow = fetchList().mapLatest { result ->
            currentPage++
            val pagingInvoker = StableReachedLoadPageInvoker {
                val oldItems = pagingUiState?.items?.toMutableList()
                pagingUiState = pagingUiState?.copyItems(items = oldItems ?: emptyList()) as? T
                if (setNextCursor == null) {
                    viewModel.nextCursor.value = result.nextCursor
                    isLastPageForNext = result.nextCursor == null
                } else {
                    setNextCursor(result)
                    isLastPageForNext = viewModel.nextCursor.value == null
                }
            }
            val newUiState = createItemUiState(result, pagingInvoker, index++)

            if (isFirstPage) {
                pagingUiState = newUiState as? T
            } else {
                val oldItems = pagingUiState?.items?.toMutableList()
                oldItems?.addAll(newUiState.items)
                pagingUiState = newUiState.copyItems(items = oldItems ?: emptyList()) as? T
            }

            if (onPostSuccess != null) {
                onPostSuccess(isLastPageForNext, currentPage)
            }
            pagingUiState
        }.onCompletion { cause ->
            isFirstPage = false
            forPullRefresh = false
            apiBaseState.loading(false)
        }

        if (isFirstPage) {
            apiBaseState.loading(true)
        }

        return flow
    }

    fun reset(viewModel: PagingViewModel, customInvoker: (() -> Unit)? = null) {
        viewModel.refreshFlow.value = true
        pagingUiState = null
        forPullRefresh = false
        isFirstPage = true
        index = 0
        currentPage = 0
        viewModel.nextCursor.value = null
        viewModel.scrollPosition.value = 0
        customInvoker?.invoke()
        viewModel.refreshFlow.value = false
    }
}