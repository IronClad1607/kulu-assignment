package com.ishaan.kuluassignment.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed interface Route {
    val route: String
}

data object MovieListScreen : Route {
    override val route: String
        get() = "movie_list_screen"
}

data object MovieDetailScreen : Route {
    override val route: String
        get() = "movie_detail_screen"

    const val MOVIE_ID_ARGS = "movieId"

    val routeWithArgs = "$route/{$MOVIE_ID_ARGS}"

    val arguments = listOf(
        navArgument(MOVIE_ID_ARGS) {
            type = NavType.LongType
            nullable = false
            defaultValue = 0L
        }
    )

    fun createRoute(movieId: Long): String {
        return "$route/$movieId"
    }
}