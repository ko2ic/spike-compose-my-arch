package com.ko2ic.sample.ui.compose.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ko2ic.sample.R
import com.ko2ic.sample.ui.transition.BottomTabType
import com.ko2ic.sample.ui.transition.MyNavigationBar

@Composable
fun ProfileScreen(
    onNavigateToRoute: (String) -> Unit,
) {
    Scaffold(
        bottomBar = {
            MyNavigationBar(
                BottomTabType.entries,
                BottomTabType.PROFILE.route,
                onNavigateToRoute,
            )
        }
    ) { padding ->
        ItemCircularProgressIndicator()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding), contentAlignment = Alignment.Center
        ) {
            Button(onClick = {

            }) {
                Text(text = stringResource(R.string.profile))
            }
        }
    }
}

@Composable
fun ItemCircularProgressIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}