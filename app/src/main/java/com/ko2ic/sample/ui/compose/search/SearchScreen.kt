package com.ko2ic.sample.ui.compose.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ko2ic.sample.R
import com.ko2ic.sample.model.dto.github.RepositoryDto
import com.ko2ic.sample.ui.stable.StableOnClickItemInvoker
import com.ko2ic.sample.ui.stable.StableReachedLoadPageInvoker
import com.ko2ic.sample.ui.stable.StableSendViewableLogInvoker
import com.ko2ic.sample.ui.transition.ApiErrorLaunchedEffect
import com.ko2ic.sample.ui.transition.BottomTabType
import com.ko2ic.sample.ui.transition.LoadingLaunchedEffect
import com.ko2ic.sample.ui.transition.MyNavigationBar
import com.ko2ic.sample.ui.transition.TransitionEventLaunchedEffect
import com.ko2ic.sample.ui.transition.webViewScreenRoute
import com.ko2ic.sample.ui.transition.webViewScreenRouteParameterName
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigateToRoute: (String) -> Unit,
    navigateToNextPage: (String) -> Unit
) {
    val inputUiState by viewModel.inputUiState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ApiErrorLaunchedEffect(viewModel.apiBaseState.error)
    TransitionEventLaunchedEffect(viewModel.navigationChannel) { event ->
        when (event) {
            is SearchViewModel.TransitionEvent.OnClickItem -> {
                val encodedUrl = URLEncoder.encode(event.url, StandardCharsets.UTF_8.toString())
                navigateToNextPage(webViewScreenRoute.replace("{$webViewScreenRouteParameterName}", encodedUrl))
            }
        }
    }

    // Previewを使うためにViewModelに依存しないようにしている
    SearchScreen(
        inputUiState,
        uiState,
        fetch = remember { viewModel::init },
        onNavigateToRoute = remember {
            onNavigateToRoute
        },
    ) {
        LoadingLaunchedEffect(viewModel.apiBaseState.loading)
    }
}

@Composable
fun SearchScreen(
    inputUiState: String,
    uiState: SearchUiState?,
    fetch: (String) -> Unit,
    onNavigateToRoute: (String) -> Unit,
    loadingLaunchedEffect: @Composable () -> Unit,
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val inputValue = rememberSaveable { mutableStateOf(inputUiState) }

    Scaffold(
        bottomBar = {
            MyNavigationBar(
                BottomTabType.entries,
                BottomTabType.SEARCH.route,
                onNavigateToRoute,
            )
        }
    ) { padding ->
        loadingLaunchedEffect()
        Column(modifier = Modifier.padding(padding)) {
            OutlinedTextField(
                value = inputValue.value,
                onValueChange = { inputValue.value = it },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    fetch(inputValue.value)
                    keyboardController?.hide() // Hide the keyboard after search
                }),
                label = { Text(text = stringResource(id = R.string.search_hint)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            RepositoriesScreen(uiState)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchScreen() {
    val sampleInputUiState = "Sample Input"
    val sampleUiState = SearchUiState(
        totalCount = 100,
        isIncompleteResults = false,
        items = listOf(
            RepositoriesItemUiState(
                repositoryDto = RepositoryDto(
                    name = "name",
                    fullName = "SampleRepo",
                    description = "This is a sample repository",
                    url = "https://example.com/repo",
                    language = "Kotlin",
                    stars = 100,
                    forks = 50,
                    owner = JsonObject(
                        mapOf(
                            "avatar_url" to JsonPrimitive("https://example.com/avatar.png")
                        )
                    ),
                ),
                onClickItemInvoker = StableOnClickItemInvoker { },
            )
        ),
        sendViewableLogInvoker = StableSendViewableLogInvoker { },
        reachedLoadPageInvoker = StableReachedLoadPageInvoker { }
    )

    SearchScreen(
        inputUiState = sampleInputUiState,
        uiState = sampleUiState,
        fetch = { _ -> },
        onNavigateToRoute = {},
        loadingLaunchedEffect = {}
    )
}
