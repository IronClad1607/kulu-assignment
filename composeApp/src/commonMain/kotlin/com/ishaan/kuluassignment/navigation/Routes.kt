package com.ishaan.kuluassignment.navigation

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
}