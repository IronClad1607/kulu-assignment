package com.ishaan.kuluassignment.features.movie_detail.viewmodel

import androidx.lifecycle.viewModelScope
import com.ishaan.kuluassignment.base.BaseViewModel
import com.ishaan.kuluassignment.features.MovieRepository
import kotlinx.coroutines.launch

// View Model linked to Movie Detail Screen
class MovieDetailViewModel(
    private val movieRepository: MovieRepository
) : BaseViewModel<MovieDetailUIState, MovieDetailUIEvent>(MovieDetailUIState()) {

    // Get movie details from repository
    fun getMovieDetails(movieId: Long) {
        viewModelScope.launch {
            // Update UI state to show loading
            safeUpdateState { it.copy(isLoading = true, error = null) }
            try {
                // Fetch movie details from repository
                val movieDetails = movieRepository.getMovieById(movieId)
                if (movieDetails != null) {
                    safeUpdateState { it.copy(movie = movieDetails, error = null) }
                } else {
                    safeUpdateState { it.copy(error = "Movie not found") }
                }
            } catch (e: Exception) {
                safeUpdateState { it.copy(error = e.message) }
            } finally {
                safeUpdateState { it.copy(isLoading = false) }
            }
        }
    }
}