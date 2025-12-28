package com.ishaan.kuluassignment.features.movie_detail.viewmodel

import com.ishaan.kuluassignment.db.MovieEntity

data class MovieDetailUIState(
    val isLoading: Boolean = false,
    val movie: MovieEntity? = null,
    val error: String? = null
)

sealed class MovieDetailUIEvent {
}

