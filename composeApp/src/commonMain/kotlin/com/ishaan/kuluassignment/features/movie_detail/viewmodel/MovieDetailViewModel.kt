package com.ishaan.kuluassignment.features.movie_detail.viewmodel

import androidx.lifecycle.viewModelScope
import com.ishaan.kuluassignment.base.BaseViewModel
import com.ishaan.kuluassignment.features.movie_list.model.MovieRepository
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val movieRepository: MovieRepository
) : BaseViewModel<MovieDetailUIState, MovieDetailUIEvent>(MovieDetailUIState()) {

    fun getMovieDetails(movieId: Long) {
        viewModelScope.launch {
            safeUpdateState { it.copy(isLoading = true, error = null) }
            try {
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