package com.ko2ic.sample.ui.paging

import kotlinx.coroutines.flow.MutableStateFlow

interface PagingViewModel {

    val nextCursor: MutableStateFlow<String?>
    val refreshFlow: MutableStateFlow<Boolean>
    val scrollPosition: MutableStateFlow<Int?>
}