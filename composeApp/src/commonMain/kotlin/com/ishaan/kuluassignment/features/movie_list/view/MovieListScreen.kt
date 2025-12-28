package com.ishaan.kuluassignment.features.movie_list.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.ishaan.kuluassignment.features.movie_list.viewmodel.MovieListUIEvent
import com.ishaan.kuluassignment.features.movie_list.viewmodel.MovieListUIState
import com.ishaan.kuluassignment.features.movie_list.viewmodel.MovieListViewModel
import com.ishaan.kuluassignment.theme.MyAppTheme
import com.ishaan.kuluassignment.utils.Logger
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MovieListScreen(
    navigateToMovieDetail: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MovieListViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(uiState) {
        Logger.testLog.d {
            buildString {
                appendLine("isLoading: ${uiState.isLoading}")
                appendLine("movies: ${uiState.movies.size}")
            }
        }
    }
    MovieListScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        navigateToMovieDetail = navigateToMovieDetail,
        modifier = modifier
    )
}

@Composable
fun MovieListScreenContent(
    uiState: MovieListUIState,
    onEvent: (MovieListUIEvent) -> Unit = {},
    navigateToMovieDetail: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            AnimatedContent(
                targetState = uiState.isSearchOpen,
                label = "Search Bar"
            ) { isSearchActive ->
                if (isSearchActive) {
                    SearchBar(
                        searchText = uiState.searchText,
                        onSearchTextChange = {

                        },
                        onCloseClick = {
                            onEvent(MovieListUIEvent.OnSearchIconClicked(false))
                        }
                    )
                } else {
                    DefaultMovieListAppBar(
                        onSearchIconClicked = {
                            onEvent(MovieListUIEvent.OnSearchIconClicked(true))
                        }
                    )
                }
            }
        }
    ) {innerPadding ->

    }
}

@Preview
@Composable
fun MovieListScreenContentPreview() {
    MyAppTheme {
        MovieListScreenContent(
            uiState = MovieListUIState()
        )
    }
}

@Preview
@Composable
fun MovieListScreenContentDarkPreview() {
    MyAppTheme(darkTheme = true) {
        MovieListScreenContent(
            uiState = MovieListUIState()
        )
    }
}