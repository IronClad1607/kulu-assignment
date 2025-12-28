package com.ishaan.kuluassignment.features.movie_list.view

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ishaan.kuluassignment.theme.MyAppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MovieListScreen(
    navigateToMovieDetail: () -> Unit,
    modifier: Modifier = Modifier
) {
    MovieListScreenContent()
}

@Composable
fun MovieListScreenContent(
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {

        }
    ) { 
        
    }
}

@Preview
@Composable
fun MovieListScreenContentPreview() {
    MyAppTheme {
        MovieListScreenContent()
    }
}

@Preview
@Composable
fun MovieListScreenContentDarkPreview() {
    MyAppTheme(darkTheme = true) {
        MovieListScreenContent()
    }
}