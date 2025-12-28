package com.ishaan.kuluassignment.features.movie_list.viewmodel

import androidx.lifecycle.viewModelScope
import com.ishaan.kuluassignment.base.BaseViewModel
import com.ishaan.kuluassignment.features.movie_list.model.MovieRepository
import com.ishaan.kuluassignment.utils.Logger
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MovieListViewModel(
    private val movieRepository: MovieRepository
) : BaseViewModel<MovieListUIState, MovieListUIEvent>(MovieListUIState()) {

    init {
        observeMovies()
        loadInitialData()
    }

    private fun observeMovies() {
        movieRepository.moviesFlow
            .onEach { cachedMovie ->
                Logger.testLog.d {
                    "cachedMovie: ${cachedMovie.size}"
                }
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
        Logger.testLog.d {
            "loadInitialData called"
        }
        val currentState = uiState.value

        if (currentState.movies.isEmpty()) {
            safeUpdateState { oldState ->
                oldState.copy(isLoading = true, error = null)
            }
        }

        viewModelScope.launch {
            val result = movieRepository.fetchAndSaveMovies(1)

            result.onSuccess {
                safeUpdateState { oldState ->
                    oldState.copy(
                        isLoading = false,
                        error = null,
                        isOffline = false
                    )
                }
            }.onFailure { error ->
                if (currentState.movies.isEmpty()) {
                    safeUpdateState { oldState ->
                        oldState.copy(
                            isLoading = false,
                            error = error.message,
                            isOffline = true
                        )
                    }
                } else {
                    safeUpdateState { oldState ->
                        oldState.copy(
                            isOffline = true
                        )
                    }
                }
            }
        }
    }

    private fun loadNextPage() {
        val currentState = uiState.value
        if (currentState.isPaginationLoading || currentState.isLoading) {
            return
        }

        safeUpdateState { oldState ->
            oldState.copy(isPaginationLoading = true, paginationError = null)
        }

        viewModelScope.launch {
            val lastPage = movieRepository.getLastLoadedPage().toInt()
            val nextPage = lastPage + 1

            val result = movieRepository.fetchAndSaveMovies(nextPage, isRefresh = false)
            result.onSuccess {
                safeUpdateState { oldState ->
                    oldState.copy(
                        isPaginationLoading = false,
                        paginationError = null,
                        isOffline = true
                    )
                }
            }.onFailure {
                safeUpdateState { oldState ->
                    oldState.copy(
                        isPaginationLoading = false,
                        paginationError = "Could not load more: ${it.message}",
                        isOffline = true
                    )
                }
            }
        }
    }
}