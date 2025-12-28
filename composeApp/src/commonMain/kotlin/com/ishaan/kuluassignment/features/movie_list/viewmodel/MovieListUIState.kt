package com.ishaan.kuluassignment.features.movie_list.viewmodel

import com.ishaan.kuluassignment.db.MovieEntity

data class MovieListUIState(
    val isLoading: Boolean = false,
    val movies: List<MovieEntity> = emptyList(),
    val error: String? = null
)

sealed class MovieListUIEvent {

}
