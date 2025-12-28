package com.ishaan.kuluassignment.features.movie_list.viewmodel

import com.ishaan.kuluassignment.db.MovieEntity

data class MovieListUIState(
    val isLoading: Boolean = false,
    val movies: List<MovieEntity> = emptyList(),
    val error: String? = null,
    val isPaginationLoading: Boolean = false,
    val paginationError: String? = null,
    val isOffline: Boolean = false,
    val currentPage: Int = 0
)

sealed class MovieListUIEvent {

}
