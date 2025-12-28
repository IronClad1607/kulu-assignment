package com.ishaan.kuluassignment.features.movie_list.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.ishaan.kuluassignment.theme.MyAppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

// Default Movie List App Bar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultMovieListAppBar(
    onSearchIconClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = "Trending Movies",
                style = MaterialTheme.typography.titleMedium
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            titleContentColor = MaterialTheme.colorScheme.onSecondary,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondary
        ),
        actions = {
            IconButton(
                onClick = {
                    onSearchIconClicked()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Movies"
                )
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp)
    )
}

@Preview
@Composable
fun DefaultMovieListAppBarPreview() {
    MyAppTheme {
        DefaultMovieListAppBar(
            onSearchIconClicked = {}
        )
    }
}