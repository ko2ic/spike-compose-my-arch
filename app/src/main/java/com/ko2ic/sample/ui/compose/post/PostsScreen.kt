package com.ko2ic.sample.ui.compose.post

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ko2ic.sample.R
import com.ko2ic.sample.ui.stable.StableOnClickItemInvoker
import com.ko2ic.sample.ui.transition.ApiErrorLaunchedEffect
import com.ko2ic.sample.ui.transition.BottomTabType
import com.ko2ic.sample.ui.transition.LoadingLaunchedEffect
import com.ko2ic.sample.ui.transition.MyNavigationBar

@Composable
fun PostsScreen(
    viewModel: PostsViewModel = hiltViewModel(),
    onNavigateToRoute: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ApiErrorLaunchedEffect(viewModel.apiBaseState.error)

    PostsScreen(
        uiState,
        onNavigateToRoute,
    ) {
        LoadingLaunchedEffect(viewModel.apiBaseState.loading)
    }
}

@Composable
fun PostsScreen(
    uiState: List<PostItemUiState>,
    onNavigateToRoute: (String) -> Unit,
    loadingLaunchedEffect: @Composable () -> Unit,
) {
    Scaffold(
        bottomBar = {
            MyNavigationBar(
                BottomTabType.entries,
                BottomTabType.LIST.route,
                onNavigateToRoute,
            )
        }
    ) { paddingValues ->
        loadingLaunchedEffect()
        val items = uiState
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            items(count = items.count()) { index ->
                val item = items[index]
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable(onClick = item::onClickItem),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_star),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
                HorizontalDivider(thickness = 1.dp, color = Color.Black)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPostsScreen() {
    val sampleUiState = listOf(
        PostItemUiState(
            userId = 1,
            id = 1,
            title = "Sample Post 1",
            body = "This is a sample post body.",
            onClickItemInvoker = StableOnClickItemInvoker { }
        ),
        PostItemUiState(
            userId = 2,
            id = 2,
            title = "Sample Post 2",
            body = "This is a sample post body.",
            onClickItemInvoker = StableOnClickItemInvoker { }
        ),
    )

    PostsScreen(
        uiState = sampleUiState,
        onNavigateToRoute = {},
        loadingLaunchedEffect = {}
    )
}