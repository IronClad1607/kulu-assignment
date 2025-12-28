package com.ishaan.kuluassignment.features.movie_list.viewmodel

import androidx.lifecycle.viewModelScope
import com.ishaan.kuluassignment.base.BaseViewModel
import com.ishaan.kuluassignment.features.movie_list.model.MovieRepository
import com.ishaan.kuluassignment.utils.Logger
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MovieListViewModel(
    private val movieRepository: MovieRepository
) : BaseViewModel<MovieListUIState, MovieListUIEvent>(MovieListUIState()) {

    private var searchJob: Job? = null

    init {
        observeMovies()
        loadInitialData()
    }

    private fun observeMovies() {
        movieRepository.moviesFlow
            .onEach { cachedMovie ->
                safeUpdateState { oldState ->
                    val maxPage = cachedMovie.maxOfOrNull { it.page }?.toInt() ?: 0
                    oldState.copy(
                        movies = cachedMovie,
                        currentPage = maxPage,
                        isLoading = if (cachedMovie.isNotEmpty()) false else oldState.isLoading,
                        error = if (cachedMovie.isNotEmpty()) null else oldState.error
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadInitialData() {
        if (uiState.value.movies.isEmpty()) {
            safeUpdateState { it.copy(isLoading = true, error = null) }
        }
        viewModelScope.launch {
            loadData(isRefresh = true)
        }
    }

    private fun loadNextPage() {
        val state = uiState.value
        if (state.isPaginationLoading || state.isLoading) return

        safeUpdateState { it.copy(isPaginationLoading = true, paginationError = null) }

        viewModelScope.launch {
            val lastPage = movieRepository.getLastLoadedPage().toInt()
            val nextPage = lastPage + 1
            loadData(isRefresh = false, pageOverride = nextPage)
        }
    }

    fun onEvent(event: MovieListUIEvent) {
        when (event) {
            is MovieListUIEvent.OnSearchIconClicked -> {
                safeUpdateState { oldState ->
                    if (!event.isOpen) {
                        oldState.copy(
                            isSearchOpen = false,
                            searchText = ""
                        )
                    } else {
                        oldState.copy(
                            isSearchOpen = true,
                            searchText = ""
                        )
                    }
                }
            }

            is MovieListUIEvent.OnSearchQueryChanged -> {
                onSearchQueryChanged(event.query)
            }

            MovieListUIEvent.LoadNextPage -> {
                loadNextPage()
            }
        }
    }

    private fun onSearchQueryChanged(query: String) {
        safeUpdateState { it.copy(searchText = query) }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500) // Debounce

            // Trigger a refresh (page 1) with the new query
            loadData(isRefresh = true)
        }
    }

    private suspend fun loadData(isRefresh: Boolean, pageOverride: Int? = null) {
        val query = uiState.value.searchText
        val page = pageOverride ?: 1

        // 1. Choose Endpoint
        val result = if (query.isNotEmpty()) {
            movieRepository.searchMovies(query, page, isRefresh)
        } else {
            movieRepository.fetchAndSaveMovies(page, isRefresh)
        }

        // 2. Handle Result
        result.onSuccess {
            safeUpdateState {
                it.copy(
                    isPaginationLoading = false,
                    isOffline = false,
                    isLoading = false,
                    error = null
                )
            }
        }.onFailure { error ->
            handleError(error, isRefresh)
        }
    }

    private fun handleError(error: Throwable, isRefresh: Boolean) {
        val hasData = uiState.value.movies.isNotEmpty()

        if (isRefresh && !hasData) {
            // Case: Offline + No DB Data -> Full Screen Error
            safeUpdateState {
                it.copy(isLoading = false, error = error.message, isOffline = true)
            }
        } else {
            // Case: Offline + Data Exists -> Persistent Indicator or Snackbar
            val errorMessage = if (isRefresh)
                "Offline: Showing cached data"
            else
                "Could not load more: ${error.message}"

            safeUpdateState {
                it.copy(
                    isPaginationLoading = false,
                    isOffline = true,
                    paginationError = errorMessage
                )
            }
        }
    }
}