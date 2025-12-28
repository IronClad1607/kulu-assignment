package com.ishaan.kuluassignment.features.movie_list.viewmodel

import androidx.lifecycle.viewModelScope
import com.ishaan.kuluassignment.base.BaseViewModel
import com.ishaan.kuluassignment.features.MovieRepository
import com.ishaan.kuluassignment.utils.Logger
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MovieListViewModel(
    private val movieRepository: MovieRepository
) : BaseViewModel<MovieListUIState, MovieListUIEvent>(MovieListUIState()) {

    // Search related properties
    private var searchJob: Job? = null
    private val _searchQueryFlow = MutableStateFlow("")

    init {
        observeMovies()
        loadInitialData()
    }


    // Observe movies from the repository and update the UI state
    private fun observeMovies() {
        val isOfflineFlow = uiState.map { it.isOffline }.distinctUntilChanged()
        combine(
            movieRepository.moviesFlow,
            _searchQueryFlow,
            isOfflineFlow
        ) { cachedMovies, query, isOffline ->
            val maxPage = cachedMovies.maxOfOrNull { it.page }?.toInt() ?: 0

            // Perform filtering
            val filteredMovies = if (query.isNotEmpty() && isOffline) {
                cachedMovies.filter {
                    it.title.contains(query, ignoreCase = true)
                }
            } else {
                cachedMovies
            }

            // Return the calculated data to the onEach block
            Triple(filteredMovies, cachedMovies, maxPage)
        }.onEach { (filteredMovies, originalCachedMovies, maxPage) ->
            safeUpdateState { oldState ->
                oldState.copy(
                    movies = filteredMovies,
                    currentPage = maxPage,
                    isLoading = if (originalCachedMovies.isNotEmpty()) false else oldState.isLoading,
                    error = if (originalCachedMovies.isNotEmpty()) null else oldState.error
                )
            }
        }.launchIn(viewModelScope)
    }

    // Load initial data from the repository
    private fun loadInitialData() {
        Logger.analyticLog.i {
            "Loading initial data"
        }
        if (uiState.value.movies.isEmpty()) {
            safeUpdateState { it.copy(isLoading = true, error = null) }
        }
        viewModelScope.launch {
            loadData(isRefresh = true)
        }
    }

    // Load next page of data from the repository
    private fun loadNextPage() {
        val state = uiState.value
        if (state.isPaginationLoading || state.isLoading) return

        safeUpdateState { it.copy(isPaginationLoading = true, paginationError = null) }

        viewModelScope.launch {
            // get current page from repository
            val lastPage = movieRepository.getLastLoadedPage().toInt()
            val nextPage = lastPage + 1

            Logger.analyticLog.i {
                "Loading next page: $nextPage"
            }
            // load data
            loadData(isRefresh = false, pageOverride = nextPage)
        }
    }

    fun onEvent(event: MovieListUIEvent) {
        when (event) {
            // Handle search icon click
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
                        )
                    }
                }
            }

            // Handle search query change
            is MovieListUIEvent.OnSearchQueryChanged -> {
                onSearchQueryChanged(event.query)
            }

            // Handle load next page event
            MovieListUIEvent.LoadNextPage -> {
                loadNextPage()
            }
        }
    }

    // Handle search query change
    private fun onSearchQueryChanged(query: String) {
        safeUpdateState { it.copy(searchText = query) }

        _searchQueryFlow.value = query

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500) // Debounce

            Logger.analyticLog.i {
                "Searching movie using: $query"
            }

            // Trigger a refresh (page 1) with the new query
            loadData(isRefresh = true)
        }
    }

    // Common Function to load data from the repository
    private suspend fun loadData(isRefresh: Boolean, pageOverride: Int? = null) {
        val query = uiState.value.searchText
        val page = pageOverride ?: 1

        // Choose Endpoint
        val result = if (query.isNotEmpty()) {
            movieRepository.searchMovies(query, page, isRefresh)
        } else {
            movieRepository.fetchAndSaveMovies(page, isRefresh)
        }

        // Handle Result
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