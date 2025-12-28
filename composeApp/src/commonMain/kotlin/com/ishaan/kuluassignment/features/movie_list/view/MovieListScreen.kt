package com.ishaan.kuluassignment.features.movie_list.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.ishaan.kuluassignment.base.EmptyComposable
import com.ishaan.kuluassignment.base.ErrorComposable
import com.ishaan.kuluassignment.base.LoadingComposable
import com.ishaan.kuluassignment.db.MovieEntity
import com.ishaan.kuluassignment.features.movie_list.viewmodel.MovieListUIEvent
import com.ishaan.kuluassignment.features.movie_list.viewmodel.MovieListUIState
import com.ishaan.kuluassignment.features.movie_list.viewmodel.MovieListViewModel
import com.ishaan.kuluassignment.theme.MyAppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MovieListScreen(
    navigateToMovieDetail: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MovieListViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
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
                            onEvent(MovieListUIEvent.OnSearchQueryChanged(it))
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
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            if (uiState.isOffline || uiState.paginationError != null) {
                OfflineIndicator(
                    isOffline = uiState.isOffline,
                    paginationError = uiState.paginationError,
                    modifier = Modifier.align(Alignment.BottomCenter)
                        .zIndex(1f)
                )
            }

            if (uiState.error != null && uiState.movies.isEmpty()) {
                ErrorComposable(
                    message = uiState.error,
                    onRetryClicked = {
                        onEvent(MovieListUIEvent.OnSearchQueryChanged(uiState.searchText))
                    }
                )
            } else if (uiState.isLoading && uiState.movies.isEmpty()) {
                LoadingComposable()
            } else if (uiState.movies.isEmpty()) {
                EmptyComposable(
                    message = "No movies found"
                )
            } else {
                MovieGrid(
                    movies = uiState.movies,
                    isLoadingMore = uiState.isPaginationLoading,
                    onLoadMore = {
                        onEvent(MovieListUIEvent.LoadNextPage)
                    },
                    onItemClick = {
                        navigateToMovieDetail()
                    },
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Composable
fun MovieGrid(
    movies: List<MovieEntity>,
    isLoadingMore: Boolean,
    onLoadMore: () -> Unit,
    onItemClick: (MovieEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val gridState = rememberLazyGridState()
    val shouldLoadMore = remember {
        derivedStateOf {
            val layoutInfo = gridState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            // Trigger when within 4 items of the end
            lastVisibleItemIndex >= (totalItems - 4) && totalItems > 0
        }
    }
    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            onLoadMore()
        }
    }

    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(movies) {
            MovieItem(
                posterPath = it.posterPath ?: "",
                title = it.title,
                onClick = {
                    onItemClick(it)
                },
            )
        }
        if (isLoadingMore) {
            item(
                span = { GridItemSpan(2) }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
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